package com.tchristofferson.stocksimulation;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.tchristofferson.stocksimulation.models.PriceTimePair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static final int MONEY_SCALE = 2;
    private static final RoundingMode MONEY_ROUNDING_MODE = RoundingMode.DOWN;

    public static double formatMoney(double d) {
        return BigDecimal.valueOf(d).setScale(MONEY_SCALE, MONEY_ROUNDING_MODE).doubleValue();
    }

    public static String formatSymbol(String symbol) {
        return symbol.trim().toUpperCase();
    }

    public static void setupStockChart(LineChart stockChart, Context context) {
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
        MarkerView marker = new StockMarker(context);
        stockChart.setMarker(marker);
        marker.setChartView(stockChart);
    }

    public static void fillStockChart(LineChart stockChart, List<PriceTimePair> priceData) {
        List<Entry> entries = new ArrayList<>(priceData.size());

        for (int i = 0; i < priceData.size(); i++) {
            PriceTimePair pair = priceData.get(i);
            entries.add(new Entry(i, (float) pair.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Price");
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

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
