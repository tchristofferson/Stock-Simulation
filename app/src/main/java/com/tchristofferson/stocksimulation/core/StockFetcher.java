package com.tchristofferson.stocksimulation.core;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.PriceTimePair;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class StockFetcher {

    //Placeholders
    private static final String SYMBOL = "%symbol%";
    private static final String SYMBOLS = "%symbols%";
    private static final String TIME = "%time%";

    private static final String API_URL = "https://cloud.iexapis.com/stable/stock/";
    private static final String API_KEY = "pk_2ab3428a78ff4baa8f2802817d6e1d00";
    private static final String API_KEY_STRING = "?token=" + API_KEY;
    private static final String HISTORICAL_URL = API_URL + SYMBOL + "/chart/" + TIME + API_KEY_STRING;
    private static final String DAY_PRICES_URL = API_URL + SYMBOL + "/intraday-prices/chartIEXOnly" + API_KEY_STRING;
    private static final String QUOTE_URL = API_URL + SYMBOL + "/quote" + API_KEY_STRING;
    private final String QUOTE_MULTIPLE_URL = API_URL + "market/batch?symbols=" + SYMBOLS + "&types=quote&token=" + API_KEY;

    public List<StockInfo> fetchStockInfo(String... symbols) throws IOException {
        if (symbols.length == 0)
            throw new IllegalArgumentException("Must provide 1 or more symbols!");

        List<StockInfo> stockInfoList = new ArrayList<>(symbols.length);
        JsonElement jsonElement = getJson(QUOTE_MULTIPLE_URL, null, symbols);
        JsonObject obj = jsonElement.getAsJsonObject();

        for (String symbol : obj.keySet()) {
            JsonObject quote = obj.getAsJsonObject(symbol).getAsJsonObject("quote");
            String companyName = quote.get("companyName").getAsString();
            double price = quote.get("latestPrice").getAsDouble();
            stockInfoList.add(new StockInfo(symbol, companyName, price));
        }

        return stockInfoList;
    }

    public List<PriceTimePair> fetchPriceHistory(String symbol, TimeFrame timeFrame) throws IOException {
        //Need to fetch today's price because it isn't included in historical data
        boolean isOneDay = timeFrame == TimeFrame.ONE_DAY || timeFrame == TimeFrame.LATEST;
        JsonElement jsonElement = getJson(isOneDay ? DAY_PRICES_URL : HISTORICAL_URL, timeFrame, symbol);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<PriceTimePair> pairs = new ArrayList<>(jsonArray.size());

        if (timeFrame == TimeFrame.LATEST) {
            JsonObject obj = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
            String label = obj.get("label").getAsString();
            double price = obj.get("close").getAsDouble();
            pairs.add(new PriceTimePair(label, price));
        } else {
            Log.d("StockSimulation", "array size: " + jsonArray.size());

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();

                JsonElement labelElement = obj.get("label");
                JsonElement priceElement = obj.get("close");
                
                if (priceElement.isJsonNull())
                    continue;

                String label = labelElement.getAsString();
                double price = priceElement.getAsDouble();

                pairs.add(new PriceTimePair(label, price));
            }
        }

        return pairs;
    }

    JsonElement getJson(String urlString, TimeFrame timeFrame, String... symbols) throws IOException {
        if (symbols.length == 0)
            throw new IllegalArgumentException("Must specify at least 1 symbol!");

        urlString = urlString.replace(SYMBOL, Util.formatSymbol(symbols[0]));
        //String.join() not available in API 21
        urlString = urlString.replace(SYMBOLS, Util.joinStrings(",", symbols));

        if (timeFrame != null)
            urlString = urlString.replace(TIME, timeFrame.toString());

        URL url = new URL(urlString);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonElement jsonElement = JsonParser.parseReader(reader);
        reader.close();

        return jsonElement;
    }

}
