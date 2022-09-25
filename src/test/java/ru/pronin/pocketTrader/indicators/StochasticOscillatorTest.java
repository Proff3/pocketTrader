package ru.pronin.pocketTrader.indicators;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pronin.pocketTrader.api.entities.CustomCandle;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class StochasticOscillatorTest {

    private StochasticOscillator so;
    private long i = 0;

    Function<BigDecimal, CustomCandle> createCandle = (value) -> {
        CustomCandle candle = new CustomCandle();
        candle.setClose(value);
        candle.setHigh(value.add(BigDecimal.TEN));
        candle.setLow(value.subtract(BigDecimal.TEN));
        candle.setTime(Timestamp.newBuilder().setSeconds(OffsetDateTime.now().plusSeconds(i++).toEpochSecond()).build());
        return candle;
    };

    @BeforeEach
    void init() {
        so = new StochasticOscillator(14, 3);
    }

    @Test
    void addCandle() {
        so.addCandle(createCandle.apply(new BigDecimal(100)));
        so.addCandle(createCandle.apply(new BigDecimal(200)));
        so.addCandle(createCandle.apply(new BigDecimal(300)));
        so.addCandle(createCandle.apply(new BigDecimal(400)));
        so.addCandle(createCandle.apply(new BigDecimal(500)));
        so.addCandle(createCandle.apply(new BigDecimal(100)));
        so.addCandle(createCandle.apply(new BigDecimal(200)));
        so.addCandle(createCandle.apply(new BigDecimal(300)));
        so.addCandle(createCandle.apply(new BigDecimal(400)));
        so.addCandle(createCandle.apply(new BigDecimal(500)));
        so.addCandle(createCandle.apply(new BigDecimal(100)));
        so.addCandle(createCandle.apply(new BigDecimal(200)));
        so.addCandle(createCandle.apply(new BigDecimal(300)));
        so.addCandle(createCandle.apply(new BigDecimal(400)));
        so.addCandle(createCandle.apply(new BigDecimal(500)));
        assertEquals(new BigDecimal("97.6200"), so.getValue().getRight());
        assertEquals(new BigDecimal("73.8100"), so.getEmaValue().getRight());
    }
}