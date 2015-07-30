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

import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.TimeStampComparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that models a database where all events loaded as stored.
 * 
 * @author mark
 */
public class EventDatabase implements EventLoaderListener {
    
    private final List<EventDatabaseListener> listeners = new ArrayList<>();
    
    private final List<Event> events = new ArrayList<>();
    
    private final StatusBar statusBar;
    
    public EventDatabase(StatusBar sbar) {
        this.statusBar = sbar;
    }
        
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
        
        Collections.sort(events, new TimeStampComparator());
        
        if (this.statusBar != null) {
            this.statusBar.setLoadedEvents(this.events.size());
        }
         
        fireUpdate(event);
    }
    
    /**
     * Adds all the Events to the database
     * @param events 
     */
    public void addEvents(List<Event> events) {
        this.events.addAll(events);
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
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void processingFile(int fileCount, int total) { }

    @Override
    public void finishedLoading() {
        Collections.sort(events, new TimeStampComparator());
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
