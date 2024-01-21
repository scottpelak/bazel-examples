package com.example;

public enum Rank {
    ACE(" A"),
    KING(" K"),
    QUEEN(" Q"),
    JACK(" J"),
    TEN("10"),
    NINE(" 9");

    String symbol;

    Rank(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
