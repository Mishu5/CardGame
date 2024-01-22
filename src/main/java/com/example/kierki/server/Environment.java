package com.example.kierki.server;

import com.example.kierki.server.eventhandling.LobbyEventHandler;
import com.example.kierki.server.eventhandling.LobbyEventListener;
import com.example.kierki.server.game.Lobby;
import com.example.kierki.server.userhandling.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds most important information:
 * client handlers,
 * lobbies,
 * event listeners,
 * event handlers,
 * Creates lobbies assignees players to them
 */

public class Environment {

    private final List<Lobby> lobbies;
    private final List<ClientHandler> clientHandlers;
    private final List<LobbyEventListener> lobbyEventListeners;
    private final List<LobbyEventHandler> lobbyEventHandlers;
    private int lobbyId;

    public Environment(){
        lobbies = new ArrayList<>();
        clientHandlers = new ArrayList<>();
        lobbyEventListeners = new ArrayList<>();
        lobbyEventHandlers = new ArrayList<>();
        lobbyId = 0;
    }

    public int getLobbiesCount(){
        return lobbies.size();
    }

    public int getClientCount(){
        return clientHandlers.size();
    }

    /**
     * Add lobby to list and gives it unique id
     * @param lobby
     */
    public void addLobby(Lobby lobby){
        String lobbyNewName = lobbyId + "#" + lobby.getLobbyName();
        lobby.setLobbyName(lobbyNewName);

        //creating handlers for lobby
        LobbyEventListener tempLobbyEventListener = new LobbyEventListener(lobbyId);
        LobbyEventHandler tempLobbyEventHandler = new LobbyEventHandler(tempLobbyEventListener, lobby);
        tempLobbyEventHandler.start();
        lobby.setLobbyEventListener(tempLobbyEventListener);

        //adding to lists
        lobbyEventHandlers.add(tempLobbyEventHandler);
        lobbyEventListeners.add(tempLobbyEventListener);
        lobbies.add(lobby);
        lobbyId++;
    }

    public void addClientHandler(ClientHandler clientHandler){
        clientHandlers.add(clientHandler);
    }

    public List<ClientHandler> getClientHandlers(){
        return clientHandlers;
    }

    public List<Lobby> getLobbies(){
        return lobbies;
    }

    public Lobby getLobbyByName(String Name){
        for(Lobby lobby: lobbies){
            if(lobby.getLobbyName().equals(Name)){
                return lobby;
            }
        }
        return null;
    }

    public ClientHandler getClientHandlerById(int id){
        for(ClientHandler clientHandler: clientHandlers){
            if(clientHandler.getHandlerId() == id){
                return clientHandler;
            }
        }
        return null;
    }

    /**
     * Assignees user to lobby
     * @param lobbyName
     * @param clientHandlerId
     * @return boolean if user was successfully added or not
     */
    public boolean joinLobby(String lobbyName, int clientHandlerId){
        ClientHandler clientToJoin = getClientHandlerById(clientHandlerId);
        Lobby lobbyToJoin = getLobbyByName(lobbyName);
        if(lobbyToJoin == null || clientToJoin == null){
            return false;
        }
        boolean result = lobbyToJoin.addClient(clientToJoin);
        if(result){
            setJoinedStatusToHandler(true, clientHandlerId);
            setListenerToUserHandler(clientHandlerId, getIdFromName(lobbyName));
            //update all players
            System.out.println("PRZYPISANO GRACZA OD ID: " + clientHandlerId + " DO HANDLERA LOBBY O ID: " + getIdFromName(lobbyName));
        }
        return result;
    }

    private void setJoinedStatusToHandler(boolean status, int id){
        for(ClientHandler client: clientHandlers){
            if(client.getHandlerId() == id){
                client.setJoinedStatus(status);
                break;
            }
        }
    }

    private void setListenerToUserHandler(int userId, int lobbyEventListerId){

        ClientHandler tempClientHandler = getClientHandlerById(userId);
        LobbyEventListener tempLobbyEventListener = null;
        for(LobbyEventListener listener: lobbyEventListeners){
            if(listener.getLobbyId() == lobbyEventListerId){
                tempLobbyEventListener = listener;
                break;
            }
        }
        if(tempLobbyEventListener != null) {
            tempClientHandler.setLobbyEventListener(tempLobbyEventListener);
        }
    }

    private int getIdFromName(String name){
        return Character.getNumericValue(name.charAt(0));
    }

    public void skipHand(String lobbyName){
        Lobby lobbyToSkip = getLobbyByName(lobbyName);
        if(lobbyToSkip == null){
            return;
        }
        lobbyToSkip.skipHand();
    }
}
