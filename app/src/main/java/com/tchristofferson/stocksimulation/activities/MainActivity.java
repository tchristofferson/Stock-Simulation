package com.tchristofferson.stocksimulation.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.fragments.PortfolioFragment;
import com.tchristofferson.stocksimulation.fragments.SearchFragment;
import com.tchristofferson.stocksimulation.fragments.SettingsFragment;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment portfolio = new PortfolioFragment();
        Fragment search = new SearchFragment();
        Fragment settings = new SettingsFragment();

        setCurrentFragment(portfolio);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                setCurrentFragment(portfolio);
            } else if (item.getItemId() == R.id.nav_search) {
                setCurrentFragment(search);
            } else {
                setCurrentFragment(settings);
            }

            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((StockSimulationApplication) getApplication()).shutdownExecutor();
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, fragment)
                .commit();
    }
}