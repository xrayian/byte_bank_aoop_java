package com.kernelcrash.byte_bank.models;

import java.math.BigDecimal;

public class CurrencyUSDValue {
    private final String currency;

    //"0.0000100990367084"
    private final BigDecimal value;

    public CurrencyUSDValue(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
        //System.out.println("CurrencyUSDValue created for " + currency + " with value " + value);
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

}
