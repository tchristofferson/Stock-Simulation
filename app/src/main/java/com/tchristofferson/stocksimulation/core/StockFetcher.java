package com.tchristofferson.stocksimulation.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tchristofferson.stocksimulation.Util;
import com.tchristofferson.stocksimulation.models.StockInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class StockFetcher {

    //Placeholders
    private static final String SYMBOL = "%symbol%";
    private static final String TIME = "%time%";

    private static final String API_URL = "https://cloud.iexapis.com/stable/stock/";
    private static final String API_KEY = "pk_2ab3428a78ff4baa8f2802817d6e1d00";
    private static final String API_KEY_STRING = "?token=" + API_KEY;
    private static final String HISTORICAL_URL = API_URL + SYMBOL + "/chart/" + TIME + API_KEY_STRING;
    private static final String DAY_PRICES_URL = API_URL + SYMBOL + "/intraday-prices/chartIEXOnly" + API_KEY_STRING;
    private static final String QUOTE_URL = API_URL + SYMBOL + "/quote" + API_KEY_STRING;

    public StockInfo fetchStockInfo(String symbol) throws IOException {
        JsonElement jsonElement = getJson(QUOTE_URL, symbol, null);
        JsonObject obj = jsonElement.getAsJsonObject();
        String companyName = obj.get("companyName").getAsString();
        double latestPrice = obj.get("latestPrice").getAsDouble();

        return new StockInfo(symbol, companyName, latestPrice);
    }

    public String fetchCompanyName(String symbol) throws IOException {
        JsonElement jsonElement = getJson(QUOTE_URL, symbol, null);
        return jsonElement.getAsJsonObject().get("companyName").getAsString();
    }

    public double fetchPrice(String symbol) throws IOException {
        JsonElement jsonElement = getJson(QUOTE_URL, symbol, null);
        return jsonElement.getAsJsonObject().get("latestPrice").getAsDouble();
    }

    //Returns LinkedHashMap to maintain insertion order
    public Map<String, Double> fetchPriceHistory(String symbol, TimeFrame timeFrame) throws IOException {
        //Need to fetch today's price because it isn't included in historical data
        boolean isOneDay = timeFrame == TimeFrame.ONE_DAY || timeFrame == TimeFrame.LATEST;
        JsonElement jsonElement = getJson(isOneDay ? DAY_PRICES_URL : HISTORICAL_URL, symbol, timeFrame);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Map<String, Double> priceHistory = new LinkedHashMap<>(jsonArray.size());

        if (timeFrame == TimeFrame.LATEST) {
            JsonObject obj = jsonArray.get(jsonArray.size() - 1).getAsJsonObject();
            String label = obj.get("label").getAsString();
            double price = obj.get("close").getAsDouble();
            priceHistory.put(label, price);
        } else {
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String label = obj.get("label").getAsString();
                double price = obj.get("close").getAsDouble();

                priceHistory.put(label, price);
            }
        }

        return priceHistory;
    }

    private JsonElement getJson(String urlString, String symbol, TimeFrame timeFrame) throws IOException {
        urlString = urlString.replace(SYMBOL, Util.formatSymbol(symbol));

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
