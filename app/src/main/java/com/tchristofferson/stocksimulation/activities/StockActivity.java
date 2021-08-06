package com.tchristofferson.stocksimulation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.adapters.HistoryAdapter;
import com.tchristofferson.stocksimulation.core.TimeFrame;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StockActivity extends AppCompatActivity {

    private TextView companyNameTextview;
    private TextView stockPriceTextview;
    private LineChart stockChart;
    private TextView stockQuantityTextview;
    private TextView stockEquityTextview;
    private TextView todayReturnsTextview;
    private TextView totalReturnsTextview;
    private boolean dataLoaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle bundle = getIntent().getExtras();
        String symbol = bundle.getString(getString(R.string.symbol_key));
        StockSimulationApplication application = (StockSimulationApplication) getApplication();
        Portfolio portfolio = application.getPortfolio();

        //Top text views
        TextView symbolTextview = findViewById(R.id.activity_stock_symbol_textview);
        companyNameTextview = findViewById(R.id.activity_stock_company_name_textview);
        stockPriceTextview = findViewById(R.id.activity_stock_stock_price_textview);

        stockChart = findViewById(R.id.stock_chart_view);
        Util.setupStockChart(stockChart, this);

        //Bottom text views
        stockQuantityTextview = findViewById(R.id.stock_quantity_textview);
        stockEquityTextview = findViewById(R.id.stock_equity_textview);
        todayReturnsTextview = findViewById(R.id.today_returns_textview);
        totalReturnsTextview = findViewById(R.id.total_returns_textview);

        Button tradeButton = findViewById(R.id.trade_btn);
        tradeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TradeActivity.class);
            intent.putExtra(getString(R.string.symbol_key), symbol);

            if (dataLoaded) {
                intent.putExtra(getString(R.string.price_key), Double.parseDouble(stockPriceTextview.getText().toString().substring(1)));
            } else {
                Toast toast = Toast.makeText(this, R.string.wait_for_data, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            startActivity(intent);
        });

        symbolTextview.setText(symbol);
        Stock stock = portfolio.getStock(symbol);

        if (stock == null)
            stock = new Stock(symbol, Collections.emptyList());

        loadStockData(symbol, application, stock);
        RecyclerView historyRecyclerview = findViewById(R.id.history_recyclerview);
        historyRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerview.setAdapter(new HistoryAdapter(stock));
    }

    private void loadStockData(String symbol, StockSimulationApplication application, Stock stock) {
        int shares = stock == null ? 0 : stock.getShares();
        double invested = stock == null ? 0 : stock.getInvested();

        new Thread(() -> {
            List<PriceTimePair> prices;
            StockInfo stockInfo;

            try {
                prices = application.getStockCache().getHistoricalData(symbol, TimeFrame.ONE_DAY);
                stockInfo = application.getStockCache().getStockInfo(false, symbol).get(0);
            } catch (IOException e) {
                e.printStackTrace();

                Toast toast = Toast.makeText(this, R.string.failed_fetch, Toast.LENGTH_LONG);
                toast.show();
                runOnUiThread(StockActivity.this::finish);
                return;
            }

            double openPrice = prices.isEmpty() ? stockInfo.getLatestPrice() : prices.get(0).getPrice();

            runOnUiThread(() -> {
                Util.fillStockChart(stockChart, prices);
                stockQuantityTextview.setText(String.valueOf(shares));

                if (stockInfo != null) {
                    stockEquityTextview.setText(String.format("$%s", Util.formatMoney(shares * stockInfo.getLatestPrice())));
                    companyNameTextview.setText(stockInfo.getCompanyName());
                    stockPriceTextview.setText(String.format("$%s", stockInfo.getLatestPrice()));
                    todayReturnsTextview.setText(String.format("$%s", Util.formatMoney((stockInfo.getLatestPrice() * shares) - (openPrice * shares))));
                    totalReturnsTextview.setText(String.format("$%s", Util.formatMoney(shares * stockInfo.getLatestPrice() - invested)));
                    dataLoaded = true;
                }
            });
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
