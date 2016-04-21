/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isai
 */
public class DecisionTree extends SupervisedLearner {
    
    
    private Node root;
    private DataFrame training_set;
    private DataFrame validation_set;
    private int nodes_count;
  

    @Override
    public void train(Matrix features, Matrix labels) throws Exception {
        int threeForths = (int)(3.0 / 4.0 *features.rows());
        Matrix trainFeatures = new Matrix(features, 0, 0, threeForths, features.cols());
        Matrix trainLabels = new Matrix(labels, 0, 0, threeForths, labels.cols());
        Matrix validationFeatures = new Matrix(features, threeForths + 1, 0, features.rows() - threeForths -2, features.cols());
        Matrix validationLabels = new Matrix(labels, threeForths + 1, 0, labels.rows() - threeForths - 2, labels.cols());
        training_set = new DataFrame(trainFeatures, trainLabels);
        validation_set = new DataFrame(validationFeatures, validationLabels);
        
        
        root = new Node(null, null, training_set);
        create_tree(root);
    }

    @Override
    public void predict(double[] features, double[] labels) throws Exception {
        labels[0] = traverse_tree(features);
    }

  
    private double traverse_tree(double[] features) {
        int next_class_label = 0;
        Node current_node = root;
        while(current_node.children_nodes.size() != 0) {
            int absolute_index = root.dataframe.features.m_attr_name.indexOf(current_node.attribute.name);
            double current_feature = features[absolute_index];
            Node next_node = find_next_node(current_node, current_feature);
            next_class_label = next_node.get_majority_class_label();
            current_node = next_node;
        }
        return (double)next_class_label;
    }

    private Node find_next_node(Node current_node, double current_feature) {
        Node next_node = null;
        for (Node node : current_node.children_nodes) {
            if (node.label.value == current_feature) {
                next_node = node;
                break;
            }
        }
        return next_node;
    }

    
    // Recursive function
    private void create_tree(Node current_node) {
        nodes_count++;
        if (current_node.meets_stopping_criteria() == false) {
            current_node.create_children_nodes();
            for (Node next_node : current_node.children_nodes) {
                create_tree(next_node);
            }
        }
    }

    
}
