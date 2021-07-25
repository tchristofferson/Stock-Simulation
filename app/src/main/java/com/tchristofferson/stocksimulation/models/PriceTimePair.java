package com.tchristofferson.stocksimulation.models;

public class PriceTimePair {

    //Can be mm/dd or similar or hh:mm or similar formats. Used as label for graph
    private final String time;
    private final double price;

    public PriceTimePair(String time, double price) {
        this.time = time;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }
}
