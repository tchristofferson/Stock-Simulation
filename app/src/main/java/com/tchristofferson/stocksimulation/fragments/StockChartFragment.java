package com.tchristofferson.stocksimulation.fragments;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockMarker;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.PriceTimePair;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StockChartFragment extends Fragment {

    protected LineChart stockChart;

    public StockChartFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockChart = getView().findViewById(R.id.stock_chart_view);
        Util.setupStockChart(stockChart, getContext());
    }
}
