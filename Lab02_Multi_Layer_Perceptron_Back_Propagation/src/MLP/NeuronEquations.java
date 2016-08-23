package NN;



import java.util.ArrayList;


public class NeuronEquations {

    
    public static double Total_Net(ArrayList<Connection> backwardConnections) {
        double sum = 0;
        for (Connection connection : backwardConnections) {
            sum = sum + connection.FromNode.Output * connection.Weight;
        }
        return sum;
    }
    
    
    public static double Activation_Function(double totalNet) {
        double result = Sigmoid(totalNet);
        return result;
    }
    
    
    public static double Activation_Function_Derivative(double nodeOutput) {
        double result = Sigmoid_Derivative(nodeOutput);
        return result;
    }
    
    
    public static double Cost_Function(double actualLabel, double predictedLabel) {
        double result = Sum_Squares(actualLabel, predictedLabel);
        return result;
    }
    
    
    public static double Cost_Fuction_Derivative(double actualLabel, double predictedLabel) {
        double result = Sum_Squares_Derivative(actualLabel, predictedLabel);
        return result;
    }

    
    // Private Functions
    
    
    private static double Sum_Squares(double actualLabel, double predictedLabel) {
        double difference = (actualLabel - predictedLabel);
        double result = 0.5 * Math.pow(difference, 2);
        return result;
    }
    
    
    private static double Sum_Squares_Derivative(double actualLabel, double predictedLabel) {
        double result = -(actualLabel - predictedLabel);
        return result;
    }
    
    
    private static double Sigmoid_Derivative(double nodeOutput) {
        //double output = this.Sigmoid(totalNet) * (1 - this.Sigmoid(totalNet)); 
        double result = nodeOutput * (1 - nodeOutput);
        return result;
    }
    
    
    private static double Sigmoid(double totalNet) {
        double result = 1.0 / (1 + Math.pow(Math.E, -totalNet));
        return result;
    }
    
    
}
