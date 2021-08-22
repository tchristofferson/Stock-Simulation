package com.tchristofferson.stocksimulation.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.adapters.WatchListAdapter;
import com.tchristofferson.stocksimulation.core.StockCache;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//This is the initial fragment displayed by MainActivity when the app starts
public class PortfolioFragment extends Fragment {

    private TextView investingTextView;
    private PieChart diversityChart;
    private WatchListAdapter adapter;

    public PortfolioFragment() {
        super(R.layout.fragment_portfolio);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        investingTextView = requireView().findViewById(R.id.investing_amount_textview);
        RecyclerView watchList = requireView().findViewById(R.id.watch_list_recylerview);
        watchList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WatchListAdapter(requireActivity());
        watchList.setAdapter(adapter);

        Portfolio portfolio = ((StockSimulationApplication) requireActivity().getApplication()).getPortfolio();
        diversityChart = requireView().findViewById(R.id.diversity_chart);
        populateDiversityChart(portfolio, diversityChart);
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

        populateDiversityChart(portfolio, diversityChart);
    }

    private void populateDiversityChart(Portfolio portfolio, PieChart pieChart) {
        StockCache stockCache = ((StockSimulationApplication) requireActivity().getApplication()).getStockCache();
        List<Stock> stocks = portfolio.getStocks();
        String[] symbols = new String[stocks.size()];

        for (int i = 0; i < stocks.size(); i++) {
            symbols[i] = stocks.get(i).getSymbol();
        }

        new Thread(() -> {
            List<StockInfo> stockData;

            try {
                stockData = stockCache.getStockInfo(false, symbols);
            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), R.string.failed_fetch, Toast.LENGTH_LONG).show());
                return;
            }

            requireActivity().runOnUiThread(() -> {
                List<PieEntry> entries = new LinkedList<>();

                for (StockInfo info : stockData) {
                    Stock stock = portfolio.getStock(info.getSymbol());

                    if (stock == null)
                        continue;

                    entries.add(new PieEntry((float) Util.formatMoney(info.getLatestPrice() * stock.getShares()), info.getSymbol()));
                }

                pieChart.setUsePercentValues(true);
                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(ColorTemplate.PASTEL_COLORS);
                PieData data = new PieData(dataSet);
                data.setValueTextSize(12);

                pieChart.setData(data);
                Description description = new Description();
                description.setText("");
                pieChart.setDescription(description);
                pieChart.setDrawHoleEnabled(false);
                pieChart.getLegend().setEnabled(false);
                pieChart.setTouchEnabled(false);

                pieChart.invalidate();
            });
        }).start();
    }
}
