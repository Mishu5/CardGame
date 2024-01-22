package com.example.kierki.server.eventhandling;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.lobby.PlayerMovement;
import com.example.kierki.server.game.Lobby;
import com.example.kierki.server.userhandling.ClientGameSender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Thread that handles all event of lobbyEventListener
 */

public class LobbyEventHandler extends Thread{

    private volatile LobbyEventListener lobbyEventListener;
    private volatile Lobby lobby;

    public LobbyEventHandler(LobbyEventListener lobbyEventListener, Lobby lobby){
        this.lobbyEventListener = lobbyEventListener;
        this.lobby = lobby;
    }

    public void run(){
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]: Lobby event handler started");
        while(true){
            if(lobbyEventListener.getEventCount() > 0){

                List<Event> eventsToHandle = lobbyEventListener.getEvents();

                while(!eventsToHandle.isEmpty()){

                    Event currentEvent = eventsToHandle.get(0);
                    eventsToHandle.remove(0);

                    switch(currentEvent.getEventType()){
                        case UPDATE_LOBBY:
                            ClientGameSender.sendGameStateToAllPlayers(lobby.getClients(), lobby.getGame());
                            break;
                        case PLAYER_MOVEMENT:
                            PlayerMovement tempPlayerMovementEvent = (PlayerMovement) currentEvent;
                            handleMovePlayer(tempPlayerMovementEvent.getClientId(), tempPlayerMovementEvent.getCardToMove());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void handleMovePlayer(int playerId, String card){
        lobby.playCard(playerId, card);
    }

}
