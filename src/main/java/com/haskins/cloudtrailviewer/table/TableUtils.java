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

package com.haskins.cloudtrailviewer.table;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.text.SimpleDateFormat;

/**
 *
 * @author mark
 */
public class TableUtils {
    
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        
    /**
     * Returns the username for the event.
     * @param event Event to check
     * @return
     */
    public static String getInvokedBy(Event event) {
        
        String username;
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("IAMUser")) {
            username = event.getUserIdentity().getUserName();
            
        } else if (event.getUserIdentity().getType().equalsIgnoreCase("AssumedRole")) {
            
            if (event.getUserIdentity().getSessionContext() != null) {
                username = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();
            } else {
                username = event.getUserIdentity().getPrincipalId();
            }
                        
        } else if (event.getUserIdentity().getType().equalsIgnoreCase("FederatedUser")) {
            username = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();
            
        } else if (event.getUserIdentity().getType().equalsIgnoreCase("Root")) {
            username = event.getUserIdentity().getInvokedBy();
            
        } else {
            username = "";
        }
        
        return username;
    }
    
    public static String getService(Event event) {
        
        String source = event.getEventSource();
        int periodPos = source.indexOf(".");
        
        return source.substring(0,periodPos);
    }
    
    public static String getFormatedDateTime(long millis) {
        return sdf2.format(millis);
    }
}
