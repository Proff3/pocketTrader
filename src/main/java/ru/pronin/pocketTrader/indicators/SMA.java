package ru.pronin.pocketTrader.indicators;

import ru.pronin.pocketTrader.api.entities.CustomCandle;
import ru.pronin.pocketTrader.indicators.utils.Pair;
import ru.pronin.pocketTrader.indicators.utils.ScalableMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

public class SMA implements Indicator {

    private final int DEPTH;
    private final ScalableMap<Instant, CustomCandle> candleMap;

    private Pair<Instant, BigDecimal> currentValue;
    private boolean isOver;

    public SMA(int depth) {
        DEPTH = depth;
        candleMap = new ScalableMap<>(depth);
    }

    @Override
    public void addCandle(CustomCandle candle) {
        candleMap.addValue(Instant.ofEpochSecond(candle.getTime().getSeconds()), candle);
        double currentValueInDouble = candleMap.getValues()
                .stream()
                .collect(Collectors.averagingDouble(c -> c.getClose().doubleValue()));
        BigDecimal value = new BigDecimal(currentValueInDouble).setScale(4, RoundingMode.HALF_UP);
        currentValue = new Pair<>(Instant.ofEpochSecond(candle.getTime().getSeconds()), value);
    }

    @Override
    public Boolean isEnoughInformation() {
        return candleMap.getSize() > DEPTH;
    }

    @Override
    public Boolean isOver() {
        return isOver;
    }

    @Override
    public int getIndicatorDepth() {
        return DEPTH;
    }

    @Override
    public void setOver() {
        isOver = true;
    }

    public Pair<Instant, BigDecimal> getCurrentValue() {
        return currentValue;
    }
}
