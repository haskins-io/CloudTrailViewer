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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.text.SimpleDateFormat;

/**
 * Collection of Event related utility methods
 * 
 * @author mark
 */
public class EventUtils {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            
    /**
     * Takes EventTime value from Event, converts it to a long and adds it back
     * onto the event
     * @param event Event to be proceessed
     */
    public static void addTimestamp(Event event) {
        event.setTimestamp(getTimestamp(event.getEventTime()));
    }
    
    /**
     * Converts passed String dateTime to long
     * @param dateString
     * @return 
     */
    public static long getTimestamp(String dateString) {
        
        long millis = 0;
        
        try {
            millis = sdf.parse(dateString).getTime();
        } catch (Exception ex) { } 
        
        return millis;
    }
    
    /**
     * Takes the passed Events and converts it to a JSON document and adds it to
     * the passed Event.
     * @param event 
     */
    public static void addRawJson(Event event) {
        
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        event.setRawJSON(g.toJson(event));
    } 
}
