package com.kernelcrash.byte_bank.models;

import java.util.List;

public class Data {
    private boolean Aggregated;
    private long TimeFrom;
    private long TimeTo;
    public static List<ChartData> Data;

    public List<ChartData> getFullChartData() {
        return Data;
    }
}
