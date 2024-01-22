package com.example.kierki.client.communication;

import com.example.kierki.shared.transfer.Command;
import com.example.kierki.shared.transfer.CommandType;
import com.example.kierki.shared.transfer.DataContainer;
import com.example.kierki.shared.transfer.DataTypes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * Sends data as command to server
 */

public class DataSender{
    private final ObjectOutputStream out;

    public DataSender(ObjectOutputStream out){
        this.out = out;
    }

    public synchronized void sendCommand(CommandType commandType, String commandData) throws IOException {
        Command command = new Command(commandType, commandData);
        DataContainer data = new DataContainer(DataTypes.COMMAND, command);
        out.reset();
        out.writeObject(data);
    }

}
