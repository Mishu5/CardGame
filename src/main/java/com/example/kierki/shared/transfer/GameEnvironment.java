package com.example.kierki.shared.transfer;

import com.example.kierki.server.game.Game;
import com.example.kierki.server.game.Player;
import com.example.kierki.server.game.cards.Card;

import java.io.Serializable;
import java.util.List;

/**
 * Container that holds data about current game in selected lobby
 */

public class GameEnvironment implements DataInterface, Serializable {

    private boolean started;
    private List<Player> players;
    private int handIndex;
    private int currentPlayerIndex;
    private List<Card> table;

    public GameEnvironment(){
    }

    @Override
    public String toString() {
        return "GameEnvironment{" +
                "started=" + started +
                ", players=" + players +
                ", currentPlayerIndex=" + currentPlayerIndex +
                ", table=" + table +
                '}';
    }

    public boolean isStarted(){
        return started;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public List<Card> getTable(){
        return table;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setTable(List<Card> table) {
        this.table = table;
    }

    public int getHandIndex() {
        return handIndex;
    }

    public void setHandIndex(int handIndex) {
        this.handIndex = handIndex;
    }
    public String getCurrentPlayer(){
        String result = null;
        for(Player player : players){
            if(player.getPlayerIndexInLobby() == currentPlayerIndex){
                result = player.getUserName();
                break;
            }
        }
        return result;
    }
}
