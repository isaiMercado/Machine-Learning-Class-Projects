package NN;



import java.util.ArrayList;


public class Neuron {
    
    
    private double LEARNING_RATE = 0.1; //0.1; Iris Dataset
    private double MOMENTUM_RATE = 0.6; //0.9; Iris Dataset

    
    public int LayerIndex;
    public int NeuronIndex;
    
    
    public double Output;
    public double Error;
    public ArrayList<Connection> ForwardConnections;
    public ArrayList<Connection> BackwardConnections;

   
    public Neuron(int LayerIndex, int NeuronIndex) {
        this.LayerIndex = LayerIndex;
        this.NeuronIndex = NeuronIndex;
        this.Output = 1;
        this.Error = 1;
        this.ForwardConnections = new ArrayList();
        this.BackwardConnections = new ArrayList();
    }
    
    
    public void Update_Output_Going_Forward() {
        double totalNet = NeuronEquations.Total_Net(this.BackwardConnections);
        this.Output = NeuronEquations.Activation_Function(totalNet);
    }
    
    
    public void Update_Error_Going_Backwards() {
        throw new UnsupportedOperationException("You have to overwrite this method"); 
    }

    
    void Update_Weights_Going_Forward() {
        for (Connection connection : this.BackwardConnections) {
            double momentumForce = MOMENTUM_RATE * connection.PreviousDeltaWeight;
            double priorLearning = connection.FromNode.Output;
            double newLearning = this.Error;
            
            double deltaWeight = LEARNING_RATE * priorLearning * newLearning;
            connection.PreviousDeltaWeight = deltaWeight;
            
            connection.Weight = connection.Weight - (deltaWeight + momentumForce);  
        }
    }
    
    
}
