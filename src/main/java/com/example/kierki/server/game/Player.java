package com.example.kierki.server.game;

import com.example.kierki.server.game.cards.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold information about client in specific game in lobby
 */

public class Player implements Serializable, Cloneable {
    private int playerIndexInLobby;
    private final String userName;
    private List<Card> hand;
    private int score;

    public Player(String userName){
        this.userName = userName;
        this.hand = new ArrayList<>();
        score = 0;
    }

    public void setPlayerIndexInLobby(int index){
        this.playerIndexInLobby = index;
    }

    public String getUserName(){
        return userName;
    }

    public List<Card>getHand(){
        return hand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addToHand(Card card){
        hand.add(card);
    }

    public int getPlayerIndexInLobby() {
        return playerIndexInLobby;
    }

    /**
     * @param card
     * @return reference to Card from Hand
     */
    public Card getCardToPlay(String card){
        Card result = null;
        String[] cardData = card.split(" ");

        for(Card currentCard : hand){
            if(currentCard.getSuit().getName().equals(cardData[0])){
                if(currentCard.getValue() == Integer.parseInt(cardData[1])){
                    result = currentCard;
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public Player clone() {
        try {
            Player clonedPlayer = (Player) super.clone();

            // Klonowanie listy kart
            List<Card> clonedHand = new ArrayList<>();
            for (Card card : hand) {
                clonedHand.add(card.clone());
            }
            clonedPlayer.hand = clonedHand;

            return clonedPlayer;
        } catch (CloneNotSupportedException e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }

}
