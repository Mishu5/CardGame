package com.example.kierki.server.eventhandling.lobby;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

/**
 * Event to update game for players
 */
public class UpdateGame extends Event {
    public UpdateGame() {
        super(EventType.UPDATE_LOBBY);
    }

    @Override
    public void handleEvent() {

    }
}
