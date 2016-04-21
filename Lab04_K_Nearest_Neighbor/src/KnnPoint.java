/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isai
 */
public class KnnPoint implements Comparable<KnnPoint> {
    
    public double[] features;
    public double label_class;
    
    public double weight;
    public double distance;
    
    @Override
    public int compareTo(KnnPoint o) {
        if (this.distance < o.distance) {
            return -1;
        } 
        if (this.distance > o.distance) {
            return 1;
        }
        return 0;
    }
    
}
