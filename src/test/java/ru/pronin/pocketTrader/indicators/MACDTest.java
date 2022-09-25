package ru.pronin.pocketTrader.indicators;

import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.pronin.pocketTrader.api.entities.CustomCandle;
import ru.pronin.pocketTrader.exceptions.WrongStrategyInitializationException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class MACDTest {

    private MACD indicator;
    long i = 0;

    Function<BigDecimal, CustomCandle> createCandle = (value) -> {
        CustomCandle candle = new CustomCandle();
        candle.setClose(value);
        candle.setTime(Timestamp.newBuilder().setSeconds(OffsetDateTime.now().plusSeconds(i++).toEpochSecond()).build());
        return candle;
    };

    @BeforeEach
    void init() throws WrongStrategyInitializationException {
        indicator = new MACD(
                new EMA(2),     //fast
                new EMA(4),     //slow
                new EMA(3)      //signal
                );
    }

    @Test
    void addCandle() {
        indicator.addCandle(createCandle.apply(new BigDecimal("5.3")));
        indicator.addCandle(createCandle.apply(new BigDecimal("6.7")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.9")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.1")));
        indicator.addCandle(createCandle.apply(new BigDecimal("5.2")));
        assertEquals(new BigDecimal("0.25"), indicator.getSignalValue().setScale(2, RoundingMode.HALF_UP));
        indicator.addCandle(createCandle.apply(new BigDecimal("4.1")));
        assertEquals(new BigDecimal("-0.19"), indicator.getSignalValue().setScale(2, RoundingMode.HALF_UP));
        indicator.addCandle(createCandle.apply(new BigDecimal("3.5")));
        assertEquals(new BigDecimal("-0.44"), indicator.getSignalValue().setScale(2, RoundingMode.HALF_UP));
        indicator.addCandle(createCandle.apply(new BigDecimal("5.4")));
        assertEquals(new BigDecimal("-0.23"), indicator.getSignalValue().setScale(2, RoundingMode.HALF_UP));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.3")));
        assertEquals(new BigDecimal("0.20"), indicator.getSignalValue().setScale(2, RoundingMode.HALF_UP));
    }

//    @Test
//    void isTimeToBuy() {
//        strategy.addCandle(createCandle.apply(new BigDecimal("5.3")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("6.7")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("7.9")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("7.1")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("5.2")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("4.1")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("3.5")));
//        assertEquals(false, strategy.isTimeToBuy());
//        strategy.addCandle(createCandle.apply(new BigDecimal("5.4")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("7.3")));
//        assertEquals(true, strategy.isTimeToBuy());
//    }
//
//    @Test
//    void isTimeToSell() {
//        strategy.addCandle(createCandle.apply(new BigDecimal("5.3")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("6.7")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("7.9")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("7.1")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("5.2")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("4.1")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("3.5")));
//        assertEquals(true, strategy.isTimeToSell());
//        strategy.addCandle(createCandle.apply(new BigDecimal("5.4")));
//        strategy.addCandle(createCandle.apply(new BigDecimal("7.3")));
//        assertEquals(false, strategy.isTimeToSell());
//    }

    @Test
    void isOver() {

    }

    @Test
    void getDifference() {
        indicator.addCandle(createCandle.apply(new BigDecimal("5.3")));
        indicator.addCandle(createCandle.apply(new BigDecimal("6.7")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.9")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.1")));
        indicator.addCandle(createCandle.apply(new BigDecimal("5.2")));
        indicator.addCandle(createCandle.apply(new BigDecimal("4.1")));
        indicator.addCandle(createCandle.apply(new BigDecimal("3.5")));
        indicator.addCandle(createCandle.apply(new BigDecimal("5.4")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.3")));
        assertEquals(new BigDecimal("0.43"), indicator.getDifference().setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void getDifferenceInPercents() {
        indicator.addCandle(createCandle.apply(new BigDecimal("5.3")));
        indicator.addCandle(createCandle.apply(new BigDecimal("6.7")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.9")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.1")));
        indicator.addCandle(createCandle.apply(new BigDecimal("5.2")));
        indicator.addCandle(createCandle.apply(new BigDecimal("4.1")));
        indicator.addCandle(createCandle.apply(new BigDecimal("3.5")));
        indicator.addCandle(createCandle.apply(new BigDecimal("5.4")));
        indicator.addCandle(createCandle.apply(new BigDecimal("7.3")));
        assertEquals(new BigDecimal("68.21"), indicator.getDifferenceInPercents().setScale(2, RoundingMode.HALF_UP));
    }
}