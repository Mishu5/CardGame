package com.example.kierki.server.game.cards;

import java.io.Serializable;

/**
 * Card used in Game class to play
 */
public class Card implements Serializable, Cloneable {
    private final Suit suit;
    private final int value;
    private int placedByPlayerId;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String toNameString(){
        return suit.getSymbol() + " " + value;
    }

    public String toButtonUserData(){
        return suit.getName() + " " + value;
    }

    public int getPlacedByPlayerId() {
        return placedByPlayerId;
    }

    public void setPlacedByPlayerId(int placedByPlayerId) {
        this.placedByPlayerId = placedByPlayerId;
    }

    @Override
    public Card clone() {
        try {
            return (Card) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
