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
    
    private CopyOnWriteArrayList<Event> masterEvents = new CopyOnWriteArrayList<>();
        
    private final Map<String, Integer> eventsPerService = new HashMap<>();
    private final Map<String, Map<String, Integer>> tpsMap = new HashMap<>();
    
    private final List<EventsDatabaseListener> listeners = new ArrayList<>();
    
    public void clear() {
        masterEvents = new CopyOnWriteArrayList<>();
        StatusBarPanel.getInstance().setEventsLoaded(0);
    }
    
    public int size() {
        return masterEvents.size();
    }
    
    public Event getRecordByIndex(int rowIndex) {
        return masterEvents.get(rowIndex);
    }
    
    public List<Event> getEvents() {
        return masterEvents;
    }
      
    public Map<String, Integer> getEventsPerService() {
        return eventsPerService;
    }
    
    public Map<String, Map<String, Integer>> getTransactionsPerService() {
       return tpsMap;
    }
    
    public void addListeners(EventsDatabaseListener listener) {
        listeners.add(listener);
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
                    
                    addRawJson(event);
                    
                    tpsPerService(event);
                    eventsPerService(event);
                    
                    for (EventsDatabaseListener listener : listeners) {
                        listener.onEvent(event);
                    }
                } 
            }
        };
        createRawJSONForEvent.start();
    }
    
    private void addRawJson(Event event) {
        
        String rawJson;
        try {
            rawJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(event);
            event.setRawJSON(rawJson);

        } catch (IOException ex) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private void eventsPerService(Event event) {
        
        int posPeriod = event.getEventSource().indexOf(".");
        String service = event.getEventSource().substring(0, posPeriod);
        
        // sort events by AWS Service
        int count = 0;
        if (eventsPerService.containsKey(service)) {
            count = eventsPerService.get(service);
        }

        count++;
        eventsPerService.put(service, count); 
    }
    
    private void tpsPerService(Event event) {
        
        int posPeriod = event.getEventSource().indexOf(".");
        String service = event.getEventSource().substring(0, posPeriod);
        
        int tpsCount = 1;
        String dateTime = event.getEventTime();
        if (tpsMap.containsKey(service)) {

            Map<String, Integer> serviceTps = tpsMap.get(service);
            if (serviceTps.containsKey(dateTime)) {

                tpsCount = serviceTps.get(dateTime);
                tpsCount++;

                serviceTps.put(dateTime, tpsCount);
                tpsMap.put(service, serviceTps);
            }
            else {

                serviceTps.put(dateTime, tpsCount);  
                tpsMap.put(service, serviceTps);
            }    
        }
        else {

             Map<String, Integer> serviceTps = new HashMap<>();
             serviceTps.put(dateTime, tpsCount);
             tpsMap.put(service, serviceTps);
        }
    }
}
