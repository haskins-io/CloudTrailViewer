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

package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.UserIdentity;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark
 */
public class EventUtils {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
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
    
    public static void addTimestamp(Event event) {
        
        event.setTimestamp(getTimestamp(event.getEventTime()));
    }
    
    public static long getTimestamp(String dateString) {
        
        long millis = 0;
        
        try {
            millis = sdf.parse(dateString).getTime();
        } catch (Exception ex) { } 
        
        return millis;
    }
                
    public static void addRawJson(Event event) {
        
        String rawJson;
        try {
            rawJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(event);
            event.setRawJSON(rawJson);

        } catch (IOException ex) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }            
                
    public static List<Entry<String,Integer>> getRequiredEvents(List<Event> masterEvents, ChartData chartData) {
                
        List<Entry<String,Integer>> events = getEventsBySource(masterEvents, chartData);
        
        if (events != null && chartData.getChartType().equalsIgnoreCase("Top")) {
            events = getTopX(events, chartData);
        }
        
        return events;
    }
    
    private static List<Entry<String,Integer>> getEventsBySource(List<Event> masterEvents, ChartData chartData) {
        
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
    
    private static List<Entry<String,Integer>> getTopX(List<Entry<String,Integer>> sorted, ChartData chartData) {
        
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
    
    public static String getEventProperty(String property, Event event) {
        
        String requiredValue = null;
                
        if (property.indexOf(".") > 1) {
            
            String[] parts = property.split(Pattern.quote("."));
            
            Object subClassObj = callMethod(parts[0], event);
            
            if (parts[0].equalsIgnoreCase("userIdentity")) {
                
                UserIdentity userIdentity = (UserIdentity) subClassObj;
                requiredValue = (String) callMethod(parts[1], userIdentity);
            }
            
        } else {
            
            requiredValue = (String) callMethod(property, event);
        }
        
        return requiredValue;
    }
    
    private static Object callMethod(String property, Object reflectionClass) {
        
        Object result;
        
        String camelCaseProperty = property.substring(0, 1).toUpperCase() + property.substring(1);
        
        try {
            String getProperty = "get" + camelCaseProperty;
            Method method = reflectionClass.getClass().getMethod(getProperty);
            
            result = method.invoke(reflectionClass);
            
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            result = null;
        }
        
        return result;
    }
    
    private static boolean isRootEvent(Event event) {
        
        boolean isRootEvent = false;
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("root")) {
            isRootEvent = true;
        }
        
        return isRootEvent;
    }
}
