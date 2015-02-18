package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark.haskins
 */
public class EventsDatabase implements EventLoaderListener {
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    private final CopyOnWriteArrayList<Event> masterEvents = new CopyOnWriteArrayList<>();
    
    public int size()
    {
        return masterEvents.size();
    }
    
    public Event getRecordByIndex(int rowIndex)
    {
        return masterEvents.get(rowIndex);
    }
    
    public List<Event> getEvents() {
        return masterEvents;
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
    }
}
