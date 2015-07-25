/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that models a database where all events loaded as stored.
 * 
 * @author mark
 */
public class EventDatabase {
    
    private final List<EventDatabaseListener> listeners = new ArrayList<>();
    
    private final List<Event> events = new ArrayList<>();
    
    /**
     * adds a listener to the database
     * @param l reference to a listener
     */
    public void addListener(EventDatabaseListener l) {
        listeners.add(l);
    }
    
    /**
     * Adds a new Event to the database
     * @param event object to be added
     */
    public void addEvent(Event event) {
        events.add(event);
         
        fireUpdate(event);
    }
     
    /**
     * returns the Event in the database at the specified position
     * @param index position
     * @return Event
     */
    public Event getEventByIndex(int index) {
        return events.get(index);
    }
    
    /**
     * Returns all the events in the database as a List object
     * @return Collection of Event objects
     */
    public List<Event> getEvents() {
        return events;
    }
    
    /**
     * Clears all the events in the database.
     */
    public void clear() {
        events.clear();
    }
    
    /**
     * Returns the size of the database
     * @return number of events in database
     */
    public int size() {
        return events.size();
    }
    
    /**
     * 
     * @param event 
     */
    protected void fireUpdate(Event event) {
        
        for (EventDatabaseListener l : listeners) {
            l.eventAdded(event);
        } 
    }   
}
