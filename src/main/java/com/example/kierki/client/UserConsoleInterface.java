package com.example.kierki.client;

import com.example.kierki.client.communication.DataSender;
import com.example.kierki.shared.transfer.CommandType;
import com.example.kierki.shared.transfer.LobbyList;
import com.example.kierki.shared.transfer.LobbyListData;

import java.io.IOException;
import java.util.Scanner;

/**
 * Thread that handles user input from terminal
 */

public class UserConsoleInterface extends Thread {

    private final Scanner keyboard;
    private final DataSender sender;

    public UserConsoleInterface(Scanner keyboard, DataSender sender) {
        this.keyboard = keyboard;
        this.sender = sender;
    }

    /**
     * shows current lobbies on server
     * @param lobbies to show
     */
    public void updateLobbyList(LobbyList lobbies){
        System.out.flush();
        System.out.println("\n-------------------------------------------");
        System.out.println("Lista lobby:");
        for(LobbyListData lobby: lobbies.getLobbies()){
            System.out.println("|-> " + lobby.toString());
        }
        System.out.println("\nDołącz(join) | Nowe lobby(new) + nazwa | Zaproś(invite) + gracz + lobby");
    }

    /**
     * show message send by server
     * @param message to show
     */
    public void setMessage(String message){
        System.out.println(message);
    }

    /**
     * Waits for user input and send it to server as a command
     */
    public void run() {
        while (true) {

            String input = keyboard.nextLine();
            String[] inputSplit = input.split(" ");

            if (inputSplit.length < 2) {
                continue;
            }

            String command = inputSplit[0];
            String data = inputSplit[1];

            try {
                switch (command) {
                    case "new":
                        sender.sendCommand(CommandType.CREATE_LOBBY, data);
                        break;
                    case "join":
                        sender.sendCommand(CommandType.JOIN_LOBBY, data);
                        break;
                    case "invite":
                        String invite = inputSplit[1] + " " + inputSplit[2];
                        sender.sendCommand(CommandType.INVITE, invite);
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
