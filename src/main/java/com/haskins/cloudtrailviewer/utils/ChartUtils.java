/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.utils;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class ChartUtils {
    
    public static <K,V extends Comparable<? super V>>  List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries, 
            new Comparator<Map.Entry<K,V>>() {
                @Override
                public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    return e2.getValue().compareTo(e1.getValue());
                }
            }
        );

        return sortedEntries;
    }
    
    public static List<Map.Entry<String,Integer>> getTopEvents(List<Event> events, int top, String eventField) {
        
        Map<String, Integer> eventsByOccurance = new HashMap<>();
        
        for (Event event : events) {
            
            String fieldValue = getEventProperty(eventField, event);

            if (fieldValue != null) {

                if (eventField.equalsIgnoreCase("EventSource")) {
                    fieldValue = TableUtils.getServiceFromEventSource(fieldValue);
                }
                
                int count = 1;
                if (eventsByOccurance.containsKey(fieldValue)) {
                    count = eventsByOccurance.get(fieldValue);
                    count++;
                }
                eventsByOccurance.put(fieldValue, count);
            }
        }
        
        if (!eventsByOccurance.isEmpty()) {
            
            List<Map.Entry<String,Integer>> sorted = entriesSortedByValues(eventsByOccurance);
            return getTopX(sorted, top);
            
        } else {
            return null;
        }
    }
    
    private static List<Map.Entry<String,Integer>> getTopX(List<Map.Entry<String,Integer>> sorted, int top) {
        
       List<Map.Entry<String,Integer>> topEvents = new ArrayList<>();
        
        int count = top;
        if (sorted.size() < count) {
            count = sorted.size();
        }
        
        for (int i=0; i<count; i++) {
            topEvents.add(sorted.get(i));
        }
        
        return topEvents;
    }
    
    private static String getEventProperty(String property, Object event) {
        
        String requiredValue;
        
        if (property.contains(".")) {
            
            int pos = property.indexOf(".");
            String field = property.substring(0, pos);
             
            Object subClass = callMethod(field, event);
            if (subClass != null) {
                property = property.substring(pos + 1);
                return getEventProperty(property, subClass); 
            } else {
                return null;
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
}
