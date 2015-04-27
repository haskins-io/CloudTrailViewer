/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.jcloudtrailerviewer.table;

import com.haskins.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark
 */
public class TableUtils {
    
    /**
     * Returns the username for the event.
     * @param event Event to check
     * @return
     */
    public static String getUserName(Event event) {
        
        String username;
        
        if (event.getUserIdentity().getUserName() != null &&  event.getUserIdentity().getUserName().length() > 0) {
            
            String arn = event.getUserIdentity().getArn();
            int lastSlash = arn.lastIndexOf(":");
            username = arn.substring(lastSlash + 1);
            
        } else {
            String arn = event.getUserIdentity().getArn();
            int lastSlash = arn.lastIndexOf("/");
            username = arn.substring(lastSlash + 1);
        }
        
        return username;
    }
    
    public static String getService(Event event) {
        
        String source = event.getEventSource();
        int periodPos = source.indexOf(".");
        
        return source.substring(0,periodPos);
    }
}
