package com.tchristofferson.stocksimulation.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.adapters.SearchAdapter;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {

    private SearchAdapter adapter;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StockSimulationApplication application = (StockSimulationApplication) requireActivity().getApplication();
        Portfolio portfolio = application.getPortfolio();
        EditText searchEditText = requireView().findViewById(R.id.search_edit_text);
        ImageButton searchButton = requireView().findViewById(R.id.search_btn);
        RecyclerView recyclerView = requireView().findViewById(R.id.search_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SearchAdapter(requireContext(), portfolio);
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> new Thread(() -> {
            List<StockInfo> searchResults;

            try {
                searchResults = application.getStockCache().getStockInfo(false, searchEditText.getText().toString().replace(" ", ""));
            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), R.string.search_failed_fetch, Toast.LENGTH_LONG).show());
                return;
            }

            requireActivity().runOnUiThread(() -> adapter.populateSearchResults(searchResults));
        }).start());
    }
}
