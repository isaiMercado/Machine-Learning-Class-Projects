/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets_stock_price_prediction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;


public class CSVManager {
    
    private PrintWriter writer;

    public CSVManager(String filename, boolean append){
        init(filename, append);
    }
    
    public void init(String fileName, boolean append) {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, append)));
        } catch (Exception e) {
            System.out.println("error opening file");
        }
    }
    
    public void writeEntry(String text) {
        writer.print(text + ", ");
    }
    
    public void writeColumnNames(String[] columnNames) {
        for (String columnTopic : columnNames) {
            writeEntry(columnTopic);
        }
    }
    
    public void finishLine() {
        writer.print("\n");
    }
    
    public void close() {
        writer.close();
    }
}
