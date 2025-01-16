package com.kernelcrash.byte_bank.models;

import java.io.Serializable;

public class Currency implements Serializable {
    private String name;
    private String symbol;
    private double value;
    static Currency baseCurrency = new Currency("USD", "$", 1.0);

    public Currency(String name, String symbol, double value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getValue() {
        return value;
    }

    public double convertToBase(double amount) {
        return amount / value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
