package Main;

// ----------------------------------------------------------------
// The contents of this file are distributed under the CC0 license.
// See http://creativecommons.org/publicdomain/zero/1.0/
// ----------------------------------------------------------------

import Main.KMeans;
import Main.Matrix;
import java.util.Random;


public class MLSystemManager {

	public void run(String[] args) throws Exception {
            
            String fileName = "data/abalone.arff";
            int K = 5;
            boolean normalize = true;
            
            // Load the model
            KMeans kmeans = new KMeans();

            // Load the ARFF file
            Matrix data = new Matrix();
            data.loadArff(fileName);
            if (normalize)
            {
                    System.out.println("Using normalized data\n");
                    data.normalize();
            }

            // Print some stats
            System.out.println();
            System.out.println("Dataset name: " + fileName);
            System.out.println("Number of instances: " + data.rows());
            System.out.println("Number of attributes: " + data.cols());
            System.out.println();

            kmeans.cluster(data, K);
	}

	/**
	 * Class for parsing out the command line arguments
	 */
	private class ArgParser {
	
		String arff;
		String learner;
		String evaluation;
		String evalExtra;
		boolean verbose;
		boolean normalize;

		//You can add more options for specific learning models if you wish
		public ArgParser(String[] argv) {
			try{
	
			 	for (int i = 0; i < argv.length; i++) {

			 		if (argv[i].equals("-V"))
			 		{
			 			verbose = true;
			 		}
			 		else if (argv[i].equals("-N"))
			 		{
			 			normalize = true;
			 		}
						else if (argv[i].equals("-A"))
						{
							arff = argv[++i];
						}
						else if (argv[i].equals("-L"))
						{
							learner = argv[++i];
						}
						else if (argv[i].equals("-E"))
						{
							evaluation = argv[++i];
							if (argv[i].equals("static"))
							{
								//expecting a test set name
								evalExtra = argv[++i];
							}
							else if (argv[i].equals("random"))
							{
								//expecting a double representing the percentage for testing
								//Note stratification is NOT done
								evalExtra = argv[++i];
							}
							else if (argv[i].equals("cross"))
							{
								//expecting the number of folds
								evalExtra = argv[++i];
							}
							else if (!argv[i].equals("training"))
							{
								System.out.println("Invalid Evaluation Method: " + argv[i]);
								System.exit(0);
							}
						}
						else
						{
							System.out.println("Invalid parameter: " + argv[i]);
							System.exit(0);
						}
			  	}
		 
				}
				catch (Exception e) {
					System.out.println("Usage:");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E [evaluationMethod] {[extraParamters]} [OPTIONS]\n");
					System.out.println("OPTIONS:");
					System.out.println("-V Print the confusion matrix and learner accuracy on individual class values\n");
					
					System.out.println("Possible evaluation methods are:");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E training");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E static [testARFF_File]");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E random [%_ForTraining]");
				  	System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E cross [numOfFolds]\n");
					System.exit(0);
				}
				
				if (arff == null || learner == null || evaluation == null)
				{
					System.out.println("Usage:");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E [evaluationMethod] {[extraParamters]} [OPTIONS]\n");
					System.out.println("OPTIONS:");
					System.out.println("-V Print the confusion matrix and learner accuracy on individual class values");
					System.out.println("-N Use normalized data");
					System.out.println();
					System.out.println("Possible evaluation methods are:");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E training");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E static [testARFF_File]");
					System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E random [%_ForTraining]");
				  	System.out.println("MLSystemManager -L [learningAlgorithm] -A [ARFF_File] -E cross [numOfFolds]\n");
					System.exit(0);
				}
			}
	 
		//The getter methods
		public String getARFF(){ return arff; }	
		public String getLearner(){ return learner; }	 
		public String getEvaluation(){ return evaluation; }	
		public String getEvalParameter() { return evalExtra; }
		public boolean getVerbose() { return verbose; } 
		public boolean getNormalize() { return normalize; }
	}

	public static void main(String[] args) throws Exception
	{
		MLSystemManager ml = new MLSystemManager();
		ml.run(args);
	}
}
