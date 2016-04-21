
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isai
 */
public class DataFrame {
    
    
    public Matrix features;
    public Matrix labels;
    public double entropy;
    
    
    public DataFrame(Matrix features, Matrix labels) {
        this.features = features;
        this.labels = labels;
    }

    
    public String get_attribute_name(int index) {
        String name = features.m_attr_name.get(index);
        return name;
    }
    
    
    public ArrayList<Attribute> get_attributes() {
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        for (int attribute_index = 0; attribute_index < features.m_attr_name.size(); attribute_index++) {
            String attribute_name = get_attribute_name(attribute_index);
            attributes.add(new Attribute(attribute_name, attribute_index));
        }
        return attributes;
    }
   
    
    public ArrayList<Label> get_attribute_labels(Attribute attribute) {
        ArrayList<Label> labels = new ArrayList<Label>();
        try{
            for (Map.Entry<Integer, String> entry : this.features.m_enum_to_str.get(attribute.index).entrySet()) {
                labels.add(new Label(entry.getValue(), entry.getKey()));
            }
        } catch(Exception e) {
            int debug = 0;
        }
        return labels;
    }
    
    public ArrayList<Label> get_class_labels() {
        ArrayList<Label> labels = new ArrayList<Label>();
        for (Map.Entry<Integer, String> entry : this.labels.m_enum_to_str.get(0).entrySet()) {
            labels.add(new Label(entry.getValue(), entry.getKey()));
        }
        return labels;
    }
    
    
    // Get subset functions
    
    
    public DataFrame get_subset(Attribute attribute, Label label) {
        Matrix features_subset = new Matrix();
        features_subset.m_attr_name = get_features_attr_name_subset(attribute, label);
        features_subset.m_data = get_features_m_data_subset(attribute, label);
        features_subset.m_enum_to_str = get_features_m_enum_to_str_subset(attribute, label);
        features_subset.m_str_to_enum = get_features_m_str_to_enum_subset(attribute, label);
        
        Matrix labels_subset = new Matrix();
        labels_subset.m_attr_name = get_labels_attr_name_subset(attribute, label);
        labels_subset.m_data = get_labels_m_data_subset(attribute, label);
        labels_subset.m_enum_to_str = get_labels_m_enum_to_str_subset(attribute, label);
        labels_subset.m_str_to_enum = get_flabels_m_str_to_enum_subset(attribute, label);
  
        DataFrame subset = new DataFrame(features_subset, labels_subset);
        return subset;
    }
    

    public String get_attribute_label(int attribute_index, int label_index) {
        TreeMap<Integer, String> mapper = this.features.m_enum_to_str.get(attribute_index);
        String label_name = mapper.get(label_index);
        return label_name;
    }

    
    private ArrayList<String> get_features_attr_name_subset(Attribute attribute, Label label) {
        ArrayList<String> m_attr_name_subset = new ArrayList<String>();
        for(int i = 0; i < features.m_attr_name.size(); i++) {
            if (i != attribute.index) {
                m_attr_name_subset.add(features.m_attr_name.get(i));
            }
        }
        return m_attr_name_subset;
    }
    
 
    private ArrayList<double[]> get_features_m_data_subset(Attribute attribute, Label label) {
        ArrayList<double[]> m_data_subset = new ArrayList<double[]>();
        for (int i = 0; i < features.rows(); i++) {
            double[] features_row_original = features.row(i);
            if (features_row_original[attribute.index] == label.value) {
                int j = 0;
                int k = 0;
                double[] feature_row_subset = new double[features_row_original.length - 1];
                for (; k < features_row_original.length; k++, j++) {
                    if (k == attribute.index) {
                        k++;
                    }
                    if (k < features_row_original.length) {
                        feature_row_subset[j] = features_row_original[k];
                    }
                }
                m_data_subset.add(feature_row_subset);
            }
        }
        return m_data_subset;
    }

    
    private ArrayList<TreeMap<Integer, String>> get_features_m_enum_to_str_subset(Attribute attribute, Label label) {
        ArrayList<TreeMap<Integer, String>> m_enum_to_str_subset = new ArrayList<TreeMap<Integer, String>>();
        for(int i = 0; i < features.m_enum_to_str.size(); i++) {
            if (i != attribute.index) {
                m_enum_to_str_subset.add(features.m_enum_to_str.get(i));
            }
        }
        return m_enum_to_str_subset;
    }

    
    private ArrayList<TreeMap<String, Integer>> get_features_m_str_to_enum_subset(Attribute attribute, Label label) {
        ArrayList<TreeMap<String, Integer>> m_str_to_enum_subset = new ArrayList<TreeMap<String, Integer>>();
        for(int i = 0; i < features.m_str_to_enum.size(); i++) {
            if (i != attribute.index) {
                m_str_to_enum_subset.add(features.m_str_to_enum.get(i));
            }
        }
        return m_str_to_enum_subset;
    }

    
    private ArrayList<String> get_labels_attr_name_subset(Attribute attribute, Label label) {
        return labels.m_attr_name;
    }

    
    private ArrayList<double[]> get_labels_m_data_subset(Attribute attribute, Label label) {
        ArrayList<double[]> m_data_subset = new ArrayList<double[]>();
        for (int i = 0; i < features.rows(); i++) {
            double[] features_row_original = features.row(i);
            if (features_row_original[attribute.index] == label.value) {
                m_data_subset.add(labels.row(i));
            }
        }
        return m_data_subset;
    }
    

    private ArrayList<TreeMap<Integer, String>> get_labels_m_enum_to_str_subset(Attribute attribute, Label label) {
        return labels.m_enum_to_str;
    }

    
    private ArrayList<TreeMap<String, Integer>> get_flabels_m_str_to_enum_subset(Attribute attribute, Label label) {
        return labels.m_str_to_enum;
    }

    double count_label(Attribute attribute, Label label) {
        int counter = 0;
        for (int row = 0; row < features.rows(); row++) {
            if (features.row(row)[attribute.index] == label.value) {
                counter++;
            }
        }
        return counter;
    }

    double count_class_label(Label label) {
        int counter = 0;
        for (int row = 0; row < labels.rows(); row++) {
            if (labels.row(row)[0] == label.value) {
                counter++;
            }
        }
        return counter;
    }
    
    
}
