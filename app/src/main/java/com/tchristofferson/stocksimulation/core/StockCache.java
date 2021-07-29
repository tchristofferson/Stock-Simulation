package com.tchristofferson.stocksimulation.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public List<StockInfo> getStockInfo(String... symbols) throws IOException {
        List<String> notCached = new ArrayList<>(symbols.length);
        List<StockInfo> cachedStockInfo = new ArrayList<>(symbols.length);

        for (String symbol : symbols) {
            StockInfo stockInfo = stockInfoCache.getIfPresent(Util.formatSymbol(symbol));

            if (stockInfo != null) {
                cachedStockInfo.add(stockInfo);
            } else {
                notCached.add(Util.formatSymbol(symbol));
            }
        }

        if (notCached.isEmpty())
            return cachedStockInfo;

        List<StockInfo> stockInfoList = new ArrayList<>(cachedStockInfo);
        stockInfoList.addAll(fetcher.fetchStockInfo(notCached.toArray(new String[0])));

        return stockInfoList;
    }

    public List<PriceTimePair> getHistoricalData(String symbol, TimeFrame timeFrame) throws IOException {
        List<PriceTimePair> prices;
        File file = new File(filesDir, Util.formatSymbol(symbol) + ".json");

        if (!file.exists()) {
            //Fetch using StockFetcher and save to file
            prices = fetcher.fetchPriceHistory(symbol, timeFrame);
            writeStockHistory(symbol, prices);
            return prices;
        }

        return readStockHistory(symbol);
    }

    private void writeStockHistory(String symbol, List<PriceTimePair> prices) {
        //TODO: Implement writeStockHistory
    }

    private List<PriceTimePair> readStockHistory(String symbol) {
        //TODO: Implement readStockHistory
        return null;
    }
}
