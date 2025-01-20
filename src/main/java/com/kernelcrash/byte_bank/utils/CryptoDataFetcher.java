package com.kernelcrash.byte_bank.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kernelcrash.byte_bank.models.ApiResponse;
import com.kernelcrash.byte_bank.models.ChartData;

import java.math.BigDecimal;
import java.util.*;

public class CryptoDataFetcher {
    public static void fetchHistoricalData(String fsymbol, String tosymbol, int aggregate, int limit) throws Exception {
        String apiUrl = ConfigHelper.BACKEND_API_URL + "crypto-data/getKlines?fsymbol=" + fsymbol + "&tosymbol=" + tosymbol + "&aggregate=" + aggregate + "&limit=" + limit;

        HttpClientHelper httpClientHelper = new HttpClientHelper();
        String response = httpClientHelper.sendGet(apiUrl, null);
        if (response == null) {
            System.err.println("Failed to fetch data from Backend API");
            return;
        }

        System.out.println("Received data from Backend API");

        //parse the JSON response

        Gson gson = new Gson();
        List responseObj = gson.fromJson(response, List.class);

        if (responseObj.isEmpty() || responseObj == null) {

            System.err.println("Failed to parse JSON response");
            return;
        }


//        List<OHLCData> ohlcDataList = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JsonArray data = jsonArray.get(i).getAsJsonArray();
//            String time = data.get(0).getAsString();
//            BigDecimal open = data.get(1).getAsBigDecimal();
//            BigDecimal high = data.get(2).getAsBigDecimal();
//            BigDecimal low = data.get(3).getAsBigDecimal();
//            BigDecimal close = data.get(4).getAsBigDecimal();
//            OHLCData ohlcData = new OHLCData(time, open, high, low, close);
//            ohlcDataList.add(ohlcData);
//        }
//
//        //populate the CurrencyDataStore with the data
//        CurrencyDataStore.setOHLCMap((HashMap<Date, OHLCData>) ohlcDataList);


        //populate the CurrencyDataStore with the data
        HashMap<Date, OHLCData> ohlcDataHashMap = new HashMap<>();
        if (!responseObj.isEmpty() && responseObj != null) {
            responseObj.forEach(item -> {
                ChartData chartData = gson.fromJson(gson.toJson(item), ChartData.class);
                OHLCData ohlcData = new OHLCData(String.valueOf(chartData.time), chartData.open, chartData.high, chartData.low, chartData.close);
                ohlcDataHashMap.put(ohlcData.date, ohlcData);
            });

            CurrencyDataStore.setOHLCMap(ohlcDataHashMap);


        }
    }

    public static class OHLCData {
        public final String time;
        public final Date date;
        public final BigDecimal open, high, low, close;

        public OHLCData(String time, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
            this.time = time;
            date = new Date(Long.parseLong(time)*1000);
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
        }
    }
}
