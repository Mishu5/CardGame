package com.example.kierki.server.userhandling;

import com.example.kierki.server.game.Game;
import com.example.kierki.shared.transfer.DataContainer;
import com.example.kierki.shared.transfer.DataTypes;
import com.example.kierki.shared.transfer.GameEnvironment;

import java.io.IOException;
import java.util.List;

public class ClientGameSender {

    public synchronized static void sendGameStateToAllPlayers(List<ClientHandler> clientsToSend, Game gameToSend) {
        DataContainer data = new DataContainer(DataTypes.GAME_ENVIRONMENT, gameToSend.toGameEnvironment());
        for (ClientHandler client : clientsToSend) {
            try {
                client.getOut().reset();
                client.getOut().writeObject(data);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
