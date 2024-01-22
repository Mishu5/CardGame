package com.example.kierki.server.eventhandling.events.server;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

/**
 * Event to handle creating new lobby
 */

public class CreateNewLobby extends Event {

    private final String lobbyName;

    public CreateNewLobby(String lobbyName) {
        super(EventType.CREATE_LOBBY);
        this.lobbyName = lobbyName;
    }

    @Override
    public void handleEvent() {

    }

    public String getLobbyName(){
        return lobbyName;
    }
}
