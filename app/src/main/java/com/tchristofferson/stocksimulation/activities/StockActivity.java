package com.tchristofferson.stocksimulation.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.core.TimeFrame;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StockActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private TextView symbolTextview;
    private TextView companyNameTextview;
    private TextView stockPriceTextview;
    private LineChart stockChart;
    private TextView stockQuantityTextview;
    private TextView stockEquityTextview;
    private TextView todayReturnsTextview;
    private TextView totalReturnsTextview;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle bundle = getIntent().getExtras();
        String symbol = bundle.getString(getString(R.string.symbol_key));
        StockSimulationApplication application = (StockSimulationApplication) getApplication();
        Portfolio portfolio = application.getPortfolio();

        //Top text views
        symbolTextview = findViewById(R.id.activity_stock_symbol_textview);
        companyNameTextview = findViewById(R.id.activity_stock_company_name_textview);
        stockPriceTextview = findViewById(R.id.activity_stock_stock_price_textview);

        stockChart = findViewById(R.id.stock_chart_view);
        Util.setupStockChart(stockChart, this);

        //Bottom text views
        stockQuantityTextview = findViewById(R.id.stock_quantity_textview);
        stockEquityTextview = findViewById(R.id.stock_equity_textview);
        todayReturnsTextview = findViewById(R.id.today_returns_textview);
        totalReturnsTextview = findViewById(R.id.total_returns_textview);

        symbolTextview.setText(symbol);
        Stock stock = portfolio.getStock(symbol);
        loadStockData(symbol, application, stock);
    }

    private void loadStockData(String symbol, StockSimulationApplication application, Stock stock) {
        int shares = stock == null ? 0 : stock.getShares();
        double invested = stock == null ? 0 : stock.getInvested();
        Future<StockInfo> futureStockInfo = executorService.submit(() -> {
            List<StockInfo> stockInfoList;

            try {
                stockInfoList = application.getStockCache().getStockInfo(symbol);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if (stockInfoList.isEmpty())
                return null;

            return stockInfoList.get(0);
        });

        executorService.submit(() -> {
            List<PriceTimePair> prices;
            StockInfo stockInfo;

            try {
                prices = application.getStockCache().getHistoricalData(symbol, TimeFrame.ONE_DAY);
                stockInfo = futureStockInfo.get();
            } catch (IOException | ExecutionException e) {
                e.printStackTrace();

                Toast toast = new Toast(StockActivity.this);
                toast.setText("Failed to retrieve stock info!");
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                runOnUiThread(StockActivity.this::finish);
                return;
            } catch (InterruptedException ignored) { return; }

            runOnUiThread(() -> {
                Util.fillStockChart(stockChart, prices);
                stockQuantityTextview.setText(String.valueOf(shares));

                if (stockInfo != null) {
                    stockEquityTextview.setText(String.format("$%s", Util.formatMoney(shares * stockInfo.getLatestPrice())));
                    companyNameTextview.setText(stockInfo.getCompanyName());
                    stockPriceTextview.setText(String.format("$%s", stockInfo.getLatestPrice()));
                    totalReturnsTextview.setText(String.format("$%s", Util.formatMoney(shares * stockInfo.getLatestPrice() - invested)));
                }
            });
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        executorService.shutdownNow();
    }
}
