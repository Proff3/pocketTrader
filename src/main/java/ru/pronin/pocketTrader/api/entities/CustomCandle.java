package ru.pronin.pocketTrader.api.entities;

import com.google.protobuf.Timestamp;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.Quotation;

import java.math.BigDecimal;
import java.util.Objects;

public class CustomCandle {
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private Timestamp time;

    public CustomCandle(BigDecimal close, BigDecimal high, BigDecimal low, BigDecimal open, Timestamp time) {
        this.close = close;
        this.high = high;
        this.low = low;
        this.open = open;
        this.time = time;
    }

    public CustomCandle(Quotation close, Quotation high, Quotation low, Quotation open, Timestamp time) {
        this.close = getValueInBigDecimal(close);
        this.high = getValueInBigDecimal(high);
        this.low = getValueInBigDecimal(low);
        this.open = getValueInBigDecimal(open);
        this.time = time;
    }

    public CustomCandle(HistoricCandle candle) {
        this.close = getValueInBigDecimal(candle.getClose());
        this.high = getValueInBigDecimal(candle.getHigh());
        this.low = getValueInBigDecimal(candle.getLow());
        this.open = getValueInBigDecimal(candle.getOpen());
        this.time = candle.getTime();
    }

    public CustomCandle(Candle candle) {
        this.close = getValueInBigDecimal(candle.getClose());
        this.high = getValueInBigDecimal(candle.getHigh());
        this.low = getValueInBigDecimal(candle.getLow());
        this.open = getValueInBigDecimal(candle.getOpen());
        this.time = candle.getTime();
    }

    public CustomCandle() {}

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    private BigDecimal getValueInBigDecimal(Quotation quotation) {
        return new BigDecimal(quotation.getUnits() + "." + quotation.getNano());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomCandle)) return false;
        CustomCandle that = (CustomCandle) o;
        return Objects.equals(getClose(), that.getClose()) && Objects.equals(getHigh(), that.getHigh()) && Objects.equals(getLow(), that.getLow()) && Objects.equals(getOpen(), that.getOpen()) && Objects.equals(getTime(), that.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClose(), getHigh(), getLow(), getOpen(), getTime());
    }

    @Override
    public String toString() {
        return "CustomCandle{" +
                "close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", time=" + time +
                '}';
    }
}
