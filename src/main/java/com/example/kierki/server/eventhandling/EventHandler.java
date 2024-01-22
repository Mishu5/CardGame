package com.example.kierki.server.eventhandling;

import com.example.kierki.server.Environment;
import com.example.kierki.server.eventhandling.events.server.InvitePlayerToLobby;
import com.example.kierki.server.game.Lobby;
import com.example.kierki.server.eventhandling.events.server.CreateNewLobby;
import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.server.JoinLobby;
import com.example.kierki.server.userhandling.ClientLobbyListSender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Event handler that handles event for server
 */

public class EventHandler extends Thread {
    private volatile EventListener eventListener;
    private final Environment environment;
    private final ClientLobbyListSender clientLobbyListSender;

    public EventHandler(EventListener eventListener, Environment environment, ClientLobbyListSender clientLobbyListSender) {
        this.eventListener = eventListener;
        this.environment = environment;
        this.clientLobbyListSender = clientLobbyListSender;
    }

    public void run() {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("[" + LocalDateTime.now().format(format) + "]: Event handler started...");

        while (true) {

            if (eventListener.getEventCount() > 0) {

                List<Event> eventsToHandle = eventListener.getEvents();

                while (!eventsToHandle.isEmpty()) {

                    Event currentEvent = eventsToHandle.get(0);
                    eventsToHandle.remove(0);

                    switch (currentEvent.getEventType()) {
                        case CREATE_LOBBY:
                            CreateNewLobby tempCreateEvent = (CreateNewLobby) currentEvent;
                            createLobby(tempCreateEvent.getLobbyName());
                            break;
                        case JOIN_LOBBY:
                            JoinLobby tempJoinEvent = (JoinLobby) currentEvent;
                            joinLobby(tempJoinEvent.getLobbyName(), tempJoinEvent.getPlayerHandlerId(), format);
                            break;
                        case NEW_PLAYER:
                            clientLobbyListSender.sendUpdatedLobby();
                            break;
                        case INVITE_TO_LOBBY:
                            InvitePlayerToLobby currentCommand = (InvitePlayerToLobby) currentEvent;
                            invitePlayerToLobby(currentCommand.getLobbyToInvite(), currentCommand.getPlayerToInvite());
                            break;
                    }

                }

            }

        }

    }

    private void createLobby(String lobbyName) {
        Lobby newLobby = new Lobby(lobbyName);
        environment.addLobby(newLobby);
        clientLobbyListSender.sendUpdatedLobby();
    }

    private void joinLobby(String lobbyName, int playerHandlerId, DateTimeFormatter format) {
        environment.joinLobby(lobbyName, playerHandlerId);
        clientLobbyListSender.sendUpdatedLobby();
    }

    private void invitePlayerToLobby(String lobbyName, String playerName){
        clientLobbyListSender.sendInviteToPlayer("Zaproszenie do lobby: " + lobbyName, playerName);
    }

}
