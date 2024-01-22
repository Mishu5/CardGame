package com.example.kierki.server.userhandling;

import com.example.kierki.server.game.Lobby;
import com.example.kierki.shared.transfer.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientLobbyListSender {

    private final List<ClientHandler> clients;
    private final List<Lobby> lobbies;

    public ClientLobbyListSender(List<Lobby> lobbies){
        clients = new ArrayList<>();
        this.lobbies = lobbies;
    }

    public void addNewOutput(ClientHandler newClient){
        clients.add(newClient);
    }

    public synchronized void sendUpdatedLobby(){
        try{
            DataContainer data = new DataContainer(DataTypes.LOBBY_LIST, new LobbyList(lobbies));

            for (ClientHandler client: clients) {
                    if(client.joinedStatus){
                        continue;
                    }
                    client.getOut().reset();
                    client.getOut().writeObject(data);
            }

        }catch (IOException ignored){
        }
    }
    public synchronized void sendInviteToPlayer(String invite, String clientToSend){
        ClientHandler client = getClientHandlerFromName(clientToSend);
        if(client == null){
            return;
        }
        try{
            Command command = new Command(CommandType.INVITE, invite);
            DataContainer data = new DataContainer(DataTypes.COMMAND, command);
            client.getOut().reset();
            client.getOut().writeObject(data);
        }catch(IOException ignored){

        }
    }

    private ClientHandler getClientHandlerFromName(String name){
        ClientHandler result = null;
        for(ClientHandler client : clients){
            if(client.getPlayerName().equals(name)){
                result = client;
                break;
            }
        }
        return result;
    }
}
