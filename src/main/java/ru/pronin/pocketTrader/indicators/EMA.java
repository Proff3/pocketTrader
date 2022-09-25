package ru.pronin.pocketTrader.indicators;

import com.google.protobuf.Timestamp;
import ru.pronin.pocketTrader.api.entities.CustomCandle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EMA implements Indicator {

    private final List<CustomCandle> listOfCandles = new ArrayList<>();
    private final int timePeriod;
    private final int depth;
    private final BigDecimal alpha;

    private BigDecimal currentValue = BigDecimal.ZERO.setScale(5,RoundingMode.HALF_UP);
    private BigDecimal previousValue = BigDecimal.ZERO.setScale(5,RoundingMode.HALF_UP);
    private boolean isOver;

    public EMA(int timePeriod) {
        this.timePeriod = timePeriod;
        alpha = BigDecimal.valueOf(2.0 / (timePeriod + 1));
        depth = timePeriod * 5;
    }

    @Override
    public void addCandle(CustomCandle currentCandle){
        if (listOfCandles.isEmpty()) {
            currentValue = currentCandle.getClose();
            listOfCandles.add(currentCandle);
            return;
        }
        CustomCandle lastCandle = listOfCandles.get(listOfCandles.size() - 1);
        if (lastCandle.getTime().getSeconds() == currentCandle.getTime().getSeconds()) {
            listOfCandles.remove(lastCandle);
            currentValue = previousValue;
        } else {
            previousValue = currentValue;
        }
        listOfCandles.add(currentCandle);
        if(timePeriod >= listOfCandles.size()){
            Double currentValueInDouble = listOfCandles
                    .stream()
                    .map(candle -> candle.getClose().doubleValue())
                    .collect(Collectors.averagingDouble(currentCandleDouble -> currentCandleDouble));
            currentValue = BigDecimal.valueOf(currentValueInDouble);
            previousValue = currentValue;
        } else {
            BigDecimal alphaValue = currentCandle.getClose().multiply(alpha).setScale(5,RoundingMode.HALF_UP);
            BigDecimal candleValue = currentValue.multiply(BigDecimal.ONE.subtract(alpha)).setScale(5,RoundingMode.HALF_UP);
            currentValue = alphaValue.add(candleValue);
        }
        if (listOfCandles.size() >= depth) listOfCandles.remove(0);
    }

    @Override
    public Boolean isEnoughInformation() {
        return listOfCandles.size() > timePeriod;
    }

    @Override
    public Boolean isOver() {
        return isOver;
    }

    @Override
    public int getIndicatorDepth() {
        return depth;
    }

    @Override
    public void setOver() {
        isOver = true;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }
}
