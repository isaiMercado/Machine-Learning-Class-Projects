/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets_stock_price_prediction;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;

/**
 *
 * @author isai
 */
public class MAIN
{
    public static void main(String[] args) {
        DatasetFactory datasetFactory = new DatasetFactory();
        datasetFactory.buildDataset();
    }
}
