/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author isai
 */
public class Cluster {
    
    
    public ArrayList<Point> points;
    public Point centroid;
    public int id;

    
    Cluster(Point point, int id) {
        points = new ArrayList();
        centroid = point;
        this.id = id;
    }

    
    void clear() {
        points.clear();
    }

    
    void calculate_new_centroid() {
        ArrayList<Double> new_coordinates = new ArrayList();
        for (int index = 0; index < centroid.coordinates.size(); index++) {
            Matrix.Type type = KMeans.coordinates_type.get(index);
            if (type == Matrix.Type.NUMBER) {
                double mean = calculate_mean(index);
                new_coordinates.add(mean);
            } else if (type == Matrix.Type.NOMINAL) {
                double most_common = calculate_most_common(index);
                new_coordinates.add(most_common);
            }
        }
        centroid.coordinates = new_coordinates;
    }
    
    
    double calculate_mean(int index) {
        double coordinate_sum = 0;
        int instances_counter = 0;
        for (Point point : points) {
            double value = point.coordinates.get(index);
            if (value != Matrix.MISSING) {
                coordinate_sum = coordinate_sum + value;
                instances_counter++;
            }
        }
        
        double mean = 0;
        if (instances_counter == 0) {
            mean = Matrix.MISSING;
        } else {
            mean = coordinate_sum / instances_counter;
        }
        
        return mean;
    }

    private double calculate_most_common(int index) {
        HashMap<Double, Integer> counter = new HashMap();
        for (Point point : points) {
            double value = point.coordinates.get(index);
            if (value != Matrix.MISSING) {
                if (counter.containsKey(value) == false) {
                    counter.put(value, 0);
                } else {
                    int counts = counter.get(value) + 1;
                    counter.put(value, counts);
                }
            }
        }
        
        double most_common = 0;
        if (counter.size() != 0) {
            int most_common_count = 0;
            for (Entry<Double, Integer> entry : counter.entrySet()) {
                if (most_common_count < entry.getValue()) {
                    most_common = entry.getKey();
                    most_common_count = entry.getValue();
                }
            }
        } else {
            most_common = Matrix.MISSING;
        }
        
        return most_common;
    }

    public double calculate_SSE() {
        double SSE = 0;
        for (Point point : points) {
            SSE = SSE + Point.distance(point, centroid);
        }
        return SSE;
    }
    
    
}
