package com.tchristofferson.stocksimulation.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.activities.StockActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder> {

    private static final int POSITIVE_COLOR = Color.rgb(0, 200, 5);
    private static final int NEGATIVE_COLOR = Color.rgb(255, 80, 0);

    private final Context context;
    private final List<String> watchList;

    public WatchListAdapter(Activity activity) {
        this.context = activity;
        watchList = ((StockSimulationApplication) activity.getApplication()).getPortfolio().getWatchList();
    }

    @NonNull
    @Override
    public WatchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_list_item, parent, false);

        return new WatchListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchListViewHolder holder, int position) {
        String symbol = watchList.get(position);
        holder.symbolTextview.setText(symbol);

        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockActivity.class);
            intent.putExtra(context.getString(R.string.symbol_key), Util.formatSymbol(symbol));
            WatchListAdapter.this.context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }

    public static class WatchListViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout layout;
        private final LinearLayout performanceLayout;
        private final TextView symbolTextview;
        private final TextView companyTextview;
        private final TextView priceTextview;
        private final TextView percentTextview;

        public WatchListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.layout = itemView.findViewById(R.id.watch_list_item_layout);
            performanceLayout = itemView.findViewById(R.id.watch_list_performance_layout);
            symbolTextview = itemView.findViewById(R.id.watch_list_stock_symbol_textview);
            companyTextview = itemView.findViewById(R.id.watch_list_company_name_textview);
            priceTextview = itemView.findViewById(R.id.watch_list_stock_price_textview);
            percentTextview = itemView.findViewById(R.id.watch_list_stock_percent_textview);
        }
    }

}
