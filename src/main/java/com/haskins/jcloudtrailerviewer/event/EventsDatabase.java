package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.panel.StatusBarPanel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private final List<Event> errorsEvents = new ArrayList<>();
    private final List<Event> iamEvents = new ArrayList<>();
    private final List<Event> securityEvents = new ArrayList<>();
    
    private final Map<String, Integer> eventsPerService = new HashMap<>();
    
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
    
    public List<Event> getErrorEvents() {
        return errorsEvents;
    }
    
    public List<Event> getIamEvents() {
        return iamEvents;
    }
    
    public List<Event> getSecurityEvents() {
        return securityEvents;
    }    
    
    public Map<String, Integer> getEventsPerService() {
        return eventsPerService;
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(final List<Event> events) {
            
        for(Event event : events) {
            masterEvents.add(event);
        }
        
        Thread createRawJSONForEvent = new Thread() {
            @Override
            public void run() {
                
                for (Event event : events) {

                    // Create RAW JSON String for Filtering <- probably get rid of this and add create more filters
                    String rawJson;
                    try {
                        rawJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(event);
                        event.setRawJSON(rawJson);

                    } catch (IOException ex) {
                        Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                    // sort events by AWS Service
                    int posPeriod = event.getEventSource().indexOf(".");
                    String service = event.getEventSource().substring(0, posPeriod);
                    
                    int count = 0;
                    if (eventsPerService.containsKey(service)) {
                        count = eventsPerService.get(service);
                    }
                    
                    count++;
                    eventsPerService.put(service, count);
                    
                    
                    // Check for warning
                    if (event.getErrorCode().length() > 1) {
                        errorsEvents.add(event);
                        StatusBarPanel.getInstance().showErrorWarning();
                    }
                } 
            }
        };
        createRawJSONForEvent.start();
    }
}
