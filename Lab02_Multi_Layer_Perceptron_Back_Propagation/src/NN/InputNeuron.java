package NN;



public class InputNeuron extends Neuron {

    
    public InputNeuron(int LayerIndex, int NeuronIndex) {
        super(LayerIndex, NeuronIndex);
    }

    
    // This function should never be called because input neurons do not have backward connections,
    // so just to make sure that it is not called, it will throw an exception.
    @Override
    public void Update_Error_Going_Backwards() {
        throw new UnsupportedOperationException("There is no Input Neuron Error"); 
    }

    
}
