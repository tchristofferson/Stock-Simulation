package com.tchristofferson.stocksimulation.core;

import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.StockInfo;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StockFetcherTests {

    private static final StockCache stockCache = new StockCache();

    @Test
    public void testGetPrice() throws IOException {
        List<StockInfo> stockInfoList = stockCache.getStockInfo(false, "AAPL", "TSLA");
        assertEquals(2, stockInfoList.size());
    }

    @Test
    public void testGetPriceHistory() throws IOException {
        List<PriceTimePair> priceTimePairList = stockCache.getHistoricalData("AAPL", TimeFrame.ONE_DAY);
        assertFalse(priceTimePairList.isEmpty());
    }
}