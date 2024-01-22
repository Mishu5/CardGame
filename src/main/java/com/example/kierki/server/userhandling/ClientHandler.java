package com.example.kierki.server.userhandling;

import com.example.kierki.server.eventhandling.LobbyEventListener;
import com.example.kierki.server.eventhandling.events.server.InvitePlayerToLobby;
import com.example.kierki.server.eventhandling.lobby.PlayerMovement;
import com.example.kierki.server.eventhandling.lobby.UpdateGame;
import com.example.kierki.server.game.Player;
import com.example.kierki.server.eventhandling.EventListener;
import com.example.kierki.server.eventhandling.events.server.CreateNewLobby;
import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.server.JoinLobby;
import com.example.kierki.server.eventhandling.events.server.NewPlayer;
import com.example.kierki.shared.transfer.Command;
import com.example.kierki.shared.transfer.DataContainer;
import com.example.kierki.shared.transfer.DataTypes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler extends Thread {

    private final int id;
    private Player player;
    private final Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final EventListener eventListener;
    private LobbyEventListener lobbyEventListener;
    boolean joinedStatus;

    public ClientHandler(int id, Socket clientSocket, EventListener eventListener) {
        this.id = id;
        this.clientSocket = clientSocket;
        this.eventListener = eventListener;
        this.joinedStatus = false;
    }

    public void generateIOForSocket() throws IOException {
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setLobbyEventListener(LobbyEventListener lobbyEventListener) {
        this.lobbyEventListener = lobbyEventListener;
        lobbyEventListener.addEvent(new UpdateGame());
    }

    public synchronized void createPlayer() throws IOException, ClassNotFoundException {
        String userName = null;

        DataContainer userData = (DataContainer) in.readUnshared();

        if (userData.getDataType() == DataTypes.COMMAND) {
            Command command = (Command) userData.getData();
            userName = id + "#" + command.getCommand();
        }
        player = new Player(userName);
        //sending username to player
        DataContainer data = new DataContainer(DataTypes.COMMAND, new Command(null, userName));
        out.reset();
        out.writeObject(data);
    }

    public int getHandlerId() {
        return id;
    }

    public String getPlayerName() {
        return player.getUserName();
    }

    public void setJoinedStatus(boolean joinedStatus) {
        this.joinedStatus = joinedStatus;
    }

    public ObjectOutputStream getOutputStream() {
        return out;
    }

    public void run() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            createPlayer();
            System.out.println("[" + LocalDateTime.now().format(formatter) + "]: Handler for player " + player.getUserName() + " stated...");
            eventListener.addEvent(new NewPlayer());

            while (true) {
                DataContainer userData = (DataContainer) in.readUnshared();
                if (userData.getDataType() == DataTypes.COMMAND) {
                    Command userCommand = (Command) userData.getData();
                    Event event;

                    switch (userCommand.getCommandType()) {
                        case CREATE_LOBBY:
                            if (joinedStatus) break;
                            event = new CreateNewLobby(userCommand.getCommand());
                            eventListener.addEvent(event);
                            break;
                        case JOIN_LOBBY:
                            if (joinedStatus) break;
                            event = new JoinLobby(userCommand.getCommand(), id);
                            eventListener.addEvent(event);
                            break;
                        case GAME_MOVEMENT:
                            if (!joinedStatus) break;
                            event = new PlayerMovement(id, userCommand.getCommand());
                            lobbyEventListener.addEvent(event);
                            break;
                        case INVITE:
                            if(joinedStatus) break;
                            String[] commands = userCommand.getCommand().split(" ");
                            if(commands.length != 2) break;
                            event = new InvitePlayerToLobby(commands[0], commands[1]);
                            eventListener.addEvent(event);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Klient rozłączył się ");
        } catch (ClassNotFoundException ignored) {
        }


    }

    public Player getPlayer(){
        return player;
    }

}
