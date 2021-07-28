package com.tchristofferson.stocksimulation;

import com.tchristofferson.stocksimulation.models.Transaction;

import androidx.room.TypeConverter;

public class TransactionTypeConverter {

    @TypeConverter
    public int fromTransactionType(Transaction.Type type) {
        return type == Transaction.Type.BUY ? 0 : 1;
    }

    @TypeConverter
    public Transaction.Type toTransactionType(int i) {
        return i == 0 ? Transaction.Type.BUY : Transaction.Type.SELL;
    }

}
