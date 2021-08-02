package com.tchristofferson.stocksimulation.models;

import com.tchristofferson.stocksimulation.Util;

public class StockInfo {

    private final String symbol;
    private final String companyName;
    private final double latestPrice;
    private final double changePercent;

    public StockInfo(String symbol, String companyName, double latestPrice, double changePercent) {
        this.symbol = Util.formatSymbol(symbol);
        this.companyName = companyName;
        this.latestPrice = latestPrice;
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getLatestPrice() {
        return latestPrice;
    }

    public double getChangePercent() {
        return changePercent;
    }
}
