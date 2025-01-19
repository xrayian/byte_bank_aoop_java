package com.kernelcrash.byte_bank.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BinanceDataFetcher {
    public static List<OHLCData> fetchBinanceData(String symbol, String interval, int limit) throws Exception {
        String apiUrl = "https://api.binance.com/api/v3/klines?symbol=" + symbol + "&interval=" + interval + "&limit=" + limit;

        HttpClientHelper httpClientHelper = new HttpClientHelper();
        String response = httpClientHelper.sendGet(apiUrl, null);
        JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();

        List<OHLCData> data = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonArray entry = jsonArray.get(i).getAsJsonArray();
            String time = entry.get(0).getAsString();
            BigDecimal open = new BigDecimal(entry.get(1).getAsString());
            BigDecimal high = new BigDecimal(entry.get(2).getAsString());
            BigDecimal low = new BigDecimal(entry.get(3).getAsString());
            BigDecimal close = new BigDecimal(entry.get(4).getAsString());
            data.add(new OHLCData(time, open, high, low, close));
        }

        return data;
    }

    public static class OHLCData {
        public final String time;
        public final Date date;
        public final BigDecimal open, high, low, close;

        public OHLCData(String time, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
            this.time = time;
            date = new Date(Long.parseLong(time));
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
        }
    }
}
