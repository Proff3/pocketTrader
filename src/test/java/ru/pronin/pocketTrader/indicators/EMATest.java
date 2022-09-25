package ru.pronin.pocketTrader.indicators;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import ru.pronin.pocketTrader.api.entities.CustomCandle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EMATest {

    int i = 0;

    Function<BigDecimal, CustomCandle> createCandle = closeValue -> {
        CustomCandle candle = new CustomCandle();
        candle.setClose(closeValue);
        candle.setTime(Timestamp.newBuilder().setSeconds(OffsetDateTime.now().plusMinutes(i++).toEpochSecond()).build());
        return candle;
    };

    @Test
    public void getCurrentValueByAveragingCandlesLowerThanDepth(){
        EMA ema = new EMA(3);
        ema.addCandle(createCandle.apply(BigDecimal.ONE));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(11)));
        assertEquals(BigDecimal.valueOf(6).setScale(1, RoundingMode.HALF_UP), ema.getCurrentValue());
    }

    @Test
    public void getCurrentValueByAveragingCandles(){
        EMA ema = new EMA(3);
        ema.addCandle(createCandle.apply(BigDecimal.ONE));
        ema.addCandle(createCandle.apply(BigDecimal.TEN));
        ema.addCandle(createCandle.apply(BigDecimal.ONE));
        assertEquals(BigDecimal.valueOf(4).setScale(1, RoundingMode.HALF_UP), ema.getCurrentValue());
    }

    @Test
    public void getCurrentValueByEMAStrategy(){
        EMA ema = new EMA(4);
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(5.3)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(6.7)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(7.9)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(7.1)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(5.2)));
        assertEquals(BigDecimal.valueOf(6.13), ema.getCurrentValue().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void getCurrentAverageValueByAddingCandlesWithSameTime(){
        EMA ema = new EMA(3);
        ema.addCandle(createCandle.apply(BigDecimal.ONE));
        ema.addCandle(createCandle.apply(BigDecimal.TEN));
        CustomCandle cc = createCandle.apply(BigDecimal.TEN);
        ema.addCandle(cc);
        cc.setClose(BigDecimal.ONE);
        ema.addCandle(cc);
        assertEquals(BigDecimal.valueOf(4).setScale(1, RoundingMode.HALF_UP), ema.getCurrentValue());
    }

    @Test
    public void getCurrentEMAValueByAddingCandlesWithSameTime(){
        EMA ema = new EMA(4);
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(5.3)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(6.7)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(7.9)));
        ema.addCandle(createCandle.apply(BigDecimal.valueOf(7.1)));
        CustomCandle cc = createCandle.apply(BigDecimal.valueOf(10.5));
        ema.addCandle(cc);
        cc.setClose(BigDecimal.valueOf(5.2));
        ema.addCandle(cc);
        assertEquals(BigDecimal.valueOf(6.13), ema.getCurrentValue().setScale(2, RoundingMode.HALF_UP));
    }
}