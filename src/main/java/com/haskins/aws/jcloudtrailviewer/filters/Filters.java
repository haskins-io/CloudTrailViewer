package com.haskins.aws.jcloudtrailviewer.filters;

import com.haskins.aws.jcloudtrailviewer.models.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    public List<Event> filterEvents(Collection<Event> events) {
        
        List<Event> filteredEvents = new ArrayList<>();
        
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
