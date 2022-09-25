package ru.pronin.pocketTrader.indicators;

import com.google.protobuf.Timestamp;
import ru.pronin.pocketTrader.api.entities.CustomCandle;
import ru.pronin.pocketTrader.indicators.utils.ScalableMap;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.BinaryOperator;

public class BollingerBands implements Indicator {

    private final ScalableMap<Instant, CustomCandle> candleMap;
    private final int DEPTH;
    private final BigDecimal MULTIPLIER;
    private SMA ML;
    private BigDecimal HL;
    private BigDecimal LL;
    private boolean isOver;

    public BollingerBands(int depth, int multiplier) {
        this.candleMap = new ScalableMap<>(depth);
        MULTIPLIER = new BigDecimal(multiplier);
        ML = new SMA(depth);
        DEPTH = depth;
    }

    @Override
    public void addCandle(CustomCandle candle) {
        candleMap.addValue(Instant.ofEpochSecond(candle.getTime().getSeconds()), candle);
        ML.addCandle(candle);
        calculateCurrentValues();
    }

    @Override
    public Boolean isEnoughInformation() {
        return candleMap.getSize() >= DEPTH && ML.isEnoughInformation();
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

    public Instant getInstant() {
        return ML.getCurrentValue().getLeft();
    }

    public BigDecimal getMLValue() {
        return ML.getCurrentValue().getRight();
    }

    public BigDecimal getHLValue() {
        return HL;
    }

    public BigDecimal getLLValue() {
        return LL;
    }

    private void calculateCurrentValues() {
        BigDecimal productOfMultiplication = getSum()
                .divide(new BigDecimal(DEPTH), 4, RoundingMode.HALF_UP)
                .sqrt(MathContext.DECIMAL64)
                .multiply(MULTIPLIER);
        HL = ML.getCurrentValue().getRight().add(productOfMultiplication).setScale(4, RoundingMode.HALF_UP);
        LL = ML.getCurrentValue().getRight().subtract(productOfMultiplication).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal getSum() {
        return candleMap.getValues().stream()
                .map(CustomCandle::getClose)
                .reduce(BigDecimal.ZERO, squaredSumOperator)
                .setScale(4, RoundingMode.HALF_UP);
    }

    private final BinaryOperator<BigDecimal> squaredSumOperator = (acc, value) -> {
        BigDecimal squaredSum = value.subtract(ML.getCurrentValue().getRight()).setScale(4, RoundingMode.HALF_UP).pow(2);
        return acc.add(squaredSum);
    };
}
