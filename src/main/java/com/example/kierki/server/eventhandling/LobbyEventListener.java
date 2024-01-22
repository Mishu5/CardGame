package com.example.kierki.server.eventhandling;

import com.example.kierki.server.eventhandling.events.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to events created by lobby its assigned
 */

public class LobbyEventListener {

    private final int lobbyId;
    private final List<Event> events;

    public LobbyEventListener(int lobbyId){
        this.lobbyId = lobbyId;
        events = new ArrayList<>();
    }

    public List<Event> getEvents(){
        return events;
    }

    public int getLobbyId(){
        return lobbyId;
    }

    public int getEventCount(){
        return events.size();
    }

    /**
     * Adds event
     * @param event to add
     */
    public void addEvent(Event event){
        events.add(event);
    }

}
