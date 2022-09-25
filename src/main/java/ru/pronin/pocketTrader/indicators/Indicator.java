package ru.pronin.pocketTrader.indicators;

import ru.pronin.pocketTrader.api.entities.CustomCandle;
import ru.tinkoff.piapi.contract.v1.Candle;

public interface Indicator {
    void addCandle(CustomCandle candle);
    Boolean isEnoughInformation();
    Boolean isOver();
    int getIndicatorDepth();
    void setOver();
}
