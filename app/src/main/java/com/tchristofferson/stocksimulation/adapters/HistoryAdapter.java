package com.tchristofferson.stocksimulation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.Transaction;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Stock stock;

    public HistoryAdapter(Stock stock) {
        this.stock = stock;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_item, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        //                                                   displays most recent transaction first
        Transaction transaction = stock.getTransaction(stock.getTransactionCount() - 1 - position);
        holder.transactionTypeTextview.setText(transaction.getType().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(transaction.getTimeOfPurchase());
        holder.dateTextview.setText(String.format("%s/%s/%s", calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR)));
        holder.totalTextview.setText(String.format("$%s", Util.formatMoney(transaction.getShares() * transaction.getPricePerShare())));
        holder.priceTextview.setText(String.format(Locale.getDefault(), "%d at $%s", transaction.getShares(), transaction.getPricePerShare()));
    }

    @Override
    public int getItemCount() {
        return stock.getTransactionCount();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView transactionTypeTextview;
        private final TextView dateTextview;
        private final TextView totalTextview;
        private final TextView priceTextview;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionTypeTextview = itemView.findViewById(R.id.transaction_type_textview);
            dateTextview = itemView.findViewById(R.id.date_textview);
            totalTextview = itemView.findViewById(R.id.transaction_total_textview);
            priceTextview = itemView.findViewById(R.id.share_amount_at_price_textview);
        }
    }

}
