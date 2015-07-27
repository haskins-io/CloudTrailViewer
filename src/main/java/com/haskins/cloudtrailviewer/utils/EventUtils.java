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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.haskins.cloudtrailviewer.core.EventLoader;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mark
 */
public class EventUtils {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    private static final ObjectMapper mapper = new ObjectMapper();
        
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
            
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            rawJson = mapper.writeValueAsString(event);
            event.setRawJSON(rawJson);

        } catch (IOException ex) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
        } 
    } 
}
