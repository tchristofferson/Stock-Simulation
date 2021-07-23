package com.tchristofferson.stocksimulation.models;

import java.util.ArrayList;
import java.util.List;

//Doesn't need Room annotations. Only need to save Transactions
public class Stock {

    private final String symbol;
    private final List<Transaction> transactions;
    private int shares = 0;

    public Stock(String symbol, List<Transaction> transactions) {
        this.symbol = symbol;
        this.transactions = transactions;

        for (Transaction transaction : transactions) {
            if (transaction.getType() == Transaction.Type.BUY)
                shares += transaction.getShares();
            else
                shares -= transaction.getShares();
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.getType() == Transaction.Type.BUY)
            shares += transaction.getShares();
        else
            shares -= transaction.getShares();

        transactions.add(transaction);
    }

    public int getShares() {
        return shares;
    }
}
