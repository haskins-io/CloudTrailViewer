/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
