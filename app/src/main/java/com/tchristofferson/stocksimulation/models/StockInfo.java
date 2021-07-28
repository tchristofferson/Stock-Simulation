package com.tchristofferson.stocksimulation.models;

import com.tchristofferson.stocksimulation.Util;

public class StockInfo {

    private final String symbol;
    private final String companyName;
    private final double latestPrice;

    public StockInfo(String symbol, String companyName, double latestPrice) {
        this.symbol = Util.formatSymbol(symbol);
        this.companyName = companyName;
        this.latestPrice = latestPrice;
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
}
