package NN;

import java.util.ArrayList;
import java.util.Random;


public class NeuralNetwork extends SupervisedLearner {
    
    
    // Micellaneous variables to avoid using magic numbers
    private final boolean DEBUG = true;
    private final int MAX_EPOCHS = 5000000;
    private final int ONLY_COL = 0;
    private final int BIAS = 1;
    
    
    // Layers important indexes
    private final int INPUT_LAYER = 0;
    private final int FIRST_HIDDEN_LAYER = 1;
    private int LAST_HIDDEN_LAYER;
    private int OUTPUT_LAYER;
    
    
    // Numbers use to initialize hidden layers 
    private int HIDDEN_LAYERS_COUNT;
    private int HIDDEN_NODES_COUNT;
    
    
    private ArrayList<NeuronLayer> NeuronLayers;
    
    
    // Functions that initialize, or setup logic before running the backpropagation algorithm
    
    
    // Constructor just saves the numbers of the hidden layer and hidden nodes.
    // We do not create the neuron network here because we need to know the number
    // of input neurons and output neurons
    public NeuralNetwork(int hiddenLayersCount, int hiddenNodesCount) {
        this.NeuronLayers = new ArrayList();
        this.HIDDEN_LAYERS_COUNT = hiddenLayersCount;
        this.HIDDEN_NODES_COUNT = hiddenNodesCount;
    }
    
    
    // This function contains the logic to create the neuron network by creating layer,
    // neurons, and connections. It is divided in three parts. The first if statement checks if the 
    // neuron has not been initialized before. This is necesary for n-cross validation since the same
    // instance of teh neuron network is going to be trained with different sections of the training set.
    // The other next part initializes the layers with neurons inside, and the last 
    // section connects the neurons
    private void Initialize_Layers(int inputNodesCount, int outputNodesCount) {
        if (this.NeuronLayers.size() == 0) {
            Create_Layers(inputNodesCount, outputNodesCount);
            Create_Connections();
        }
    }
    
    
    // This function creates one layer of input neurons with one neuron per column in the training set
    // Then it creates layers of hidden nodes with the specified number of hidden neurons from the constructor
    // Finally, it creates one layer of output neurons with one neuron per output class 
    private void Create_Layers(int inputNodesCount, int outputNodesCount) {
        this.NeuronLayers.add(new NeuronLayer(inputNodesCount, NeuronLayer.Type.Input, 0));
        int index = 1;
        for(; index < HIDDEN_LAYERS_COUNT; index++) {
            this.NeuronLayers.add(new NeuronLayer(HIDDEN_NODES_COUNT, NeuronLayer.Type.Hidden, index));
        }
        this.NeuronLayers.add(new NeuronLayer(outputNodesCount, NeuronLayer.Type.Output, index));
        OUTPUT_LAYER = this.NeuronLayers.size() - 1;
        LAST_HIDDEN_LAYER = OUTPUT_LAYER - 1;
    }
    
    
    // This function creates the connections between neurons. It needs to be called after the neurons
    // have been initialized. It is divided in two parts. First, it creates the forward connections, and then it
    // it uses the forward connections  to pull together the backward connections. 
    private void Create_Connections() {
        // init forward connections
        int NEXT = 1;
        for (int layer = INPUT_LAYER; layer <= LAST_HIDDEN_LAYER; layer++) {
            for (int from = 0; from < this.NeuronLayers.get(layer).neurons.size(); from++) {
                Neuron fromNeuron = this.NeuronLayers.get(layer).neurons.get(from);
                for (int to = 0; to < this.NeuronLayers.get(layer + NEXT).neurons.size(); to++) {
                    Neuron toNeuron = this.NeuronLayers.get(layer + NEXT).neurons.get(to);
                    if (toNeuron.getClass() != BiasNeuron.class) {
                        Connection connection = new Connection(fromNeuron, toNeuron);
                        fromNeuron.ForwardConnections.add(connection);
                    }
                }
            }
        }
        
        // init backward connections
        int BEFORE = 1;
        for (int layer = OUTPUT_LAYER; layer >= FIRST_HIDDEN_LAYER; layer--) {
            for (int to = 0; to < this.NeuronLayers.get(layer).neurons.size(); to++) {
                Neuron toNeuron = this.NeuronLayers.get(layer).neurons.get(to);
                for (int from = 0; from < this.NeuronLayers.get(layer - BEFORE).neurons.size(); from++) {
                    Neuron fromNeuron = this.NeuronLayers.get(layer - BEFORE).neurons.get(from);
                    if (to < fromNeuron.ForwardConnections.size()) {
                        Connection connection = fromNeuron.ForwardConnections.get(to);
                        toNeuron.BackwardConnections.add(connection);
                    }
                }
            }
        }   
    }
    
    
    // This function grabs one row from the data set and gives the values of the columns to each 
    // input neuron in the neuron network. This function needs to be called before the feed forward
    // function because feed forward needs to have the new inputs to be classified.
    private void Set_Features(double[] features) {
        ArrayList<Neuron> inputLayer = this.NeuronLayers.get(INPUT_LAYER).neurons;
        for (int index = 0; index < inputLayer.size() - BIAS; index++) {
            Neuron inputNeuron = inputLayer.get(index);
            double feature = features[index];
            inputNeuron.Output = feature;
        }
    }
    
    
    // This function seta a one in the output neuron that is mapped to the actual label.
    // Class labels map an integer(starting from zero) to a string. For example, 0 -> pink, 1 -> orange.
    // Since this neuron network creates one output neuron per output class, the output neuron is mapped to the 
    // output class by index. For example, output neuron at index 0 will give a 1 when the neuron network 
    // classified pink, or it will return a 1 at index 1, if it classified orange.
    // This function cleans all the output neurons by setting a zero in its actual label property.
    // Then it uses the actual label as index to set a one to that output neuron.
    private void Set_Actual_Label(int outputNeuronIndex) {
        for (Neuron neuron : this.NeuronLayers.get(OUTPUT_LAYER).neurons) {
            OutputNeuron outputNeuron = (OutputNeuron)neuron;
            outputNeuron.ActualLabel = 0;
        }
        try {
        ((OutputNeuron)this.NeuronLayers.get(OUTPUT_LAYER).neurons.get(outputNeuronIndex)).ActualLabel = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        }
    
    
    // Train Section
    ErrorManager errorManager;
    int globlaEpochs = 0;
    // This function is called by the MLSystemMananger to train the model
    @Override
    public void train(Matrix features, Matrix labels) {
        Initialize_Layers(features.cols(), labels.m_enum_to_str.get(0).size());
        //if (errorManager == null) {
            errorManager = new ErrorManager();
        //} else {
          //  errorManager.resetBSSF();
        //}
        
        for (int epoch = 0; epoch < MAX_EPOCHS; epoch++)
        {
            globlaEpochs++;
            double trainingError = 0;
            double validationError = 0;
            
            features.shuffle(new Random(), labels);
            
            int threeForths = (int)(3.0 / 4.0 *features.rows());
            Matrix trainFeatures = new Matrix(features, 0, 0, threeForths, features.cols());
            Matrix trainLabels = new Matrix(labels, 0, 0, threeForths, labels.cols());
            Matrix validationFeatures = new Matrix(features, threeForths + 1, 0, features.rows() - threeForths -2, features.cols());
            Matrix validationLabels = new Matrix(labels, threeForths + 1, 0, labels.rows() - threeForths - 2, labels.cols());
            
            for (int row = 0; row < trainFeatures.rows(); row++) {
                int predictedLabel = Feed_Forward(trainFeatures.row(row));
                int actualLabel = (int)trainLabels.get(row, ONLY_COL);
                Error_Backpropagation(actualLabel);
                Update_Weights();
                int localError = actualLabel - predictedLabel;
                trainingError = trainingError + (localError * localError);   
            }
            
            double validationAccuracy = 0;
            for (int row = 0; row < validationFeatures.rows(); row++) {
                int predictedLabel = Feed_Forward(validationFeatures.row(row));
                int actualLabel = (int)validationLabels.get(row, ONLY_COL);
                int localError = actualLabel - predictedLabel;
                validationError = validationError + (localError * localError); 
                if (localError == 0) validationAccuracy++;
            }
            
            double trainingMeanSquareError = trainingError / trainFeatures.rows();
            double validationMeanSquareError = validationError / validationFeatures.rows();
            double validationAccuracyPercentage = validationAccuracy / validationFeatures.rows();
            
            if (errorManager.Is_Overfitting(epoch, trainingMeanSquareError , validationMeanSquareError, validationAccuracyPercentage) == true) {
                break;
            }
        } 
        System.out.println("epochs " + globlaEpochs);
    }

    
    // This function is called by the MLSystemMananger to test the model
    @Override
    public void predict(double[] features, double[] labels) {
        labels[0] = Feed_Forward(features);
    }
    
    
    // This function is ran after feed forward. It looks at all the output neurons and return the index of the
    // neuron with the greates output. It return the index because every output neuron is mapped to a type of the
    // classifing classes. For example, output neuron at index 0 maps to pink, and output neuron at index 1 maps
    // to orange, so if the neuron network after the feed forward get a bigger output at index 0, it classified 
    // the features as a pink class
    private int Get_Prediction() {
        int prediction = 0;
        double largestOutput = 0;
        for (int index = 0; index < this.NeuronLayers.get(OUTPUT_LAYER).neurons.size(); index++) {
            OutputNeuron outputNeuron = (OutputNeuron)this.NeuronLayers.get(OUTPUT_LAYER).neurons.get(index);
            if (outputNeuron.Output > largestOutput) {
                largestOutput = outputNeuron.Output;
                prediction = index;
            }
        }
        return prediction;
    }
    
    
    private int Feed_Forward(double[] features) {
        Set_Features(features);
        for (int layer = FIRST_HIDDEN_LAYER; layer <= OUTPUT_LAYER; layer++) {
            for (Neuron neuron : this.NeuronLayers.get(layer).neurons) {
                neuron.Update_Output_Going_Forward();
            }
        }
        int predictedLabel = Get_Prediction();
        return predictedLabel;
    }
    
    
    public void Error_Backpropagation(double actualLabel) {
        Set_Actual_Label((int)actualLabel);
        for (int layer = OUTPUT_LAYER; layer >= FIRST_HIDDEN_LAYER ; layer--) {
            for (Neuron neuron : this.NeuronLayers.get(layer).neurons) {
                neuron.Update_Error_Going_Backwards();
            }
        }
    }
    
    
    private void Update_Weights() {
        for (int layer = FIRST_HIDDEN_LAYER; layer <= OUTPUT_LAYER; layer++) {
            for (Neuron neuron : this.NeuronLayers.get(layer).neurons) {
                neuron.Update_Weights_Going_Forward();
            }
        }
    }
    
        
}
