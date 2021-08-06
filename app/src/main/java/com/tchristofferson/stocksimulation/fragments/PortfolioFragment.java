package com.tchristofferson.stocksimulation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.adapters.WatchListAdapter;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//This is the initial fragment displayed by MainActivity when the app starts
public class PortfolioFragment extends StockChartFragment {

    private TextView investingTextView;
    private WatchListAdapter adapter;
    private RecyclerView watchList;

    public PortfolioFragment() {
        super(R.layout.fragment_portfolio);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        investingTextView = getView().findViewById(R.id.investing_amount_textview);
        watchList = getView().findViewById(R.id.watch_list_recylerview);
        watchList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WatchListAdapter(requireActivity());
        watchList.setAdapter(adapter);

        List<PriceTimePair> prices = new ArrayList<>(5);
        prices.add(new PriceTimePair("7/10", 10D));
        prices.add(new PriceTimePair("7/11", 11D));
        prices.add(new PriceTimePair("7/12", 10.5D));
        prices.add(new PriceTimePair("7/13", 12D));
        prices.add(new PriceTimePair("7/14", 15.3D));

        Util.fillStockChart(stockChart, prices);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        StockSimulationApplication application = (StockSimulationApplication) requireActivity().getApplication();
        Portfolio portfolio = application.getPortfolio();
        List<Stock> stocks = portfolio.getStocks();

        new Thread(() -> {
            String[] ownedSymbols = new String[stocks.size()];

            for (int i = 0; i < stocks.size(); i++) {
                Stock stock = stocks.get(i);
                ownedSymbols[i] = stock.getSymbol();
            }

            List<StockInfo> stockInfo;

            try {
                stockInfo = application.getStockCache().getStockInfo(false, ownedSymbols);
            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), R.string.failed_fetch, Toast.LENGTH_LONG).show());
                return;
            }

            requireActivity().runOnUiThread(() -> {
                double value = 0;

                for (StockInfo info : stockInfo) {
                    Stock stock = portfolio.getStock(info.getSymbol());

                    if (stock == null)
                        continue;

                    value += info.getLatestPrice() * stock.getShares();
                }

                investingTextView.setText(String.format("$%s", Util.formatMoney(value)));
            });
        }).start();
    }
}
