package com.tchristofferson.stocksimulation.fragments;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockMarker;
import com.tchristofferson.stocksimulation.models.PriceTimePair;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StockChartFragment extends Fragment {

    private LineChart stockChart;

    public StockChartFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockChart = getView().findViewById(R.id.stock_chart_view);
        stockChart.setTouchEnabled(true);
        Description description = new Description();
        description.setText("");
        stockChart.setDescription(description);

        stockChart.getAxisLeft().setDrawLabels(false);
        stockChart.getAxisRight().setDrawLabels(false);
        stockChart.getXAxis().setDrawLabels(false);
        stockChart.getLegend().setEnabled(false);
        stockChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        stockChart.getXAxis().setDrawGridLines(false);
        stockChart.getAxisLeft().setDrawGridLines(false);
        stockChart.getAxisRight().setDrawGridLines(false);
        stockChart.setDrawMarkers(true);
        MarkerView marker = new StockMarker(getContext());
        stockChart.setMarker(marker);
        marker.setChartView(stockChart);
    }

    public void fillChart(List<PriceTimePair> priceData) {
        List<Entry> entries = new ArrayList<>(priceData.size());

        for (int i = 0; i < priceData.size(); i++) {
            PriceTimePair pair = priceData.get(i);
            entries.add(new Entry(i, (float) pair.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        dataSet.setValueTextSize(10);

        LineData lineData = new LineData(dataSet);
        stockChart.setData(lineData);

        stockChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return priceData.get(((int) value)).getTime();
            }
        });

        //Updates chart
        stockChart.invalidate();
    }
}
