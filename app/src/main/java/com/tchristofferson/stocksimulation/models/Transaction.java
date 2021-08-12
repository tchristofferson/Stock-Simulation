package com.tchristofferson.stocksimulation.models;

import com.tchristofferson.stocksimulation.Util;

public class Transaction implements Comparable<Transaction> {

    private final String symbol;
    private final long timeOfPurchase;
    private final Type type;
    private final int shares;
    private int soldShares;
    private final double pricePerShare;

    public Transaction(String symbol, Type type, long timeOfPurchase, int shares, double pricePerShare) {
        this(symbol, type, timeOfPurchase, shares, pricePerShare, 0);
    }

    public Transaction(String symbol, Type type, long timeOfPurchase, int shares, double pricePerShare, int soldShares) {
        this.symbol = Util.formatSymbol(symbol);
        this.type = type;
        this.timeOfPurchase = timeOfPurchase;
        this.shares = shares;
        this.pricePerShare = pricePerShare;

        if (type == Type.SELL)
            this.soldShares = shares;
        else
            this.soldShares = soldShares;
    }

    public String getSymbol() {
        return symbol;
    }

    public Type getType() {
        return type;
    }

    public long getTimeOfPurchase() {
        return timeOfPurchase;
    }

    public int getShares() {
        return shares;
    }

    public int getSoldShares() {
        return soldShares;
    }

    public void setSoldShares(int soldShares) {
        if (type == Type.SELL)
            throw new IllegalStateException("Cannot set sold shares for a sell transaction!");

        if (soldShares > shares)
            throw new IllegalArgumentException("soldShares must be <= shares!");

        this.soldShares = soldShares;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    public double getInvested() {
        if (type == Type.SELL)
            throw new IllegalStateException("Cannot get invested amount for sell transactions!");

        return (shares - soldShares) * pricePerShare;
    }

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(this.timeOfPurchase, o.timeOfPurchase);
    }

    public enum Type {
        BUY("Buy"),
        SELL("Sell");

        public final String pretty;

        Type(String pretty) {
            this.pretty = pretty;
        }

        @Override
        public String toString() {
            return pretty;
        }
    }
}
