package com.github.githublemming.jcloudtrailerviewer.event;

import com.github.githublemming.jcloudtrailerviewer.filter.Filters;
import com.github.githublemming.jcloudtrailerviewer.filter.FiltersListener;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public class EventsDatabase implements EventLoaderListener, FiltersListener {
    
    private final List<Event> masterEventsMap = new ArrayList<>();
    private final List<EventsDatabaseListener> listeners = new ArrayList<>();
    
    private final Filters filters;
     
    public EventsDatabase(EventLoader eventLoader, Filters filters) {
        
        this.filters = filters;
        this.filters.addListener(this);
        
        eventLoader.addListener(this);
    }
    
    public void addListeners(EventsDatabaseListener listener) {
        this.listeners.add(listener);
    }
    
    public int size()
    {
        return masterEventsMap.size();
    }
    
    public Event getRecordByIndex(int rowIndex)
    {
        return masterEventsMap.get(rowIndex);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {
            
        for(Event event : events) {
            masterEventsMap.add( event);
        }
        
        for (EventsDatabaseListener listener : listeners) {
            listener.onEventsUpdated(masterEventsMap);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventFilterListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
        
        List<Event> filteredEvents = filters.filterEvents(masterEventsMap);
        
        for (EventsDatabaseListener listener : listeners) {
            listener.onEventsUpdated(filteredEvents);
        }
    }
}
