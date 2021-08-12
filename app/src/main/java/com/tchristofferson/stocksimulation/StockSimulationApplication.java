package com.tchristofferson.stocksimulation;

import android.app.Application;

import com.tchristofferson.stocksimulation.core.StockCache;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockSimulationApplication extends Application {

    private Portfolio portfolio;
    private List<String> watchList;
    private StockCache stockCache;

    @Override
    public void onCreate() {
        super.onCreate();

        watchList = new ArrayList<>();
        watchList.add("AAPL");
        watchList.add("TSLA");

        List<Stock> stocks = new ArrayList<>(1);
        stocks.add(new Stock("AAPL", Arrays.asList(new Transaction("AAPL", Transaction.Type.BUY, System.currentTimeMillis(), 5, 146.00))));

        portfolio = new Portfolio(5000, stocks);
        stockCache = new StockCache();

        //TODO: Load stocks and watchlist
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public String getWatchListSymbol(int index) {
        return watchList.get(index);
    }

    public int getWatchListSize() {
        return watchList.size();
    }

    public List<String> getWatchList() {
        return new ArrayList<>(watchList);
    }

    public void addToWatchList(String symbol) {
        watchList.add(Util.formatSymbol(symbol));
    }

    public void removeFromWatchList(String symbol) {
        watchList.remove(Util.formatSymbol(symbol));
    }

    public StockCache getStockCache() {
        return stockCache;
    }
}
