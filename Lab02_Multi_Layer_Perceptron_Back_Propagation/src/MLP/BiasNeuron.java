package NN;



public class BiasNeuron extends Neuron {

    
    public BiasNeuron(int LayerIndex, int NeuronIndex) {
        super(LayerIndex, NeuronIndex);
    }
    
    
    @Override
    public void Update_Output_Going_Forward() {
        // When this function get called it does not do anything because this neuron is a Bias
    }
    
    
    @Override
    public void Update_Error_Going_Backwards() { 
        // When this function get called it does not do anything because this neuron is a Bias
    }
    
    
}
