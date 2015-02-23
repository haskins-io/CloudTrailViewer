package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author mark
 */
public class EventUtils {
    
    public static <K,V extends Comparable<? super V>> 
                List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries, 
            new Comparator<Entry<K,V>>() {
                @Override
                public int compare(Entry<K,V> e1, Entry<K,V> e2) {
                    return e2.getValue().compareTo(e1.getValue());
                }
            }
        );

        return sortedEntries;
    }
    
    public List<Entry<String,Integer>> getRequiredEvents(List<Event> masterEvents, ChartData chartData) {
                
        List<Entry<String,Integer>> events = getEventsBySource(masterEvents, chartData);
        
        if (events != null && chartData.getChartType().equalsIgnoreCase("Top")) {
            events = getTopX(events, chartData);
        }
        
        return events;
    }
    
    private List<Entry<String,Integer>> getEventsBySource(List<Event> masterEvents, ChartData chartData) {
        
        Map<String, Integer> eventsByOccurance = new HashMap<>();
        
        for (Event event : masterEvents) {
            
            if (chartData.isIgnoreRoot() && isRootEvent(event)) {
                continue;
            }
            
            String property = getEventProperty(chartData.getChartSource(), event);

            if (property != null) {

                int count = 1;
                if (eventsByOccurance.containsKey(property)) {
                    count = eventsByOccurance.get(property);
                    count++;
                }
                eventsByOccurance.put(property, count);
            }
        }
        
        if (!eventsByOccurance.isEmpty()) {
            return entriesSortedByValues(eventsByOccurance);
        } else {
            return null;
        }
    }
    
    private List<Entry<String,Integer>> getTopX(List<Entry<String,Integer>> sorted, ChartData chartData) {
        
       List<Entry<String,Integer>> top = new ArrayList<>();
        
        int count = chartData.getTop();
        if (sorted.size() < count) {
            count = sorted.size();
        }
        
        for (int i=0; i<count; i++) {
            
            top.add(sorted.get(i));
        }
        
        return top;
    }
    
    private String getEventProperty(String property, Event event) {
        
        String requiredValue;
        
        switch(property) {
            
            case "eventSource":
                requiredValue = event.getEventSource();
                break;
            case "eventName":
                requiredValue = event.getEventName();
                break;
            case "sourceIPAddress":
                requiredValue = event.getSourceIPAddress();
                break;
            case "userAgent":
                requiredValue = event.getUserAgent();
                break;
            case "principalId":
                requiredValue = event.getUserIdentity().getPrincipalId();
                break;
            case "arn":
                requiredValue = event.getUserIdentity().getArn();
                break;
            case "userName":
                requiredValue = event.getUserIdentity().getUserName();
                break;
            case "invokedBy":
                requiredValue = event.getUserIdentity().getInvokedBy();
                break;
            default:
                requiredValue = "Not Supported";
        }
        
        return requiredValue;
    }
    
    private boolean isRootEvent(Event event) {
        
        boolean isRootEvent = false;
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("root")) {
            isRootEvent = true;
        }
        
        return isRootEvent;
    }
}
