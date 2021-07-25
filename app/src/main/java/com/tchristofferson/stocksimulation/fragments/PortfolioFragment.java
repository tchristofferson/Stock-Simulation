package com.tchristofferson.stocksimulation.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.models.PriceTimePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PortfolioFragment extends StockChartFragment {

    private TextView investingTextView;
    private RecyclerView watchList;

    public PortfolioFragment() {
        super(R.layout.fragment_portfolio);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        investingTextView = getView().findViewById(R.id.investing_amount_textview);
        watchList = getView().findViewById(R.id.watch_list_recylerview);

        List<PriceTimePair> prices = new ArrayList<>(5);
        prices.add(new PriceTimePair("7/10", 10D));
        prices.add(new PriceTimePair("7/11", 11D));
        prices.add(new PriceTimePair("7/12", 10.5D));
        prices.add(new PriceTimePair("7/13", 12D));
        prices.add(new PriceTimePair("7/14", 15.3D));

        fillChart(prices);
    }
}
