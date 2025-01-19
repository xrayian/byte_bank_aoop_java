package com.kernelcrash.byte_bank.utils;

import com.google.gson.JsonElement;
import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataStore {
    static HashMap<String, CurrencyUSDValue> currencyList = new HashMap<>();
    static HashMap<String, CurrencyUSDValue> previousCurrencyList = new HashMap<>();

    synchronized public static HashMap<String, CurrencyUSDValue> getCurrencyList() {
        return currencyList;
    }

    synchronized public static void setCurrencyList(HashMap<String, CurrencyUSDValue> currencyList) {
        previousCurrencyList = CurrencyDataStore.currencyList;
        CurrencyDataStore.currencyList = currencyList;
    }

    public static void parseCoinbaseJSONData(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject rates = jsonObject.getAsJsonObject("data").getAsJsonObject("rates");
        HashMap<String, CurrencyUSDValue> currencyList = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : rates.entrySet()) {
            String currency = entry.getKey();
            BigDecimal value = entry.getValue().getAsBigDecimal();
            CurrencyUSDValue currencyUSDValue = new CurrencyUSDValue(currency, value);

            if (previousCurrencyList.containsKey(currency)) {
                BigDecimal previousValue = previousCurrencyList.get(currency).getUnitCurrencyValueInUSD();
                BigDecimal changePercentage = calculateChangePercentage(previousValue, currencyUSDValue);
                currencyUSDValue.setChangePercentage(changePercentage);
            } else {
                currencyUSDValue.setChangePercentage(BigDecimal.ZERO);
            }

            currencyList.put(currency, currencyUSDValue);
        }

        setCurrencyList(currencyList);
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


    public static HashMap<String, CurrencyUSDValue> getPreviousCurrencyList() {
        return previousCurrencyList;
    }
}
