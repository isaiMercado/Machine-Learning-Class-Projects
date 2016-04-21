/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets_stock_price_prediction;

import java.util.ArrayList;

public class TextManager
{

    public String clean(String tweet){
    
//      String tweet_no_url = Regex.VALID_URL.matcher(tweet).replaceAll("");
//      String tweet_no_hashtag = Regex.VALID_HASHTAG.matcher(tweet_no_url).replaceAll("");
//      String tweet_no_at = tweet_no_hashtag.replace("@", "");
        
        String tweet_no_url = tweet.replaceAll("((https?://)(.*?)($|\\s))", "");
        String tweet_no_hashtag = tweet_no_url.replaceAll("(#(.*?)($|\\s))", "");
        String tweet_no_at = tweet_no_hashtag.replace("@", "");


        //System.out.println("*********** TWEET BEFORE: " + tweet);
        //System.out.println("*********** TWEET AFTER: " + tweet_no_at);
      
      return tweet_no_at;
   }
    
    
    public ArrayList<String> clean(ArrayList<String> tweets) {
        ArrayList<String> tweets_list = new ArrayList();
        for (String tweet : tweets) {
            String cleaned_tweet = clean(tweet);
            tweets_list.add(cleaned_tweet);
        }
        return tweets_list;
    }
}