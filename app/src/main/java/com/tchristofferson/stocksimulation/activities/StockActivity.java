package com.tchristofferson.stocksimulation.activities;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

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
    private HistoryAdapter adapter;
    String symbol;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Bundle bundle = getIntent().getExtras();
        symbol = bundle.getString(getString(R.string.symbol_key));
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

        setGraphButtonListener(findViewById(R.id.one_day_btn), TimeFrame.ONE_DAY);
        setGraphButtonListener(findViewById(R.id.one_week_btn), TimeFrame.ONE_WEEK);
        setGraphButtonListener(findViewById(R.id.one_month_btn), TimeFrame.ONE_MONTH);
        setGraphButtonListener(findViewById(R.id.three_month_btn), TimeFrame.THREE_MONTHS);
        setGraphButtonListener(findViewById(R.id.one_year_btn), TimeFrame.ONE_YEAR);
        setGraphButtonListener(findViewById(R.id.five_year_btn), TimeFrame.FIVE_YEARS);

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
        adapter = new HistoryAdapter(stock);
        historyRecyclerview.setAdapter(adapter);
    }

    private void loadStockData(String symbol, StockSimulationApplication application, Stock stock) {
        int shares = stock == null ? 0 : stock.getShares();
        double invested = stock == null ? 0 : stock.getInvested();

        application.runAsync(() -> {
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

                    double todayReturns = (stockInfo.getLatestPrice() * shares) - (openPrice * shares);

                    if (stock != null && stock.getTransactionCount() > 0) {
                        Calendar timeOfPurchaseCalendar = Calendar.getInstance(TimeZone.getDefault());
                        timeOfPurchaseCalendar.setTimeInMillis(stock.getTransaction(stock.getTransactionCount() - 1).getTimeOfPurchase());

                        if (isCalendarToday(timeOfPurchaseCalendar))
                            todayReturns = (stockInfo.getLatestPrice() * shares) - (stock.getLatestTransaction().getPricePerShare() * shares);
                    }

                    todayReturnsTextview.setText(String.format("$%s", Util.formatMoney(todayReturns)));
                    totalReturnsTextview.setText(String.format("$%s", Util.formatMoney(shares * stockInfo.getLatestPrice() - invested)));
                    dataLoaded = true;
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Portfolio portfolio = ((StockSimulationApplication) getApplication()).getPortfolio();
        Stock stock = portfolio.getStock(symbol);

        if (stock != null)
            adapter.setStock(stock);

        adapter.notifyDataSetChanged();
        loadStockData(symbol, (StockSimulationApplication) getApplication(), stock);
    }

    private boolean isCalendarToday(Calendar calendar) {
        Calendar today = Calendar.getInstance();

        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
    }

    private void setGraphButtonListener(Button button, TimeFrame timeFrame) {
        button.setOnClickListener(v -> ((StockSimulationApplication) getApplication()).runAsync(() -> {
            List<PriceTimePair> timePairs;

            try {
                timePairs = ((StockSimulationApplication) getApplication()).getStockCache()
                        .getHistoricalData(symbol, timeFrame);
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, R.string.failed_fetch, Toast.LENGTH_LONG).show());
                return;
            }

            runOnUiThread(() -> Util.fillStockChart(stockChart, timePairs));
        }));
    }
}
