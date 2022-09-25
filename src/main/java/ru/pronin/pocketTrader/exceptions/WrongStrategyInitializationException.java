package ru.pronin.pocketTrader.exceptions;

public class WrongStrategyInitializationException extends Exception{
    public WrongStrategyInitializationException() {
        super("Wrong strategy initialization");
    }
}
