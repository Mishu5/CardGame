package com.example.kierki.client.communication;

import com.example.kierki.client.GuiViewUpdater;
import com.example.kierki.client.UserConsoleInterface;
import com.example.kierki.shared.transfer.*;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Waits for input from server and takes actions according to input
 */

public class DataReceiver extends Thread{

    private final ObjectInputStream in;
    private final UserConsoleInterface userUI;
    private GuiViewUpdater guiViewUpdater;
    private boolean joined;

    public DataReceiver(ObjectInputStream in, UserConsoleInterface userUI){
        this.in = in;
        this.userUI = userUI;
        this.joined = false;
    }

    public void run(){
        while(true){
            try {
                DataContainer dataFromServer = (DataContainer) in.readObject();

                switch(dataFromServer.getDataType()){
                    case LOBBY_LIST:
                        if(joined) break;
                        LobbyList lobbyList = (LobbyList) dataFromServer.getData();
                        userUI.updateLobbyList(lobbyList);
                        break;
                    case GAME_ENVIRONMENT:
                        GameEnvironment environment = (GameEnvironment)dataFromServer.getData();
                        handleGameEnvironment(environment);
                        joined = true;
                        break;
                    case COMMAND:
                        Command command = (Command) dataFromServer.getData();
                        handleCommand(command);
                    default:
                        break;

                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void handleGameEnvironment(GameEnvironment environment){
        guiViewUpdater.updateView(environment);
    }
    public void setGuiViewUpdater(GuiViewUpdater guiViewUpdater) {
        this.guiViewUpdater = guiViewUpdater;
    }
    private void handleCommand(Command command){
        if(command.getCommandType() == CommandType.INVITE){
            userUI.setMessage(command.getCommand());
        }
    }
}
