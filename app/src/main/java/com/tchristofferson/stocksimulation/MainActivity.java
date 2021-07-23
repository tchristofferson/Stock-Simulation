package com.tchristofferson.stocksimulation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tchristofferson.stocksimulation.models.Stock;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment portfolio = new Fragment(R.layout.activity_portfolio);
        Fragment search = new Fragment(R.layout.activity_search);
        Fragment settings = new Fragment(R.layout.activity_settings);

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

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_frame, fragment)
                .commit();
    }
}