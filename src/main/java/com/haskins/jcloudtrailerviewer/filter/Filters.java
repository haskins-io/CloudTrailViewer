package com.haskins.jcloudtrailerviewer.filter;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author mark
 */
public class Filters {
    
    private final List<EventFilter> filters = new ArrayList<>();
    
    public void addEventFilter(EventFilter filter) {
        this.filters.add(filter);
    }
    
    public CopyOnWriteArrayList<Event> filterEvents(Collection<Event> events) {
        
        CopyOnWriteArrayList<Event> filteredEvents = new CopyOnWriteArrayList<>();
        
        for (Event event : events) {
            
            boolean passed = true;
            
            for (EventFilter filter : filters) {
                
                passed &= filter.passesFilter(event);
            }
            
            if (passed) {
                
                filteredEvents.add(event);
            }
        } 
        
        return filteredEvents;
    } 
}
