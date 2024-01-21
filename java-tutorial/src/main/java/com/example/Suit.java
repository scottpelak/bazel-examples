package com.example;

public enum Suit {
    CLUBS("♧"),
    DIAMONDS("♦"),
    HEARTS("♥"),
    SPADES("♤");

    String symbol;

    Suit(
        String symbol
    ) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public Suit getOppositeSuit() {
        switch (this) {
            case CLUBS:
                return SPADES;
            case DIAMONDS:
                return HEARTS;
            case HEARTS:
                return DIAMONDS;
            default:
                return CLUBS;
        }
    }

    @Override
    public String toString() {
        return symbol;
    }
}
