package com.kernelcrash.byte_bank.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUSDValue {
    private final String currency;

    //"0.0000100990367084"
    private final BigDecimal value;
    private BigDecimal changePercentage;

    public CurrencyUSDValue(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
        //System.out.println("CurrencyUSDValue created for " + currency + " with value " + value);
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getUnitCurrencyValueInUSD() {
        return BigDecimal.ONE.divide(value, 10, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setChangePercentage(BigDecimal changePercentage) {
        this.changePercentage = changePercentage;
    }

    public BigDecimal getChangePercentage() {
        return changePercentage;
    }
}
