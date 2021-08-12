package com.tchristofferson.stocksimulation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.activities.StockActivity;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final Context context;
    private final List<StockInfo> searchResults;

    public SearchAdapter(Context context) {
        this.context = context;
        this.searchResults = new ArrayList<>(1);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        StockInfo stockInfo = searchResults.get(position);
        holder.companyTextview.setText(stockInfo.getCompanyName());
        holder.symbolTextview.setText(stockInfo.getSymbol());
        holder.watchListCheckbox.setChecked(((StockSimulationApplication) context.getApplicationContext()).getWatchList().contains(stockInfo.getSymbol()));
        holder.watchListCheckbox.setText("");

        holder.container.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockActivity.class);
            intent.putExtra(context.getString(R.string.symbol_key), stockInfo.getSymbol());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void populateSearchResults(List<StockInfo> searchResults) {
        this.searchResults.clear();
        this.searchResults.addAll(searchResults);
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout container;
        private final TextView companyTextview;
        private final TextView symbolTextview;
        private final CheckBox watchListCheckbox;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.search_list_item_container);
            companyTextview = itemView.findViewById(R.id.search_company_name_textview);
            symbolTextview = itemView.findViewById(R.id.search_stock_symbol_textview);
            watchListCheckbox = itemView.findViewById(R.id.watch_list_checkbox);

            watchListCheckbox.setOnClickListener(v -> {
                String symbol = symbolTextview.getText().toString();
                StockSimulationApplication application = (StockSimulationApplication) context.getApplicationContext();

                if (watchListCheckbox.isChecked())
                    application.addToWatchList(symbol);
                else
                    application.removeFromWatchList(symbol);
            });
        }
    }

}
