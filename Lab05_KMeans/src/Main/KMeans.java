/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.ArrayList;

/**
 *
 * @author isai
 */
public class KMeans {
    
    
    public static ArrayList<Matrix.Type> coordinates_type; 
    
    public int K;  
    public int iteration;
    public ArrayList<Point> points;
    public ArrayList<Cluster> clusters;

    
    public void cluster(Matrix data, int K) {
        coordinates_type = data.m_attr_type;
        init_points(data);
        init_clusters(K, data.cols());
        calculate_clusters();
    }
    
    private void init_points(Matrix data) {
        points = new ArrayList();
        for (int row = 0; row < data.rows(); row++) {
            Point point = new Point(row);
            for (int col = 0; col < data.cols(); col++) {
                point.coordinates.add(data.get(row, col));
            }
            points.add(point);
        }
    }

    private void init_clusters(int K, int dimensions) {
        clusters = new ArrayList();
        for (int i = 0; i < K; i++) {
            clusters.add(new Cluster(new Point(dimensions, -1), i));
        }
    }
    
    private void init_clusters(int K) {
        clusters = new ArrayList();
        for (int i = 0; i < K; i++) {
            clusters.add(new Cluster(new Point(points.get(i)), i));
        }
    }

    private void calculate_clusters() {
        boolean finish = false;
        while(finish == false) {
            iteration = iteration + 1;
            clear_clusters();
            ArrayList last_centroids = get_centroids();
            assign_clusters();
            calculate_new_centroids();
            ArrayList current_centroids = get_centroids();
            print_clusters();
            calculate_SSE();
            calculate_silhouette(); 
            double change = centroids_change(last_centroids, current_centroids);
            if (ImprovementChecker.improved(change) == false) {
                finish = true;
            }
        }
    }

    private void clear_clusters() {
        for (Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private ArrayList get_centroids() {
        ArrayList<Point> centroids = new ArrayList();
        for (Cluster cluster : clusters) {
            centroids.add(new Point(cluster.centroid));
        }
        return centroids;
    }

    private void assign_clusters() {
        for (Point point : points) {
            double min_distance = Double.MAX_VALUE;
            Cluster min_cluster = null;
            for (Cluster cluster : clusters) {
                double distance = Point.distance(cluster.centroid, point); 
                if (distance < min_distance) {
                    min_distance = distance;
                    min_cluster = cluster;
                }
            }
            min_cluster.points.add(new Point(point, min_cluster));
        }
    }

    private void calculate_new_centroids() {
        for (Cluster cluster : clusters) {
            cluster.calculate_new_centroid();
        }
    }

    private double centroids_change(ArrayList<Point> last_centroids, ArrayList<Point> current_centroids) {
        double change = 0;
        for (int p = 0; p < last_centroids.size(); p++) {
            change = change + Point.distance(last_centroids.get(p), current_centroids.get(p));
        }
        System.out.println("change " + change);
        return change;
    }

    private void print_clusters() {
        String output = "Iteration: " + iteration;
        for (Cluster cluster : clusters) {
            output = output + "\nCluster " + cluster.id + "\n";
            for (Point point : cluster.points) {
                output = output + "point " + point.id + "\n";
            }
        }
        System.out.print(output);
    }

    private void calculate_SSE() {
        double total_SSE = 0.0;
        for (Cluster cluster : clusters) {
            total_SSE = total_SSE + cluster.calculate_SSE();
        }
        System.out.println("Total SSE: " + total_SSE);
    }

    private void calculate_silhouette() {
        double total_silhouette = 0.0;
        for (Cluster cluster : clusters) {
            for (Point point : cluster.points) {
                total_silhouette = total_silhouette + point.calculate_silhouette(clusters);
            }
        }
        System.out.println("Total Silhouette: " + total_silhouette);
    }
    
    

    
}
