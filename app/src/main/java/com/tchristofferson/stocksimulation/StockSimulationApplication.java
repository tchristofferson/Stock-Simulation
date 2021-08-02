package com.tchristofferson.stocksimulation;

import android.app.Application;
import android.util.Log;

import com.tchristofferson.stocksimulation.core.StockCache;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockSimulationApplication extends Application {

    private Portfolio portfolio;
    private StockCache stockCache;

    @Override
    public void onCreate() {
        super.onCreate();

        List<String> watchlist = new ArrayList<>();
        watchlist.add("AAPL");
        watchlist.add("TSLA");

        List<Stock> stocks = new ArrayList<>(1);
        stocks.add(new Stock("AAPL", Arrays.asList(new Transaction("AAPL", Transaction.Type.BUY, System.currentTimeMillis(), 5, 146.00))));

        portfolio = new Portfolio(5000, stocks, watchlist);
        stockCache = new StockCache();

        //TODO: Load stocks and watchlist
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public StockCache getStockCache() {
        return stockCache;
    }
}
