package com.tchristofferson.stocksimulation.activities;

import android.os.Bundle;

import com.tchristofferson.stocksimulation.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StockActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String symbol = bundle.getString(getString(R.string.symbol_key));
    }
}
