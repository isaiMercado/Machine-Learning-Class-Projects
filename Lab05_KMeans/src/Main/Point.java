/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author isai
 */
public class Point {
 
    
    static double distance(Point point1, Point point2) {
        double sum = 0;
        for (int index = 0; index < KMeans.coordinates_type.size(); index++) {
            Matrix.Type type = KMeans.coordinates_type.get(index);
            Double c1 = point1.coordinates.get(index);
            Double c2 = point2.coordinates.get(index);
            
            if (c1 == Matrix.MISSING || c2 == Matrix.MISSING) {
                sum = sum + 1;
            } else if (type == Matrix.Type.NUMBER) {
                sum = sum + Math.pow(c1 - c2, 2);
            } else if (type == Matrix.Type.NOMINAL) {
                double value = (c1.intValue() == c2.intValue()) ? 0 : 1;
                sum = sum + value;
            }
        }
        
        return sum;
    }
    
    
    public ArrayList<Double> coordinates;
    public int id;
    public Cluster cluster;
    
    
    
    public Point(int dimensions, int id) {
        Random random = new Random();
        coordinates = new ArrayList();
        for (int i = 0; i < dimensions; i++) {
            coordinates.add(random.nextDouble());
        }
        this.id = id;
    }

    
    public Point(int id) {
        coordinates = new ArrayList();
        this.id = id;
    }

    
    public Point(Point point) {
        coordinates = new ArrayList();
        for (Double coordinate : point.coordinates) {
            coordinates.add(coordinate);
        }
        this.id = point.id;
    }
  

    public Point(Point point, Cluster cluster) {
        coordinates = new ArrayList();
        for (Double coordinate : point.coordinates) {
            coordinates.add(coordinate);
        }
        this.id = point.id;
        this.cluster = cluster;
    }

    public double calculate_silhouette(ArrayList<Cluster> clusters) {
        double a = 0.0;
        double b = Double.MAX_VALUE;
        
        for (Cluster cluster : clusters) {
            double current_b = 0.0;
            for (Point point : cluster.points) {
                if (cluster.id == this.cluster.id) {
                    a = a + Point.distance(point, cluster.centroid);
                } else {
                    current_b = current_b + Point.distance(point, cluster.centroid);
                }
            }
            current_b = current_b / cluster.points.size();
            if (current_b < b && current_b != 0.0) {
                b = current_b;
            }
        }
        
        a = a / this.cluster.points.size();
        
        double silhouette = (b - a) / Math.max(b, a);
        return silhouette;
    }
    
    
}
