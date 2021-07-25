package com.tchristofferson.stocksimulation;

import android.app.Application;

import com.tchristofferson.stocksimulation.models.Portfolio;

import java.util.ArrayList;
import java.util.List;

public class StockSimulationApplication extends Application {

    private Portfolio portfolio;

    @Override
    public void onCreate() {
        super.onCreate();

        List<String> watchlist = new ArrayList<>();
        watchlist.add("AAPL");
        watchlist.add("TSLA");

        portfolio = new Portfolio(5000, new ArrayList<>(), watchlist);

        //TODO: Load stocks and watchlist
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
}
