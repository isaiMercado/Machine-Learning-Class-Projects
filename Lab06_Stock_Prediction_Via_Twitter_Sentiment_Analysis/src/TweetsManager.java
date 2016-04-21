/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets_stock_price_prediction;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TweetsManager {

    private Twitter twitter;
    public TweetsManager(){
        twitter = new TwitterFactory().getInstance();
    }

    public ArrayList<String> getTweets(String topic, String fromDate, String toDate) {

        //Twitter twitter = new TwitterFactory().getInstance();
        System.out.println("*** TWITTER QUERY: " + topic);
        ArrayList<String> tweetList = new ArrayList<String>();
        try {
            Query query = new Query(topic);
            query.setLang("en");
            //query.setCount(count);
            query.setSince(fromDate);
            query.setUntil(toDate);
            QueryResult result;
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    //System.out.print("LANGUAGE " + tweet.getLang() + "\n\n");
                    //if (tweet.getLang().equals("en")) {
                        tweetList.add(tweet.getText());
                    //}
                }
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
        }

        System.out.println("************ TWEET LIST: " + tweetList.size());
        return tweetList;
    }
}