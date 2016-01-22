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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for processing events into a format that can be used to create
 * a chart
 * @author mark
 */
public class ChartUtils {
    
    /**
     * 
     * @param <K>
     * @param <V>
     * @param map Map of Events to sort
     * @return 
     */
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
    
    /**
     * Returns the Top X Events sorted by value
     * @param events Events to process
     * @param top The Top value required
     * @param eventField The field to process against e.g. EventName
     * @return a Map keys by EventField that has the count of events that match
     */
    public static List<Map.Entry<String,Integer>> getTopEvents(List<Event> events, int top, String eventField) {
        
        Map<String, Integer> eventsByOccurance = new HashMap<>();
        
        for (Event event : events) {
            
            String fieldValue = EventUtils.getEventProperty(eventField, event);

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
    
    public static List<Map.Entry<String,Integer>> getTopX(List<Map.Entry<String,Integer>> sorted, int top) {
        
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
}
