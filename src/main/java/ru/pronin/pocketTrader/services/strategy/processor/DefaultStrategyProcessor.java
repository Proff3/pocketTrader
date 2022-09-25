package ru.pronin.pocketTrader.services.strategy.processor;

import org.springframework.stereotype.Service;
import ru.pronin.pocketTrader.api.entities.CustomCandle;
import ru.tinkoff.piapi.contract.v1.Candle;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

@Service
public class DefaultStrategyProcessor implements StrategyProcessor {

    private final Logger LOGGER = Logger.getLogger(DefaultStrategyProcessor.class.getName());

    @Override
    public void process(Candle brokerCandle) {
        CustomCandle candle = new CustomCandle(brokerCandle);
        LOGGER.log(INFO, candle.toString());
    }
}
