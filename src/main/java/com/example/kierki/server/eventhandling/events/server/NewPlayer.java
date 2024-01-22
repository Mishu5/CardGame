package com.example.kierki.server.eventhandling.events.server;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

/**
 * Event to handle new player in server client list
 */

public class NewPlayer extends Event {
    public NewPlayer() {
        super(EventType.NEW_PLAYER);
    }

    @Override
    public void handleEvent() {

    }
}
