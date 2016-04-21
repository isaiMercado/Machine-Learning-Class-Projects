package tweets_stock_price_prediction;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bobbyswingler on 3/22/16.
 */
public class DatasetFactory
{

    public static boolean VERBOSE = false;
    public static boolean APPEND = false;
    public static int DAYS = 3;
    //public static int TWEETS_COUNT = 1000;
    public static String[] COLUMN_TOPICS = {
            "Apple",
            "Microsoft",
            "Google",
            "Samsung",
            "Sony",
            "Mac",
            "StockFluctuation"};

    public DatasetFactory(){

    }

    public void buildDataset(){

        //Init Controllers
        System.out.println("*** Initializing Controllers");
        SentimentManager sentimentManager = new SentimentManager();
        TextManager textManager = new TextManager();
        TweetsManager tweetsManager = new TweetsManager();
        StockManager stockManager = new StockManager();
        CSVManager csvManager = new CSVManager("data_set.csv", APPEND);

        if (APPEND == false) {
            csvManager.writeColumnNames(COLUMN_TOPICS);
        }
        csvManager.finishLine();

        DateTime now = new DateTime(new Date());


        for (int day = 0; day < DAYS; day++) {

            System.out.println("** DAY: " + day);
            //Set up day and previous day
            String dateformat = "YYYY-MM-dd";
            String toDate = now.minusDays(day).toString(dateformat);
            String fromDate = now.minusDays(day + 1).toString(dateformat);

            for (String columnTopic : COLUMN_TOPICS) {
                if (!columnTopic.equals("StockFluctuation")){
                    ArrayList<String> tweets = tweetsManager.getTweets(columnTopic, fromDate, toDate);
                    ArrayList<String> clean_tweets = textManager.clean(tweets);
                    double sentimentAvarage = sentimentManager.sentimentAvarage(clean_tweets, VERBOSE);
                    csvManager.writeEntry(String.valueOf(sentimentAvarage));
                }
            }

            double fluctuation = stockManager.getFluctuation("AAPL", day);
            csvManager.writeEntry(String.valueOf(fluctuation));
            csvManager.finishLine();
        }

        csvManager.close();
    }
}
