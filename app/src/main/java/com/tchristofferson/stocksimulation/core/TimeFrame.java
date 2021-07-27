package com.tchristofferson.stocksimulation.core;

public enum TimeFrame {

    LATEST("latest"),
    ONE_DAY("5dm"),
    ONE_WEEK("5d"),
    ONE_MONTH("1m"),
    THREE_MONTHS("3m"),
    ONE_YEAR("1y"),
    FIVE_YEARS("5y");

    private final String value;

    TimeFrame(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
