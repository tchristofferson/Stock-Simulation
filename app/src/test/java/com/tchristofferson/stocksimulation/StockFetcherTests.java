package com.tchristofferson.stocksimulation;

import com.tchristofferson.stocksimulation.core.StockFetcher;
import com.tchristofferson.stocksimulation.core.TimeFrame;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class StockFetcherTests {

    private static final StockFetcher stockFetcher = new StockFetcher();

    @Test
    public void testGetPrice() throws IOException {
        double price = stockFetcher.fetchPrice("AAPL");
        Logger.getAnonymousLogger().info("AAPL price: $" + price);
    }

    @Test
    public void testGetPriceHistory() throws IOException {
        Map<String, Double> history = stockFetcher.fetchPriceHistory("AAPL", TimeFrame.ONE_DAY);
        assertNotNull(history);
        assertFalse(history.isEmpty());
        Logger.getAnonymousLogger().info(history.toString());
    }
}