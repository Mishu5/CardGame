package com.example.kierki.server.eventhandling.events;

/**
 * Base Event class
 */

public abstract class Event {

    protected EventType eventType;

    public Event(EventType eventType){
        this.eventType = eventType;
    }

    public EventType getEventType(){
        return eventType;
    }

    public abstract void handleEvent();

}
