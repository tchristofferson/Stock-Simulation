package com.tchristofferson.stocksimulation.models;

import java.util.ArrayList;
import java.util.List;

//Doesn't need Room annotations (doesn't need to be saved in database). Only need to save Transactions
public class Stock {

    private final String symbol;
    private final List<Transaction> transactions;
    private int shares = 0;
    private double invested = 0;

    public Stock(String symbol, List<Transaction> transactions) {
        this.symbol = symbol;
        this.transactions = new ArrayList<>(transactions);

        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.Type.BUY) {
                shares += transaction.getShares();
                invested += transaction.getInvested();
            } else {
                shares -= transaction.getShares();
            }
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public int getTransactionCount() {
        return transactions.size();
    }

    public Transaction getLatestTransaction() {
        if (transactions.isEmpty())
            return null;

        return transactions.get(transactions.size() - 1);
    }

    public Transaction getTransaction(int index) {
        return transactions.get(index);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.getType() == Transaction.Type.BUY) {
            shares += transaction.getShares();
            invested += transaction.getInvested();
        } else {
            shares -= transaction.getShares();

            int remainingShares = transaction.getShares();
            for (Transaction t : transactions) {
                if (t.getType() != Transaction.Type.BUY || t.getShares() == t.getSoldShares())
                    continue;

                if (t.getShares() - t.getSoldShares() >= remainingShares) {
                    t.setSoldShares(t.getSoldShares() + remainingShares);
                    break;
                }

                remainingShares -= t.getShares() - t.getSoldShares();
                t.setSoldShares(t.getShares());
            }
        }

        transactions.add(transaction);
    }

    public int getShares() {
        return shares;
    }

    public double getInvested() {
        return invested;
    }
}
