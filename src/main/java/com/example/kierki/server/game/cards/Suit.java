package com.example.kierki.server.game.cards;

/**
 * Enum to define suit of card
 */
public enum Suit {
    HEARTS("H", '\u2665'),
    DIAMONDS("D", '\u2666'),
    CLUBS("C", '\u2663'),
    SPADES("S", '\u2660');

    private final String name;
    private final char symbol;

    Suit(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

}
