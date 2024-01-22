package com.example.kierki.server.userhandling;

import com.example.kierki.server.eventhandling.EventListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles incoming users and gives them their client handler
 */

public class NewConnectionHandler extends Thread {

    private final ServerSocket serverSocket;
    private final List<ClientHandler> clients;
    private final EventListener eventListener;
    private final ClientLobbyListSender lobbySender;

    public NewConnectionHandler(ServerSocket serverSocket, List<ClientHandler> clients, EventListener eventListener, ClientLobbyListSender lobbySender) {
        this.serverSocket = serverSocket;
        this.clients = clients;
        this.eventListener = eventListener;
        this.lobbySender = lobbySender;
    }

    public void run() {

        int clientId = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("[" + LocalDateTime.now().format(formatter) + "]: Waiting for players...");
        while (true) {
            try {
                //accepting new player
                Socket clientSocket = serverSocket.accept();

                //prepare new client
                ClientHandler tempClient = new ClientHandler(clientId++, clientSocket, eventListener);
                tempClient.generateIOForSocket();

                //add out to list
                lobbySender.addNewOutput(tempClient);

                //start new client
                tempClient.start();
                clients.add(tempClient);
                System.out.println("[" + LocalDateTime.now().format(formatter) + "]: New client added...");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
