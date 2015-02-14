package com.github.githublemming.jcloudtrailerviewer.filter;

import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author mark
 */
public class Filters implements EventFilterListener {
    
    private final List<EventFilter> filters = new ArrayList<>();
    private final List<FiltersListener> listeners = new ArrayList<>();
    
    public void addEventFilter(EventFilter filter) {
        filter.addListener(this);
        this.filters.add(filter);
    }
    
    public void addListener(FiltersListener l) {
        this.listeners.add(l);
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
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventFilterListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
        
        for (FiltersListener l  : this.listeners) {
            
            l.onFilterChanged();
        }
    }
}
