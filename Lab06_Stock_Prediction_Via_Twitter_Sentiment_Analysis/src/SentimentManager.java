/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets_stock_price_prediction;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Properties;

public class SentimentManager {
    
    private StanfordCoreNLP pipeline;
    private String[] SENTIMENTS = { "Very Negative","Negative", "Neutral", "Positive", "Very Positive"};

    public SentimentManager(){
        init();
    }

    public void init() {
        Properties props = new Properties();
        //annotators = tokenize, ssplit, parse, sentiment
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props); //"MyPropFile.properties");
    }

    public int findSentiment(String tweet, boolean verbose) {
        
        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        
        if (verbose) {
            System.out.println("TEXT: " + tweet + " SENTIMENT: " + mainSentiment + " SENTIMENT_STRING: " + SENTIMENTS[mainSentiment]);
        }
        
        return mainSentiment;
    }
    
    
    public double sentimentAvarage(ArrayList<String> tweets, boolean verbose) {
        double sum = 0;
        for (String tweet : tweets) {
            sum = sum + findSentiment(tweet, verbose);
        }
        double output = sum / tweets.size();
        return output;
    }
}