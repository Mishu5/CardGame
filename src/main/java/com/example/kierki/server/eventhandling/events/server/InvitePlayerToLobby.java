package com.example.kierki.server.eventhandling.events.server;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

/**
 * Event to handle sending invites to players
 */

public class InvitePlayerToLobby extends Event {
    private final String playerToInvite;
    private final String lobbyToInvite;
    public InvitePlayerToLobby(String playerToInvite, String lobbyToInvite) {
        super(EventType.INVITE_TO_LOBBY);
        this.playerToInvite = playerToInvite;
        this.lobbyToInvite = lobbyToInvite;
    }

    @Override
    public void handleEvent() {

    }

    public String getPlayerToInvite() {
        return playerToInvite;
    }

    public String getLobbyToInvite() {
        return lobbyToInvite;
    }
}
