package com.tchristofferson.stocksimulation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final List<StockInfo> searchResults;
    private final Portfolio portfolio;

    public SearchAdapter(Portfolio portfolio) {
        searchResults = new ArrayList<>(1);
        this.portfolio = portfolio;
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
        holder.watchListCheckbox.setChecked(portfolio.getWatchList().contains(stockInfo.getSymbol()));
        holder.watchListCheckbox.setText("");
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

        private final TextView companyTextview;
        private final TextView symbolTextview;
        private final CheckBox watchListCheckbox;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            companyTextview = itemView.findViewById(R.id.search_company_name_textview);
            symbolTextview = itemView.findViewById(R.id.search_stock_symbol_textview);
            watchListCheckbox = itemView.findViewById(R.id.watch_list_checkbox);

            watchListCheckbox.setOnClickListener(v -> {
                String symbol = symbolTextview.getText().toString();

                if (watchListCheckbox.isChecked())
                    portfolio.addWatchListSymbol(symbol);
                else
                    portfolio.removeWatchListSymbol(symbol);
            });
        }
    }

}
