package com.example.kierki.server.eventhandling.events.server;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

/**
 * Event to handle joining lobby by players
 */

public class JoinLobby extends Event {

    private final String lobbyName;
    private final int playerHandlerId;

    public JoinLobby(String lobbyName, int playerHandlerId) {
        super(EventType.JOIN_LOBBY);
        this.lobbyName = lobbyName;
        this.playerHandlerId = playerHandlerId;
    }

    @Override
    public void handleEvent() {

    }

    public String getLobbyName() {
        return lobbyName;
    }

    public int getPlayerHandlerId() {
        return playerHandlerId;
    }
}
