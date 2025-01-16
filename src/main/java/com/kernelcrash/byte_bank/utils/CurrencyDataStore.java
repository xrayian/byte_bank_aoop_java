package com.kernelcrash.byte_bank.utils;

import com.google.gson.JsonElement;
import com.kernelcrash.byte_bank.models.CurrencyUSDValue;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataStore {
    static HashMap<String, CurrencyUSDValue> currencyList = new HashMap<>();

    synchronized public static HashMap<String, CurrencyUSDValue> getCurrencyList() {
        return currencyList;
    }

    synchronized public static void setCurrencyList(HashMap<String, CurrencyUSDValue> currencyList) {
        CurrencyDataStore.currencyList = currencyList;
//        System.out.println("Currency list set");
    }

    public static void parseCoinbaseJSONData(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonObject rates = jsonObject.getAsJsonObject("data").getAsJsonObject("rates");
        HashMap<String, CurrencyUSDValue> currencyList = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : rates.entrySet()) {
            //System.out.println("Key: `" + entry.getKey() + "` | Value: `" + entry.getValue() + "`");
            String currency = entry.getKey();
            BigDecimal value = entry.getValue().getAsBigDecimal();
            CurrencyUSDValue currencyUSDValue = new CurrencyUSDValue(currency, value);
            currencyList.put(currency, currencyUSDValue);
            //System.out.println(currencyList.get(currency).getCurrency() + " : " + currencyList.get(currency).getValue());
//            System.out.println("Currency list length: " + currencyList.size());
        }
//        System.out.println("Currency list length: " + getCurrencyList().size());
        setCurrencyList(currencyList);
    }


}

