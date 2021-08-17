package com.tchristofferson.stocksimulation.models;

import com.google.common.base.Preconditions;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Portfolio {

    private final List<Stock> stocks;
    private double money;

    public Portfolio(double money, List<Stock> stocks) {
        this.money = money;
        this.stocks = new ArrayList<>(stocks);
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void addMoney(double money) {
        this.money += money;
    }

    public void subMoney(double money) {
        this.money -= money;
    }

    public List<Stock> getStocks() {
        return new ArrayList<>(stocks);
    }

    public Stock getStock(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equalsIgnoreCase(symbol))
                return stock;
        }

        return null;
    }

    //Will add shares and subtract cost from this.money
    public void addShares(String symbol, int shares, double cost) {
        Preconditions.checkState(this.money >= cost, "Invalid funds!");
        Stock stock = getStock(symbol);

        if (stock == null) {
            stock = new Stock(symbol, Collections.emptyList());
            stocks.add(stock);
        }

        Transaction transaction = new Transaction(symbol, Transaction.Type.BUY, System.currentTimeMillis(), shares, cost / shares);
        stock.addTransaction(transaction);
        this.money = Util.formatMoney(this.money - cost);
    }

    //Will take shares and add value to this.money
    public void removeShares(String symbol, int shares, double value) {
        Stock stock = getStock(symbol);
        Preconditions.checkArgument(stock != null, "Stock " + symbol + " isn't owned!");
        Preconditions.checkState(stock.getShares() >= shares, "shares must be <= owned shares!");

        Transaction transaction = new Transaction(symbol, Transaction.Type.SELL, System.currentTimeMillis(), shares, value / shares);
        stock.addTransaction(transaction);
        this.money = Util.formatMoney(this.money + value);
    }

    public void resetPortfolio(double startingMoney) {
        stocks.clear();
        setMoney(startingMoney);
    }
}
