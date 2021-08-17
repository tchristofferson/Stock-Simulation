package com.tchristofferson.stocksimulation.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.models.Portfolio;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private static final double MIN_AMOUNT = 0.01;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Portfolio portfolio = ((StockSimulationApplication) requireActivity().getApplication()).getPortfolio();
        EditText addFundsEditText = requireView().findViewById(R.id.add_funds_edit_text);
        Button addFundsBtn = requireView().findViewById(R.id.add_funds_button);

        EditText resetEditText = requireView().findViewById(R.id.reset_sim_edit_text);
        Button resetButton = requireView().findViewById(R.id.reset_sim_btn);

        addFundsBtn.setOnClickListener(v -> {
            double amount = getDouble(addFundsEditText);

            if (amount < MIN_AMOUNT) {
                Toast.makeText(requireContext(), R.string.add_funds_too_small, Toast.LENGTH_SHORT).show();
                return;
            }

            portfolio.addMoney(amount);
            addFundsEditText.getText().clear();
            Toast.makeText(requireContext(), R.string.added_funds, Toast.LENGTH_LONG).show();
        });

        resetButton.setOnClickListener(v -> {
            double amount = getDouble(resetEditText);

            if (amount < MIN_AMOUNT) {
                Toast.makeText(requireContext(), R.string.add_funds_too_small, Toast.LENGTH_LONG).show();
                return;
            }

            portfolio.resetPortfolio(amount);
            resetEditText.getText().clear();
            Toast.makeText(requireContext(), R.string.simulation_reset, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            ((StockSimulationApplication) requireActivity().getApplication()).saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getDouble(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }
}
