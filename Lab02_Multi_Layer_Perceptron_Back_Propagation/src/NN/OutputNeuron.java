package NN;



public class OutputNeuron extends Neuron {

    
    public double ActualLabel;

    
    public OutputNeuron(int LayerIndex, int NeuronIndex) {
        super(LayerIndex, NeuronIndex);
    }
    
    
    @Override
    public void Update_Error_Going_Backwards() {
        double magnitudeAndDirectionTowardErrorMinima = NeuronEquations.Cost_Fuction_Derivative(this.ActualLabel, this.Output);
        double normalizer = NeuronEquations.Activation_Function_Derivative(Output);
        this.Error = magnitudeAndDirectionTowardErrorMinima * normalizer; 
    }
    
    
}
