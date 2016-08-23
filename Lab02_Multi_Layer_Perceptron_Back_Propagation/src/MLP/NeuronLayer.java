package NN;

import java.util.ArrayList;


public class NeuronLayer {

    
    public enum Type {
        Input,
        Hidden,
        Output,
    }
    
    
    public ArrayList<Neuron> neurons;

    
    public NeuronLayer(int nodesCount, NeuronLayer.Type type, int layerIndex) {
        this.neurons = new ArrayList();
        if (type == NeuronLayer.Type.Input) {
            for (int index = 0; index < nodesCount; index++) {
                this.neurons.add(new InputNeuron(layerIndex, index));
            }
            this.neurons.add(new BiasNeuron(layerIndex,-1));
        } else if (type == NeuronLayer.Type.Hidden) {
            for (int index = 0; index < nodesCount; index++) {
                this.neurons.add(new HiddenNeuron(layerIndex, index));
            }
            this.neurons.add(new BiasNeuron(layerIndex,-1));
        } else if (type == NeuronLayer.Type.Output) {
            for (int index = 0; index < nodesCount; index++) {
                this.neurons.add(new OutputNeuron(layerIndex, index));
            }
        }
       
        
    }


}
