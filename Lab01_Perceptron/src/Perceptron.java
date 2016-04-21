/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Random;

/**
 *
 * @author isai
 */
public class Perceptron extends SupervisedLearner {
    
    private static final int MAX_EPOCHS = 100;
    private static final int MIN_SIGNIFICANT_CHANGE = 0;
    private static final int MIN_SIGNIFICANT_CHANGE_TIMES = 3;
    
    private static final int ONLY_COL = 0;
    
    private static final int COLUMN_TO_TEST = -1; //3,9,10
    private static final Boolean DEBUG = true;
    
    private static final double LEARNING_RATE = 0.1;
    private static final double THRESHOLD = 0.0;
    
    private static final int ACTIVATION_SIGNAL = 1;
    private static final int NO_SIGNAL = 0;
    
    private double[] weights;
    private int lastTotalError;
    private int noImprovementCounter;
    
    /**
     * features is the Data inside the DataFrame
     * labels is the target. It is usually the last column in the DataFrame
     */
    @Override
    public void train(Matrix features, Matrix labels) throws Exception {
        lastTotalError = Integer.MAX_VALUE;
        noImprovementCounter = 0;
    
        // init random weights
        this.weights = initWeights(features.cols());
        
        for (int epoch = 0; epoch < MAX_EPOCHS; epoch++)
        {
            int totalError = 0;
            
            //loop through all instances to make line converge
            for (int rowIndex = 0; rowIndex < features.rows(); rowIndex++) {
                
                // Predict a label and then correct the weights according to the error direction
                int predictedLabel = getPrediction(features.row(rowIndex));
                int actualLabel = (int)labels.get(rowIndex, ONLY_COL);
                int localError = actualLabel - predictedLabel;
                weights = recalculateWeights(features.row(rowIndex), localError);
                
                // add the error to see if we have less than the max error
                totalError = totalError + (localError * localError);
            }
            
            if (DEBUG) System.out.println("epoch: " + epoch + ", totalError: " + totalError);
            
            // if we have less error than the max error threshold, we are trained to predict
            if (isThereSignificantChange(totalError) == false) {
                if (DEBUG) System.out.println("Perceptron is trained");
                if (DEBUG) System.out.println("Equation: " + lineEquationToString());
                break;
            }
        } 
        
    }

    
    @Override
    public void predict(double[] features, double[] labels) throws Exception {
        labels[0] = getPrediction(features);
    }
    
    
    private Boolean isThereSignificantChange(int totalError) {
        int improvementChange = Math.abs(lastTotalError - totalError);
        if (improvementChange <= MIN_SIGNIFICANT_CHANGE) {
            noImprovementCounter++;
        }
        lastTotalError = totalError;
        if (noImprovementCounter >= MIN_SIGNIFICANT_CHANGE_TIMES) {
            return false;
        }
        return true;
    }
    
    
    private int getPrediction(double[] features) {
        double sum = 0;
        
        for (int colIndex = 0; colIndex < features.length; colIndex++) {
            if (colIndex != COLUMN_TO_TEST) {
                double feature = features[colIndex];
                double weight = weights[colIndex];
                sum = sum + (feature * weight);
            }
        }
        
        double bias = weights[weights.length - 1];
        sum = sum + bias;
        
        int predictedLabel = (sum > THRESHOLD) ? ACTIVATION_SIGNAL : NO_SIGNAL;
        return predictedLabel;
    }


    private double[] initWeights(int totalColumns) {
        Random random = new Random();
        double[] newWeights = new double[totalColumns + 1];// one weight per property and one for bias
        
        for (int index = 0; index < newWeights.length; index++) {
            double randomDouble = random.nextDouble();
            newWeights[index] = randomDouble;
        }
        
        return newWeights;
    }
    
    
    public double[] recalculateWeights(double[] features, double localError) {
        double[] newWeights = new double[weights.length];
        
        for (int colIndex = 0; colIndex < features.length; colIndex++) {
            double feature = features[colIndex];
            double weight = weights[colIndex];
            newWeights[colIndex] = weight + (feature * LEARNING_RATE * localError);
        }
        
        double bias = weights[weights.length - 1];
        newWeights[weights.length - 1] = bias + (LEARNING_RATE * localError);
        
        return newWeights;
    }
    
    
    private String weightsToString() {
        String output = "";
        for (double weight : weights) {
            output = output + "   " + String.format("%.4f", weight);
        }
        return output;
    }
    
    private String lineEquationToString() {
        String equation = "";
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        for (int index = 0; index < weights.length - 1; index++) {
            String weight = String.format("%.4f", Math.abs(weights[index]));
            String sign = (weights[index] < 0) ? " -" : " +";
            char variable = abc.charAt(index);
            equation = equation + sign + weight + variable;
        }
        
        String bias = String.format("%.4f", Math.abs(weights[weights.length - 1]));
        String sign = (weights[weights.length - 1] < 0) ? " -" : " +";
        equation = equation + sign + bias + " = 0";
        return equation;
    }
}
