package com.tchristofferson.stocksimulation;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tchristofferson.stocksimulation.core.StockCache;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.storage.PortfolioSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StockSimulationApplication extends Application {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Portfolio.class, new PortfolioSerializer())
            .create();

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Portfolio portfolio;
    private List<String> watchList;
    private StockCache stockCache;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            watchList = loadWatchList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            portfolio = loadPortfolio();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        stockCache = new StockCache();
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

    public void saveData() throws IOException {
        File portfolioFile = new File(getFilesDir(), "portfolio.json");
        File watchListFile = new File(getFilesDir(), "watchlist.json");

        String portfolioJson = gson.toJson(portfolio, Portfolio.class);
        BufferedWriter writer = new BufferedWriter(new FileWriter(portfolioFile));
        writer.write(portfolioJson);
        writer.close();

        String watchListJson = gson.toJson(watchList, new TypeToken<List<String>>(){}.getType());
        writer = new BufferedWriter(new FileWriter(watchListFile));
        writer.write(watchListJson);
        writer.close();
    }

    public void runAsync(Runnable runnable) {
        executorService.submit(runnable);
    }

    public void shutdownExecutor() {
        executorService.shutdown();
    }

    private List<String> loadWatchList() throws FileNotFoundException {
        File file = new File(getFilesDir(), "watchlist.json");

        if (!file.exists())
            return new ArrayList<>();

        FileReader fileReader = new FileReader(file);
        return gson.fromJson(fileReader, new TypeToken<List<String>>(){}.getType());
    }

    private Portfolio loadPortfolio() throws FileNotFoundException {
        File file = new File(getFilesDir(), "portfolio.json");

        if (!file.exists())
            return new Portfolio(1000, Collections.emptyList());

        FileReader fileReader = new FileReader(file);
        return gson.fromJson(fileReader, Portfolio.class);
    }
}
