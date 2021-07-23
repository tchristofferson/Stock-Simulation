package com.tchristofferson.stocksimulation;

import android.app.Application;

import com.tchristofferson.stocksimulation.models.Stock;

import java.util.List;

public class StockSimulationApplication extends Application {

    private static List<Stock> portfolio;
    private static List<String> watchList;

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO: Load stocks and watchlist
    }
}
