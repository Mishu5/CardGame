package com.example.kierki.server.eventhandling;

import com.example.kierki.server.eventhandling.events.Event;
import com.example.kierki.server.eventhandling.events.EventType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Event listener that handles events from server
 */

public class EventListener {

    private final List<Event> events;
    private DateTimeFormatter format;

    public EventListener(DateTimeFormatter format){
        events = new ArrayList<>();
        this.format = format;
    }

    public List<Event> getEvents(){
        return events;
    }

    public int getEventCount(){
        return events.size();
    }

    public void addEvent(Event event){
        events.add(event);
        if(event.getEventType() != EventType.PLAYER_MOVEMENT) {
            System.out.println("[" + LocalDateTime.now().format(format) + "]: New event: " + event.getEventType());
        }
    }

}
