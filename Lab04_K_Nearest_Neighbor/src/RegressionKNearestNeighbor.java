
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author isai
 */
public class RegressionKNearestNeighbor extends SupervisedLearner {

    private int SAMPLE_SIZE = 1000;
    
    private int K = 3;
    private boolean weighted;
    
    private ArrayList<KnnPoint> dataframe;
    
    
    public RegressionKNearestNeighbor(boolean weighted, int K) {
        this.K = K;
        this.weighted = weighted;
    }
    
    
    @Override
    public void train(Matrix features, Matrix labels) throws Exception {
        dataframe = new ArrayList();
        features.shuffle(new Random(), labels);
        for(int i = 0; i < SAMPLE_SIZE; i++) {
            if (i < features.rows()) {
                KnnPoint point = new KnnPoint();
                point.features = features.row(i);
                point.label_class = labels.row(i)[0];
                point.weight = 1;
                point.distance = i;
                dataframe.add(point);
            }
        }
        
            int dd =0 ;
    }

   
    @Override
    public void predict(double[] features, double[] labels) throws Exception {
        
        dataframe = calculate_distances(features, dataframe);
        
        ArrayList<KnnPoint> k_best_points = pick_k_best_points(dataframe, K);
        
        double regression_f_of_x = calculate_regression(k_best_points);
        
        labels[0] = regression_f_of_x;
    }
    
    
    private double euclidean_distance(double[] unknown_point, double[] known_point) {
        double sum = 0.0;
        for(int i = 0 ; i < known_point.length; i++) {
            sum = sum + Math.pow(unknown_point[i] - known_point[i], 2);
        }
        double distance = Math.sqrt(sum);
        return distance;
    }

    private ArrayList<KnnPoint> calculate_distances(double[] features, ArrayList<KnnPoint> dataframe) {
        for(KnnPoint point : dataframe) {
            point.distance = euclidean_distance(features, point.features);
        }
        return dataframe;
    }

    private ArrayList<KnnPoint> pick_k_best_points(ArrayList<KnnPoint> dataframe, int K) {
        Collections.sort(dataframe); 
        ArrayList<KnnPoint> k_best_points = new ArrayList();
        for (int i = 0; i < K; i++) {
            k_best_points.add(dataframe.get(i));
        }
        return k_best_points;
    }

    private double calculate_regression(ArrayList<KnnPoint> k_best_points) {
        
        double numerator = 0.0;
        for(KnnPoint point : k_best_points) {
            double f_of_x = point.label_class;
            double weight = (this.weighted == true)? 1 / Math.pow(point.distance, 2) : 1;
            numerator = numerator + (weight * f_of_x);
        }
        
        double denominator = 0.0;
        for(KnnPoint point : k_best_points) {
            double weight = 1 / Math.pow(point.distance, 2);
            denominator = denominator + weight;
        }
        
        double regression = numerator / denominator;
        
        return regression;
    }

    private double pick_best_class(HashMap<Double, Double> classes_weights) {
        double majority_class_label = 0.0;
        for (Entry<Double, Double> entry : classes_weights.entrySet()) {
            if (entry.getValue() > majority_class_label) {
                majority_class_label = entry.getKey();
            }
        }
        return majority_class_label;
    }
    
}
