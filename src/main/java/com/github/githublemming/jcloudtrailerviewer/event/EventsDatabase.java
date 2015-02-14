package com.github.githublemming.jcloudtrailerviewer.event;

import com.github.githublemming.jcloudtrailerviewer.filter.Filters;
import com.github.githublemming.jcloudtrailerviewer.filter.FiltersListener;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark.haskins
 */
public class EventsDatabase implements EventLoaderListener, FiltersListener {
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    private final CopyOnWriteArrayList<Event> masterEvents = new CopyOnWriteArrayList<>();
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
        return masterEvents.size();
    }
    
    public Event getRecordByIndex(int rowIndex)
    {
        return masterEvents.get(rowIndex);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(final List<Event> events) {
            
        for(Event event : events) {
            masterEvents.add( event);
        }
        
        Thread createRawJSONForEvent = new Thread() {
            @Override
            public void run() {
                for (Event event : events) {

                    String rawJson;
                    try {
                        rawJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(event);
                        event.setRawJSON(rawJson);

                    } catch (IOException ex) {
                        Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
            }
        };
        createRawJSONForEvent.start();
        
        for (EventsDatabaseListener listener : listeners) {
            listener.onEventsUpdated(masterEvents);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventFilterListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
        
        CopyOnWriteArrayList<Event> filteredEvents = filters.filterEvents(masterEvents);
        
        for (EventsDatabaseListener listener : listeners) {
            listener.onEventsUpdated(filteredEvents);
        }
    }
}
