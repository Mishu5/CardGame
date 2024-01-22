package com.example.kierki.client;

import com.example.kierki.client.communication.DataReceiver;
import com.example.kierki.client.communication.DataSender;
import com.example.kierki.shared.transfer.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Main class for client. Connects to server and lets user input their name
 * Starts threads required for communication and launches gui
 */

public class KierkiClient extends Application {

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    //Working environment
    private static DataSender sender;
    private static volatile DataReceiver receiverThread;
    private static UserConsoleInterface consoleUiThread;
    private static GuiViewUpdater guiViewUpdater;
    private static String userName;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KierkiClient.class.getResource("/com/example/kierki/kierki-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Kierki");
        stage.setScene(scene);
        KierkiController controller = fxmlLoader.getController();
        controller.setDataSender(sender);
        guiViewUpdater.setController(controller);
        stage.show();
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Podaj nick: ");
//        String userName = keyboard.nextLine();
        String userName = args[0];
        try{
            //connecting to server
            socket = new Socket("127.0.0.1",2137);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            //creating gameEnvironment

            //creating handlers and IO
            sender = new DataSender(out);
            consoleUiThread = new UserConsoleInterface(keyboard, sender);
            receiverThread = new DataReceiver(in, consoleUiThread);
            guiViewUpdater = new GuiViewUpdater();
            receiverThread.setGuiViewUpdater(guiViewUpdater);

            //sending username to server
            sender.sendCommand(CommandType.NULL, userName);
            System.out.println("Connected...");

            //getting username
            DataContainer data = (DataContainer) in.readUnshared();
            Command command = (Command) data.getData();
            userName = command.getCommand();

            UserName.setUserName(userName);

            //starting
            receiverThread.start();
            consoleUiThread.start();

            launch(args);

        }catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException ignored) {
        }
    }
}