package com.example.kierki.shared.transfer;

import java.io.Serializable;

/**
 * Command that is used to send text between server and client
 */

public class Command implements DataInterface, Serializable {

    private final CommandType commandType;
    private final String command;

    public Command(CommandType commandType, String command){
        this.commandType = commandType;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public CommandType getCommandType(){
        return commandType;
    }

}
