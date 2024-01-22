package com.example.kierki.server.eventhandling.lobby;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

/**
 *Event for handling player moves
 */

public class PlayerMovement extends Event {

    private final int clientId;
    private final String cardToMove;

    public PlayerMovement(int clientId, String cardToMove) {
        super(EventType.PLAYER_MOVEMENT);
        this.clientId = clientId;
        this.cardToMove = cardToMove;
    }

    @Override
    public void handleEvent() {

    }

    public int getClientId() {
        return clientId;
    }

    public String getCardToMove() {
        return cardToMove;
    }
}
