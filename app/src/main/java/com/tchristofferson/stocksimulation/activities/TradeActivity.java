package com.tchristofferson.stocksimulation.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tchristofferson.stocksimulation.R;
import com.tchristofferson.stocksimulation.StockSimulationApplication;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.Portfolio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TradeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        StockSimulationApplication application = ((StockSimulationApplication) getApplication());
        Portfolio portfolio = application.getPortfolio();
        Bundle bundle = getIntent().getExtras();
        double price = bundle.getDouble(getString(R.string.price_key));
        String symbol = Util.formatSymbol(bundle.getString(getString(R.string.symbol_key)));

        TextView symbolTextview = findViewById(R.id.activity_trade_stock_symbol_textview);
        TextView fundsTextview = findViewById(R.id.activity_trade_available_funds_textview);
        RadioButton buyRadioBtn = findViewById(R.id.buy_radio_btn);
        RadioButton sellRadioBtn = findViewById(R.id.sell_radio_btn);
        EditText sharesEditText = findViewById(R.id.activity_trade_shares_edit_text);
        TextView priceTextview = findViewById(R.id.activity_trade_market_price_textview);
        TextView estimatedCostTextview = findViewById(R.id.activity_trade_estimated_cost_textview);
        Button submitButton = findViewById(R.id.activity_trade_submit_btn);

        symbolTextview.setText(symbol);
        fundsTextview.setText(String.format("$%s", portfolio.getMoney()));
        priceTextview.setText(String.format("$%s", price));

        sharesEditText.requestFocus();
        sharesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    estimatedCostTextview.setText("$0.00");
                    return;
                }

                int shares = Integer.parseInt(s.toString());
                estimatedCostTextview.setText(String.format("$%s", Util.formatMoney(shares * price)));
            }
        });

        submitButton.setOnClickListener(v -> {
            //TODO: Setup submit button click listener
        });
    }
}
