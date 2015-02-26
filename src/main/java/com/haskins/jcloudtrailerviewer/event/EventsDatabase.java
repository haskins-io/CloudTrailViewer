/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.panel.StatusBarPanel;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author mark.haskins
 */
public class EventsDatabase implements EventLoaderListener {
    
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
                    
                    EventUtils.addTimestamp(event);
                    
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
    
    @Override
    public void finishedLoading() { }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
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
