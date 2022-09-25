package ru.pronin.pocketTrader.utils;

import ru.pronin.pocketTrader.api.entities.CustomCandle;

import java.math.BigDecimal;

public class Utils {
    public static CustomCandle getCandleWithNewCloseValue(CustomCandle candle, BigDecimal closeValue) {
        return new CustomCandle(closeValue, candle.getHigh(), candle.getLow(), candle.getOpen(), candle.getTime());
    }
}
