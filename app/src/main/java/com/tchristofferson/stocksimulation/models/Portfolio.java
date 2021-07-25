package com.tchristofferson.stocksimulation.models;

import java.util.List;

public class Portfolio {

    private double money;
    private final List<Stock> stocks;
    private final List<String> watchList;

    public Portfolio(double money, List<Stock> stocks, List<String> watchList) {
        this.money = money;
        this.stocks = stocks;
        this.watchList = watchList;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public List<String> getWatchList() {
        return watchList;
    }
}
