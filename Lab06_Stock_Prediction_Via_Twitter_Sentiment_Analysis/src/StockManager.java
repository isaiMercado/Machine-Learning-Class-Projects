package tweets_stock_price_prediction;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bobbyswingler on 3/23/16.
 */
public class StockManager
{
    public double getFluctuation(String company, int daysAgo){

        Stock stock = null;
        HistoricalQuote historical = null;
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.DATE, -(daysAgo + 1));
        to.add(Calendar.DATE, -daysAgo);


        try {
            stock = YahooFinance.get(company, from, to, Interval.DAILY);
            List<HistoricalQuote> history = stock.getHistory(from, to);
            historical = history.get(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

//        BigDecimal changePercent = stock.getQuote().getChangeInPercent();
//        BigDecimal price = stock.getQuote().getPrice();
//        BigDecimal change = stock.getQuote().getChange();


        BigDecimal change = null;
        BigDecimal quote = null;
        if (daysAgo == 0){
            change = stock.getQuote().getChange();
            quote = stock.getQuote().getPrice();
        }
        else {
            BigDecimal open = historical.getOpen();
            BigDecimal close = historical.getClose();
            change = close.subtract(open);
            quote = close;
        }


        //BigDecimal changePercent = historical.getQuote().getChangeInPercent();
        //BigDecimal price = historical.getQuote().getPrice();
        //BigDecimal change = historical.getQuote().getChange();




        System.out.println("COMPANY: " + company + " QUOTE: " + quote.toString() + " CHANGE: " + change.toString() + " FROM: " + from.getTime() + " TO: " + to.getTime());

        stock.print();

        return change.doubleValue();
    }

//    public BigDecimal calcHistoricalChange (BigDecimal open, BigDecimal close){
//        BigDecimal change = null;
//        if (open.doubleValue() <= close.doubleValue()){
//            change = close.subtract(open);
//        }
//        else {
//            change = close.subtract(open);
//        }
//        return change;
//    }
}
