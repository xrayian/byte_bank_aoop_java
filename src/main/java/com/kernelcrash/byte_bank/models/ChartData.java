package com.kernelcrash.byte_bank.models;

import java.math.BigDecimal;
import java.util.Date;

public class ChartData {
    public long time;
    public BigDecimal high;
    public BigDecimal low;
    public BigDecimal open;
    public BigDecimal volumefrom;
    public BigDecimal volumeto;
    public BigDecimal close;
    public String conversionType;
    public String conversionSymbol;

    public Date getDate() {
        return new Date(time * 1000);
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getVolumefrom() {
        return volumefrom;
    }

    public BigDecimal getVolumeto() {
        return volumeto;
    }

}
