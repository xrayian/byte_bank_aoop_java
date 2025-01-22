package com.kernelcrash.byte_bank.utils;

import com.google.gson.JsonElement;
import com.kernelcrash.byte_bank.MainApplication;
import com.kernelcrash.byte_bank.models.ChartData;
import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kernelcrash.byte_bank.models.Wallet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyDataStore {
    static HashMap<String, CurrencyUSDValue> latestCurrencyPriceList = new HashMap<>();
    static HashMap<String, CurrencyUSDValue> previousCurrencyPriceList = new HashMap<>();
//    static HashMap<Date, CryptoDataFetcher.OHLCData> ohlcDataHashMap = new HashMap<>();
    static HashMap<String, HashMap<Date, CryptoDataFetcher.OHLCData>> symbolOHLCHistory = new HashMap<>();

    synchronized public static HashMap<String, CurrencyUSDValue> getLatestCurrencyPriceList() {
        return latestCurrencyPriceList;
    }

    synchronized public static void setLatestCurrencyPriceList(HashMap<String, CurrencyUSDValue> latestCurrencyPriceList) {
        previousCurrencyPriceList = CurrencyDataStore.latestCurrencyPriceList;
        CurrencyDataStore.latestCurrencyPriceList = latestCurrencyPriceList;
    }

    public static void parseCoinbaseJSONData(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject rates = jsonObject.getAsJsonObject("data").getAsJsonObject("rates");
        HashMap<String, CurrencyUSDValue> currencyList = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : rates.entrySet()) {
            String currency = entry.getKey();
            BigDecimal value = entry.getValue().getAsBigDecimal();
            CurrencyUSDValue currencyUSDValue = new CurrencyUSDValue(currency, value);

            if (previousCurrencyPriceList.containsKey(currency)) {
                BigDecimal previousValue = previousCurrencyPriceList.get(currency).getUnitCurrencyValueInUSD();
                BigDecimal changePercentage = calculateChangePercentage(previousValue, currencyUSDValue);
                currencyUSDValue.setChangePercentage(changePercentage);
            } else {
                currencyUSDValue.setChangePercentage(BigDecimal.ZERO);
            }

            currencyList.put(currency, currencyUSDValue);
        }

        setLatestCurrencyPriceList(currencyList);
    }

    private static BigDecimal calculateChangePercentage(BigDecimal previousValue, CurrencyUSDValue currencyUSDValue) {
        BigDecimal changePercentage;

        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
            // Handle division by zero case
            changePercentage = BigDecimal.ZERO;
        } else {
            changePercentage = currencyUSDValue.getUnitCurrencyValueInUSD()
                    .subtract(previousValue)
                    .divide(previousValue, 10, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
        }
        return changePercentage;
    }

    public static HashMap<String, CurrencyUSDValue> getPreviousCurrencyPriceList() {
        return previousCurrencyPriceList;
    }

    synchronized public static HashMap<Date, CryptoDataFetcher.OHLCData> getOHLCMap(String cryptoSymbol) {
        return symbolOHLCHistory.get(cryptoSymbol);
    }

    synchronized public static void setOHLCMap(String cryptoSymbol, HashMap<Date, CryptoDataFetcher.OHLCData> cryptoOHLCDataHashMap) {
        symbolOHLCHistory.put(cryptoSymbol, cryptoOHLCDataHashMap);
    }

//    synchronized private static void setOHLCMap(HashMap<Date, CryptoDataFetcher.OHLCData> ohlcDataHashMap) {
//        CurrencyDataStore.ohlcDataHashMap = ohlcDataHashMap;
//    }

    public static void setChartData(List<ChartData> chartData) {
        //todo check later
//        HashMap<Date, CryptoDataFetcher.OHLCData> ohlcData = new HashMap<>();
//        for (ChartData data : chartData) {
//            ohlcData.put(data.getDate(), new CryptoDataFetcher.OHLCData(String.valueOf(data.time), data.getOpen(), data.getHigh(), data.getLow(), data.getClose()));
//        }
//
//        setOHLCMap(ohlcData);
    }

    public static String getTotalPortfolioValue() {
        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        if (latestCurrencyPriceList.isEmpty()) {
            return "Loading...";
        }
        for (CurrencyUSDValue currencyUSDValue : latestCurrencyPriceList.values()) {
            totalPortfolioValue = totalPortfolioValue.add(currencyUSDValue.getUnitCurrencyValueInUSD());
        }
        return totalPortfolioValue.toString();
    }

    public static String getUserPortfolioValue() {
        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        if (latestCurrencyPriceList.isEmpty()) {
            return "Loading...";
        }
        for (CurrencyUSDValue currencyUSDValue : latestCurrencyPriceList.values()) {
            for (Wallet wallet : MainApplication.stateManager.getCurrentUser().getWallets()) {
                if (currencyUSDValue.getCurrency().equals(wallet.getCryptoType())) {
                    totalPortfolioValue = totalPortfolioValue.add(currencyUSDValue.getUnitCurrencyValueInUSD().multiply(BigDecimal.valueOf(wallet.getBalance())));
                }
            }
        }
        //return 3 decimal places
        return totalPortfolioValue.setScale(3, RoundingMode.HALF_UP).toString();
    }

    public static void clearData() {
        latestCurrencyPriceList.clear();
        previousCurrencyPriceList.clear();
        symbolOHLCHistory.clear();
    }
}
