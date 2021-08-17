package com.tchristofferson.stocksimulation.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tchristofferson.stocksimulation.models.PriceTimePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HistoricalData {

    private final Cache<TimeFrame, List<PriceTimePair>> history;

    public HistoricalData() {
        history = CacheBuilder.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .build();
    }

    public List<PriceTimePair> getData(TimeFrame timeFrame) {
        List<PriceTimePair> pairs = history.getIfPresent(timeFrame);

        if (pairs != null)
            return new ArrayList<>(pairs);

        return null;
    }

    public void putData(TimeFrame timeFrame, List<PriceTimePair> data) {
        history.put(timeFrame, data);
    }
}
