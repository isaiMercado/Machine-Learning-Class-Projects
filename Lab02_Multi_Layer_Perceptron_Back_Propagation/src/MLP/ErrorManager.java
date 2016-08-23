package NN;



import java.text.DecimalFormat;


public class ErrorManager {
   
    
    private final boolean DEBUG = true; 
    private final int OVERFITTING_THREASHOLD = 8; 
    private final double ERROR_MARGIN = 0.01;
 
    
    private double ValidationError_BSSF = Double.POSITIVE_INFINITY;
    private int OverfittingCounter;
    
    
    public boolean Is_Overfitting(int epoch, double trainingError, double validationError, double validationAccuracy) {
        
        DecimalFormat df = new DecimalFormat("#.####");
        if (DEBUG) System.out.println("epoch\t" + epoch + 
                                      "\ttrainingError\t" + df.format(trainingError) + 
                                      "\tvalidationError\t" + df.format(validationError) +
                                      "\tbestValidationError\t" + df.format(this.ValidationError_BSSF) + 
                                      "\tvalidationAccuracy\t" + df.format(validationAccuracy));
        
        if (validationError < ValidationError_BSSF) {
            ValidationError_BSSF = validationError;
        }
        
        boolean itIsOverfitting = trainingError <= (validationError + ERROR_MARGIN);
        boolean accuracyIsDecreasing = (validationError + ERROR_MARGIN) >= this.ValidationError_BSSF;
        
        if (itIsOverfitting && accuracyIsDecreasing) {
            this.OverfittingCounter = this.OverfittingCounter + 1;
        } else {
            this.OverfittingCounter = 0;
        }
        
        if (this.OverfittingCounter > OVERFITTING_THREASHOLD) {
            return true;
        }
        
        return false;
        
    }
            
        
}
