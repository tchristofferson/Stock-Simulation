package com.tchristofferson.stocksimulation.models;

import com.tchristofferson.stocksimulation.TransactionTypeConverter;

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

    @ColumnInfo(name = "price_per_share")
    private final double pricePerShare;

    public Transaction(String symbol, Type type, long timeOfPurchase, int shares, double pricePerShare) {
        this.symbol = symbol.trim().toUpperCase();
        this.type = type;
        this.timeOfPurchase = timeOfPurchase;
        this.shares = shares;
        this.pricePerShare = pricePerShare;
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

    public double getPricePerShare() {
        return pricePerShare;
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
