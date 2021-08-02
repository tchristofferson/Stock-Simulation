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
import android.widget.Toast;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.activities.StockActivity;
import com.tchristofferson.stocksimulation.core.StockCache;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder> {

    private static final int POSITIVE_COLOR = Color.rgb(0, 200, 5);//green
    private static final int NEUTRAL_COLOR = Color.rgb(128, 128, 128);//gray
    private static final int NEGATIVE_COLOR = Color.rgb(255, 80, 0);//red

    private final Context context;
    private final ExecutorService executorService;
    private final StockCache stockCache;
    private final List<String> watchList;

    public WatchListAdapter(Activity activity) {
        this.context = activity;
        executorService = Executors.newCachedThreadPool();
        StockSimulationApplication application = (StockSimulationApplication) activity.getApplication();
        stockCache = application.getStockCache();
        watchList = application.getPortfolio().getWatchList();
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

        executorService.submit(() -> {
            StockInfo stockInfo;

            try {
                stockInfo = stockCache.getStockInfo(symbol).get(0);
            } catch (IOException e) {
                e.printStackTrace();

                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setText("Failed to fetch stock info!");
                ((Activity) context).runOnUiThread(toast::show);
                return;
            }

            ((Activity) context).runOnUiThread(() -> {
                holder.companyTextview.setText(stockInfo.getCompanyName());
                holder.priceTextview.setText(String.format("$%s", stockInfo.getLatestPrice()));
                double percentChange = Util.formatMoney(stockInfo.getChangePercent() * 100);
                holder.percentTextview.setText(String.format("%s%%", percentChange));

                if (percentChange < 0)
                    holder.performanceLayout.setBackgroundColor(NEGATIVE_COLOR);
                else if (percentChange > 0)
                    holder.performanceLayout.setBackgroundColor(POSITIVE_COLOR);
                else
                    holder.performanceLayout.setBackgroundColor(NEUTRAL_COLOR);
            });
        });

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
