package ru.pronin.pocketTrader.indicators;

import ru.pronin.pocketTrader.api.entities.CustomCandle;
import ru.pronin.pocketTrader.indicators.utils.Pair;
import ru.pronin.pocketTrader.indicators.utils.ScalableMap;
import ru.pronin.pocketTrader.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class StochasticOscillator implements Indicator {

    private final int DEPTH;
    private final SMA SMA;
    private final BigDecimal MULTIPLIER = new BigDecimal(100);

    private BigDecimal max;
    private BigDecimal min;
    private Pair<Instant, BigDecimal> value;
    private final ScalableMap<Instant, CustomCandle> candleMap;
    private boolean isOver = false;
    private final ScalableMap<Instant, BigDecimal> numerators;
    private final ScalableMap<Instant, BigDecimal> denominators;

    public StochasticOscillator(int depth, int emaDepth, int smooth) {
        DEPTH = depth;
        SMA = new SMA(emaDepth);
        candleMap = new ScalableMap<>(depth);
        numerators = new ScalableMap<>(smooth);
        denominators = new ScalableMap<>(smooth);
    }

    public StochasticOscillator(int depth, int emaDepth) {
        DEPTH = depth;
        SMA = new SMA(emaDepth);
        candleMap = new ScalableMap<>(depth);
        numerators = new ScalableMap<>(1);
        denominators = new ScalableMap<>(1);
    }

    @Override
    public void addCandle(CustomCandle candle) {
        if(max == null || min == null) {
            updateLimits(candle);
            calculate(candle);
        } else {
            calculate(candle);
        }
    }

    @Override
    public Boolean isEnoughInformation() {
        return candleMap.getSize() >= DEPTH && SMA.isEnoughInformation();
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

    private void calculate(CustomCandle candle) {
        candleMap.addValue(Instant.ofEpochSecond(candle.getTime().getSeconds()), candle);
        updateLimits(candle);
        value = new Pair<>(Instant.ofEpochSecond(candle.getTime().getSeconds()), calculateCurrentValue(candle));
        CustomCandle candleWithCalculatedValue = Utils.getCandleWithNewCloseValue(candle, value.getRight());
        SMA.addCandle(candleWithCalculatedValue);
    }

    private void updateLimits(CustomCandle candle) {
        BigDecimal currentMax = candleMap.getValues().stream().map(CustomCandle::getHigh).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal currentMin = candleMap.getValues().stream().map(CustomCandle::getLow).min(BigDecimal::compareTo).orElse(new BigDecimal(1_000_000));
        max = candle.getHigh().compareTo(currentMax) > 0 ? candle.getHigh() : currentMax;
        min = candle.getLow().compareTo(currentMin) < 0 ? candle.getLow() : currentMin;
    }

    private BigDecimal calculateCurrentValue(CustomCandle candle) {
        BigDecimal numerator = candle.getClose().subtract(min);
        numerators.addValue(Instant.ofEpochSecond(candle.getTime().getSeconds()), numerator);
        BigDecimal numeratorSmoothValue = numerators.getValues().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal denominator = max.compareTo(min) == 0 ? BigDecimal.ONE : max.subtract(min);
        denominators.addValue(Instant.ofEpochSecond(candle.getTime().getSeconds()), denominator);
        BigDecimal denominatorSmoothValue = denominators.getValues().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return MULTIPLIER.multiply(numeratorSmoothValue.divide(denominatorSmoothValue, 4, RoundingMode.HALF_UP));
    }

    public Pair<Instant, BigDecimal> getValue() {
        return value;
    }

    public Pair<Instant, BigDecimal> getEmaValue() {
        return SMA.getCurrentValue();
    }
}
