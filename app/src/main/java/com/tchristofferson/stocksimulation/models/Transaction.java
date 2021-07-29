package com.tchristofferson.stocksimulation.models;

import com.tchristofferson.stocksimulation.TransactionTypeConverter;
import com.tchristofferson.stocksimulation.Util;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

@Entity(tableName = "Transactions", primaryKeys = {"symbol", "timeOfPurchase"})
public class Transaction implements Comparable<Transaction> {

    private final String symbol;
    private final long timeOfPurchase;

    @ColumnInfo(name = "transaction_type")
    @TypeConverters(TransactionTypeConverter.class)
    private final Type type;

    @ColumnInfo(name = "shares")
    private final int shares;

    @ColumnInfo(name = "sold_shares")
    private int soldShares;

    @ColumnInfo(name = "price_per_share")
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
        BUY,
        SELL
    }
}
