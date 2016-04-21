
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
public class KNearestNeighbor extends SupervisedLearner {

    private int SAMPLE_SIZE = 1100;
    
    private int K;
    private boolean weighted;
    
    private ArrayList<KnnPoint> dataframe;
    
    
    public KNearestNeighbor(boolean weighted, int K) {
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
        
    }

   
    @Override
    public void predict(double[] features, double[] labels) throws Exception {
        
        dataframe = calculate_distances(features, dataframe);
        
        ArrayList<KnnPoint> k_best_points = pick_k_best_points(dataframe, K);
        
        HashMap<Double, Double> classes_weights = calculate_classes_weights(k_best_points);
        
        double best_class = pick_best_class(classes_weights);
        
        labels[0] = best_class;
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

    private HashMap<Double, Double> calculate_classes_weights(ArrayList<KnnPoint> k_best_points) {
        HashMap<Double, Double> classes_weights = new HashMap();
        
        for(KnnPoint point : k_best_points) {
            classes_weights.put(point.label_class, 0.0);
        }
        
        for(KnnPoint point : k_best_points) {
            double old_weight = classes_weights.get(point.label_class);
            double current_weight = (this.weighted == true)? 1 / Math.pow(point.distance, 2) : 1;
            double new_weight = old_weight + current_weight;
            classes_weights.put(point.label_class, new_weight);
        }
        return classes_weights;
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
