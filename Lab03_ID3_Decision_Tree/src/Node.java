
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isai
 */
public class Node {
    
    public Attribute attribute;
    public Label label;
    public DataFrame dataframe;
    
    public Node parent_node;
    public ArrayList<Node> children_nodes;
    
    public boolean overfits;
    
    public Node(Node parent_node, Label label, DataFrame dataframe) {
        this.attribute = null;
        this.label = label;
        this.dataframe = dataframe;
        this.parent_node = parent_node;
        this.children_nodes = new ArrayList<Node>();
    }

    public boolean meets_stopping_criteria() {
        // checking if node is a leaf
        if (are_class_labels_pure() == true) {
            return true;
        }
        
        if (dataframe.features.m_data.size() <= 50) {
            return true;
        }
        
        return false;
    }

    public Attribute calculate_best_attribute() {
        double least_attr_info = Double.MAX_VALUE;
        Attribute best_attribute = null;
        for (Attribute attribute : dataframe.get_attributes()) {
            double attr_info = calculate_remaining_information(attribute);
            if (attr_info < least_attr_info) {
                least_attr_info = attr_info;
                best_attribute = attribute;
            }
        }
        
        if (best_attribute == null) {
            int debug = 0;
        }
        
        return best_attribute;
    }
    
    
    private double calculate_remaining_information(Attribute attribute) {
        double remaining_information = 0;
        for (Label label : dataframe.get_attribute_labels(attribute)) {
            double label_count = dataframe.count_label(attribute, label);
            double label_total = dataframe.features.rows();
            double label_proportion = label_count / label_total;
            DataFrame subset_dataframe = dataframe.get_subset(attribute, label);
            double label_information = calculate_label_information(subset_dataframe);
            remaining_information = remaining_information + (label_proportion * label_information);
        }
        
        if(remaining_information == 0) {
            int debug = 0;
        }
        return remaining_information;
    }
    
    
    private double calculate_label_information(DataFrame subset_dataframe) {
        double information = 0;
        for (Label class_label : subset_dataframe.get_class_labels()) {
            double class_label_count = subset_dataframe.count_class_label(class_label);
            double class_label_total = subset_dataframe.labels.rows();
            double class_label_proportion = class_label_count / class_label_total;
            double log2_proportion = (class_label_proportion == 0) ? 0 : log2(class_label_proportion);
            information = information + (-class_label_proportion * log2_proportion);
        }
        return information;
    }
    
    
    public double log2(double num)
    {
        return (Math.log(num)/Math.log(2));
    }
    

    public void create_children_nodes() {
        attribute = calculate_best_attribute();
        for (Label label : dataframe.get_attribute_labels(attribute)) {
            DataFrame subset_dataframe = dataframe.get_subset(attribute, label);
            children_nodes.add(new Node(this, label, subset_dataframe));
        }
    }

    int get_majority_class_label() {
        HashMap<Double, Integer> counter = new HashMap<Double, Integer>();
        for (Label class_label : dataframe.get_class_labels()) {
            counter.put((double)class_label.value, 0);
        }
        
        for (double[] class_label : dataframe.labels.m_data) {
            int count = counter.get(class_label[0]);
            counter.put(class_label[0], count + 1);
        }
        
        int majority_class_label = 0;
        for (Entry<Double, Integer> entry : counter.entrySet()) {
            if (entry.getValue() > majority_class_label) {
                majority_class_label = entry.getKey().intValue();
            }
        }
        
        return majority_class_label;
    }

    private boolean are_class_labels_pure() {
        double first_class_value = dataframe.labels.m_data.get(0)[0];
        for(double[] class_value : dataframe.labels.m_data) {
            if (class_value[0] != first_class_value) {
                return false;
            }
        }
        return true;
    }

    
}
