/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author isai
 */
public class ImprovementChecker {
    
    static int THRESHOLD = 5;
    static int counter;
    static double last_change;
    
    static public boolean improved(double current_change) {
        
        if (current_change == last_change) {
            counter = counter + 1;
        } else {
            counter = 0;
        }
        
        last_change = current_change;
        
        if (counter > THRESHOLD) {
            return false;
        }
        
        return true;
    }
}
