package com.example.kierki.server;

import com.example.kierki.server.eventhandling.EventHandler;
import com.example.kierki.server.eventhandling.EventListener;
import com.example.kierki.server.game.Lobby;
import com.example.kierki.server.userhandling.ClientLobbyListSender;
import com.example.kierki.server.userhandling.NewConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Server to host games
 * Initializes required parameters
 * Starts thread to handle incoming users
 * Has console to skip Hands of the specified lobby and for listing all lobbies
 */

public class KierkiServer {
    public static void main(String[] args) {

        //creating user input
        Scanner keyboard = new Scanner(System.in);

        //Setting up environment
        Environment environment = new Environment();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //Creating client lobby sender
        ClientLobbyListSender clientLobbyListSender = new ClientLobbyListSender(environment.getLobbies());

        //Creating event listener and handler
        EventListener eventListener = new EventListener(format);
        EventHandler eventHandler = new EventHandler(eventListener, environment, clientLobbyListSender);

        ServerSocket serverSocket;
        NewConnectionHandler newConnectionHandler;

        try {
            //Opening Socket
            serverSocket = new ServerSocket(2137);

            //Creating new connection handler
            newConnectionHandler = new NewConnectionHandler(serverSocket, environment.getClientHandlers(), eventListener, clientLobbyListSender);

            //Starting all threads
            newConnectionHandler.start();
            eventHandler.start();
            while (true) {
                String command = keyboard.nextLine();
                String[] splitCommand = command.split(" ");
                if (command.equals("lobby")) {
                    for (Lobby lobby: environment.getLobbies()) {
                        System.out.println("Lobby " + lobby.getLobbyName() + ": " + lobby.getAllPlayersAsString());
                    }
                }else if(splitCommand[0].equals("skip")){
                    if(splitCommand.length == 2){
                        environment.skipHand(splitCommand[1]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}