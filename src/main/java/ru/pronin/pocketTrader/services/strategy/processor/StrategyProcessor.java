package ru.pronin.pocketTrader.services.strategy.processor;

import ru.tinkoff.piapi.contract.v1.Candle;

public interface StrategyProcessor {
    void process(Candle brokerCandle);
}
