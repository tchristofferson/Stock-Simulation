package com.tchristofferson.stocksimulation.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StockCache {

    private final StockFetcher fetcher;
    private final Cache<String, StockInfo> stockInfoCache;
    private final Cache<String, List<PriceTimePair>> historyCache;

    public StockCache() {
        this.fetcher = new StockFetcher();
        this.stockInfoCache = CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .build();
        this.historyCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(5)//Set max because the list can contain lots of price time pairs
                .build();
    }

    public List<StockInfo> getStockInfo(boolean requireLive, String... symbols) throws IOException {
        List<String> notCached = new ArrayList<>(symbols.length);
        List<StockInfo> cachedStockInfo = new ArrayList<>(symbols.length);

        for (String symbol : symbols) {
            StockInfo stockInfo = null;

            if (!requireLive) {
                synchronized (stockInfoCache) {
                    stockInfo = stockInfoCache.getIfPresent(Util.formatSymbol(symbol));
                }
            }

            if (stockInfo != null) {
                cachedStockInfo.add(stockInfo);
            } else {
                notCached.add(Util.formatSymbol(symbol));
            }
        }

        if (notCached.isEmpty())
            return cachedStockInfo;

        List<StockInfo> stockInfoList = new ArrayList<>(cachedStockInfo);
        List<StockInfo> fetched = fetcher.fetchStockInfo(notCached.toArray(new String[0]));

        for (StockInfo stockInfo : fetched) {
            stockInfoList.add(stockInfo);

            synchronized (stockInfoCache) {
                stockInfoCache.put(stockInfo.getSymbol(), stockInfo);
            }
        }

        return stockInfoList;
    }

    public List<PriceTimePair> getHistoricalData(String symbol, TimeFrame timeFrame) throws IOException {
        symbol = Util.formatSymbol(symbol);
        List<PriceTimePair> historyList;

        synchronized (historyCache) {
            historyList = historyCache.getIfPresent(symbol);
        }

        if (historyList == null) {
            historyList = fetcher.fetchPriceHistory(symbol, timeFrame);

            synchronized (historyCache) {
                historyCache.put(symbol, historyList);
            }
        }

        return historyList;
    }

    public List<PriceTimePair> getPortfolioValueHistory(Portfolio portfolio, TimeFrame timeFrame) {
        //TODO: Implement getPortfolioValueHistory
        return null;
    }
}
