package com.example.kierki.server.game;

import com.example.kierki.server.eventhandling.LobbyEventListener;
import com.example.kierki.server.eventhandling.lobby.UpdateGame;
import com.example.kierki.server.userhandling.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds information about lobby:
 * current game that is played, clients connected to lobby and lobby listener
 */

public class Lobby {

    private String lobbyName;
    private final List<ClientHandler> clients;
    private final int maxCapacity;
    private volatile Game game;
    private LobbyEventListener listener;

    public Lobby(String lobbyName) {
        this.lobbyName = lobbyName;
        clients = new ArrayList<>();
        maxCapacity = 4;
        game = new Game();
    }

    public int getPlayerCount() {
        return clients.size();
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyEventListener(LobbyEventListener listener) {
        this.listener = listener;
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public void setLobbyName(String newName) {
        lobbyName = newName;
    }

    public String getAllPlayersAsString() {
        StringBuilder result = new StringBuilder();

        for (ClientHandler client : clients) {
            result.append(client.getPlayerName()).append(", ");
        }
        return result.toString().trim();
    }

    public boolean addClient(ClientHandler newClient) {
        if (clients.size() >= maxCapacity) {
            return false;
        }
        clients.add(newClient);
        game.addPlayer(newClient);
        listener.addEvent(new UpdateGame());
        if (clients.size() == 4) {
            game.startGame();
            listener.addEvent(new UpdateGame());
        }
        return true;
    }

    public Game getGame() {
        return game;
    }

    public void playCard(int userId, String cardToPlay) {
        game.make_move(userId, cardToPlay);
        listener.addEvent(new UpdateGame());
    }

    public void skipHand() {
        if (game.isStared()) {
            game.skipHand();
            listener.addEvent(new UpdateGame());
        }
    }
}
