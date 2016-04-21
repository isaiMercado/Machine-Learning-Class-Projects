package NN;

public class Connection {

    
    public Neuron FromNode;    
    public Neuron ToNode; 
    
    
    public double Weight;  
    public double PreviousDeltaWeight; // This is used by the momentum 

   
    public Connection(Neuron from, Neuron to) {
        this.FromNode = from;
        this.ToNode = to;
        this.Weight = Math.random();
        this.PreviousDeltaWeight = 0.0;
    }

    
}