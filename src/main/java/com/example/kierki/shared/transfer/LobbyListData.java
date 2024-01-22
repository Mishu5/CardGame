package com.example.kierki.shared.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Container that hold data about current Lobby
 */
public class LobbyListData implements Serializable {

    private final String lobbyName;
    private final int lobbyUserCount;
    private final int lobbyMaxCount;
    private final boolean gameStarted;
    private final String players;

    public LobbyListData(String lobbyName, int lobbyUserCount, int lobbyMaxCount, boolean gameStarted, String players) {
        this.lobbyName = lobbyName;
        this.lobbyUserCount = lobbyUserCount;
        this.lobbyMaxCount = lobbyMaxCount;
        this.gameStarted = gameStarted;
        this.players = players;
    }

    public String getLobbyName(){
        return lobbyName;
    }

    public int getLobbyUserCount() {
        return lobbyUserCount;
    }

    public int getLobbyMaxCount() {
        return lobbyMaxCount;
    }

    public String toString(){
        String state = null;
        if(gameStarted){
            state = "Rozpoczęta";
        }else{
            state = "Czekanie na graczy";
        }

        return lobbyName + " | stan gry: " + state + " | gracze(" + lobbyUserCount + "/" + lobbyMaxCount + "): " + players;
    }
}
