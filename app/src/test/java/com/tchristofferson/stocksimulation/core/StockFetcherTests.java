package com.tchristofferson.stocksimulation.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tchristofferson.stocksimulation.core.StockFetcher;
import com.tchristofferson.stocksimulation.core.TimeFrame;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.StockInfo;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class StockFetcherTests {

    private static final StockFetcher stockFetcher = new StockFetcher();

    @Test
    public void testGetPrice() throws IOException {
        List<StockInfo> stockInfoList = stockFetcher.fetchStockInfo("AAPL", "TSLA");
        assertEquals(2, stockInfoList.size());
    }

    @Test
    public void testGetPriceHistory() throws IOException {
        List<PriceTimePair> priceTimePairList = stockFetcher.fetchPriceHistory("AAPL", TimeFrame.ONE_DAY);
        assertFalse(priceTimePairList.isEmpty());
    }
}