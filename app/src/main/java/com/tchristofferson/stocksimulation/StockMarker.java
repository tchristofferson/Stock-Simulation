package com.tchristofferson.stocksimulation;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class StockMarker extends MarkerView {

    private final TextView valueTextView;
    private final TextView dateTextView;

    public StockMarker(Context context) {
        super(context, R.layout.marker);
        this.valueTextView = findViewById(R.id.marker_value_textview);
        this.dateTextView = findViewById(R.id.marker_time_textview);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        valueTextView.setText("$" + e.getY());
        dateTextView.setText(getChartView().getXAxis().getValueFormatter().getFormattedValue((int) e.getX()));
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2F), -getHeight());
    }
}
