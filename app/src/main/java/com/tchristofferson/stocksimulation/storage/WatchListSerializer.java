package com.tchristofferson.stocksimulation.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WatchListSerializer implements JsonSerializer<List<String>>, JsonDeserializer<List<String>> {
    @Override
    public JsonElement serialize(List<String> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray array = new JsonArray(src.size());

        for (String symbol : src) {
            array.add(symbol);
        }

        return array;
    }

    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        List<String> watchList = new ArrayList<>(array.size());

        for (JsonElement symbolElement : array) {
            watchList.add(symbolElement.getAsString());
        }

        return watchList;
    }
}
