package NN;



public class HiddenNeuron extends Neuron {

    
    public HiddenNeuron(int LayerIndex, int NeuronIndex) {
        super(LayerIndex, NeuronIndex);
    }

    
    @Override
    public void Update_Error_Going_Backwards() {
        double resultingErrorVote = 0;
        for (Connection connection : this.ForwardConnections) {
            double magnitudeAndDirectionTowardErrorMinima = connection.ToNode.Error;
            double proportionOfError = connection.Weight;
            resultingErrorVote = resultingErrorVote + (magnitudeAndDirectionTowardErrorMinima * proportionOfError); 
        }
        double normalizer = NeuronEquations.Activation_Function_Derivative(Output);
        this.Error = resultingErrorVote * normalizer;
    }
   
    
}
