package com.tchristofferson.stocksimulation.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tchristofferson.stocksimulation.models.Portfolio;
import com.tchristofferson.stocksimulation.models.Stock;
import com.tchristofferson.stocksimulation.models.Transaction;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class PortfolioSerializer implements JsonSerializer<Portfolio>, JsonDeserializer<Portfolio> {

    @Override
    public JsonElement serialize(Portfolio src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("money", src.getMoney());

        JsonObject transactionsObj = new JsonObject();
        for (Stock stock : src.getStocks()) {
            List<Transaction> transactions = stock.getTransactions();
            JsonArray transactionArray = new JsonArray(transactions.size());

            for (Transaction transaction : transactions) {
                JsonObject transactionObj = new JsonObject();

                transactionObj.addProperty("timeOfPurchase", transaction.getTimeOfPurchase());
                transactionObj.addProperty("transactionType", transaction.getType() == Transaction.Type.BUY ? 0 : 1);
                transactionObj.addProperty("shares", transaction.getShares());
                transactionObj.addProperty("soldShares", transaction.getSoldShares());
                transactionObj.addProperty("pricePerShare", transaction.getPricePerShare());

                transactionArray.add(transactionObj);
            }

            transactionsObj.add(stock.getSymbol(), transactionArray);
        }

        obj.add("transactions", transactionsObj);
        return obj;
    }

    @Override
    public Portfolio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Stock> stocks = new LinkedList<>();
        JsonObject obj = json.getAsJsonObject();
        double money = obj.get("money").getAsDouble();

        JsonObject transactionsObj = obj.get("transactions").getAsJsonObject();
        for (String symbol : transactionsObj.keySet()) {
            JsonArray transactionArray = transactionsObj.get(symbol).getAsJsonArray();

            List<Transaction> transactions = new LinkedList<>();
            for (JsonElement transactionElement : transactionArray) {
                JsonObject transactionObj = transactionElement.getAsJsonObject();

                long timeOfPurchase = transactionObj.get("timeOfPurchase").getAsLong();
                int transactionTypeInt = transactionObj.get("transactionType").getAsInt();
                Transaction.Type type = transactionTypeInt == 0 ? Transaction.Type.BUY : Transaction.Type.SELL;
                int shares = transactionObj.get("shares").getAsInt();
                int soldShares = transactionObj.get("soldShares").getAsInt();
                double pricePerShare = transactionObj.get("pricePerShare").getAsDouble();

                transactions.add(new Transaction(symbol, type, timeOfPurchase, shares, pricePerShare, soldShares));
            }

            stocks.add(new Stock(symbol, transactions));
        }

        return new Portfolio(money, stocks);
    }
}
