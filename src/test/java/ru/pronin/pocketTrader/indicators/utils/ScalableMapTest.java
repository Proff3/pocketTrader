package ru.pronin.pocketTrader.indicators.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScalableMapTest {

    ScalableMap<String, Integer> map = new ScalableMap<>(2);

    @Test
    void addValue() {
        map.addValue("1", 1);
        map.addValue("2", 2);
        map.addValue("3", 3);
        assertEquals(map.getValues().get(1), 3);
        assertEquals(map.getSize(), 2);
    }
}