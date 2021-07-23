package com.tchristofferson.stocksimulation.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Transactions", primaryKeys = {"symbol", "timeOfPurchase"})
public class Transaction implements Comparable<Transaction> {

    private final String symbol;
    private final long timeOfPurchase;

    @ColumnInfo(name = "transaction_type")
    private final Type type;

    @ColumnInfo(name = "shares")
    private final int shares;

    public Transaction(String symbol, Type type, long timeOfPurchase, int shares) {
        this.symbol = symbol.trim().toUpperCase();
        this.type = type;
        this.timeOfPurchase = timeOfPurchase;
        this.shares = shares;
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

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(this.timeOfPurchase, o.timeOfPurchase);
    }

    public enum Type {
        BUY,
        SELL
    }
}
