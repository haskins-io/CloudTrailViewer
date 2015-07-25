/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public class EventDatabase {
    
    private final List<EventDatabaseListener> listeners = new ArrayList<>();
    
    private final List<Event> events = new ArrayList<>();
    
    public void addListener(EventDatabaseListener l) {
        listeners.add(l);
    }
    
    public void addEvent(Event event) {
        events.add(event);
         
        fireUpdate(event);
    }
        
    public Event getEventByIndex(int index) {
        return events.get(index);
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public void clear() {
        events.clear();
    }
    
    public int size() {
        return events.size();
    }
    
    public void fireUpdate(Event event) {
        
        for (EventDatabaseListener l : listeners) {
            l.eventAdded(event);
        } 
    }
        
}
