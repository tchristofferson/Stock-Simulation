package com.tchristofferson.stocksimulation.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {

    private static final int POSITIVE_COLOR = Color.rgb(0, 200, 5);
    private static final int NEGATIVE_COLOR = Color.rgb(255, 80, 0);

    private final List<String> watchList;

    public WatchListAdapter(StockSimulationApplication application) {
        watchList = application.getPortfolio().getWatchList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchListAdapter.ViewHolder holder, int position) {
        holder.symbolTextview.setText(watchList.get(position));
    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayout;
        private final TextView symbolTextview;
        private final TextView companyTextview;
        private final TextView priceTextview;
        private final TextView percentTextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.watch_list_performance_layout);
            symbolTextview = itemView.findViewById(R.id.watch_list_stock_symbol_textview);
            companyTextview = itemView.findViewById(R.id.watch_list_company_name_textview);
            priceTextview = itemView.findViewById(R.id.watch_list_stock_price_textview);
            percentTextview = itemView.findViewById(R.id.watch_list_stock_percent_textview);
        }
    }

}
