package com.tchristofferson.stocksimulation.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class StockCache {

    private final StockFetcher fetcher;
    private final File filesDir;
    private final Cache<String, StockInfo> stockInfoCache;

    public StockCache(File filesDir) {
        this.fetcher = new StockFetcher();
        this.filesDir = filesDir;
        this.stockInfoCache = CacheBuilder.newBuilder()
                        .expireAfterWrite(30, TimeUnit.SECONDS)
                        .build();
    }

    public StockInfo getStockInfo(String symbol) throws ExecutionException {
        return stockInfoCache.get(Util.formatSymbol(symbol), () -> fetcher.fetchStockInfo(symbol));
    }

    public Map<String, Double> getHistoricalData(String symbol, TimeFrame timeFrame) throws IOException {
        Map<String, Double> prices;
        File file = new File(filesDir, Util.formatSymbol(symbol) + ".json");

        if (!file.exists()) {
            //Fetch using StockFetcher and save to file
            prices = fetcher.fetchPriceHistory(symbol, timeFrame);
            writeStockHistory(symbol, prices);
            return prices;
        }

        return readStockHistory(symbol);
    }

    private void writeStockHistory(String symbol, Map<String, Double> prices) {
        //TODO: Implement writeStockHistory
    }

    private Map<String, Double> readStockHistory(String symbol) {
        //TODO: Implement readStockHistory
        return null;
    }
}
