package com.example.adventurelibertarian.utils;

import java.util.HashMap;
import java.util.Map;

public class CurrencyUtil {
    public static Map<String, Integer> currencies = new HashMap<>();
    public static Map<Integer, String> reverseCurrencies = new HashMap<>();

    public static void initCurrencies() {
        currencies.put("", 0);
        currencies.put("K", 3);
        currencies.put("M", 6);
        currencies.put("B", 9);
        currencies.put("T", 12);
        currencies.put("Qua", 15);
        currencies.put("Qui", 18);
        currencies.put("Sex", 21);
        currencies.put("Sep", 24);
        currencies.put("O", 27);
        currencies.put("N", 30);
        currencies.put("D", 33);
        currencies.put("Und", 36);
        currencies.put("Duod", 39);
        currencies.put("Tred", 42);

        reverseCurrencies.put(0,"");
        reverseCurrencies.put(3,"K");
        reverseCurrencies.put(6, "M");
        reverseCurrencies.put(9, "B");
        reverseCurrencies.put(12, "T");
        reverseCurrencies.put(15, "Quad");
        reverseCurrencies.put(18, "Quin");
        reverseCurrencies.put(21, "Sext");
        reverseCurrencies.put(24, "Sept");
        reverseCurrencies.put(27, "O");
        reverseCurrencies.put(30, "N");
        reverseCurrencies.put(33, "D");
        reverseCurrencies.put(36, "Und");
        reverseCurrencies.put(39, "Duod");
        reverseCurrencies.put(42, "Tred");
    }
}
