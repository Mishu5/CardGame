package com.example.kierki.shared.transfer;

import com.example.kierki.server.game.Lobby;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Container that holds list of lobbies on server
 */

public class LobbyList implements DataInterface, Serializable {

    private List<LobbyListData> lobbies;

    public LobbyList(List<Lobby> lobbyList) {

        lobbies = new ArrayList<>();

        for (Lobby lobby: lobbyList) {
            LobbyListData tempData = new LobbyListData(lobby.getLobbyName(), lobby.getPlayerCount(),4, false, lobby.getAllPlayersAsString());
            lobbies.add(tempData);
        }

    }

    public List<LobbyListData> getLobbies() {
        return lobbies;
    }
}
