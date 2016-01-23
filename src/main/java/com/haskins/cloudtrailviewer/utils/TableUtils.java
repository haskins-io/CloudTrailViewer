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

import com.haskins.cloudtrailviewer.core.AwsService;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Table Utility Methods
 * 
 * @author mark
 */
public class TableUtils implements Serializable {

    private static final long serialVersionUID = 6462957031303627906L;
    
    private final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        
    /**
     * Returns the username for the event.
     * @param event Event to check
     * @return
     */
    public String getInvokedBy(Event event) {
        
        String username;
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("IAMUser")) {
            username = event.getUserIdentity().getUserName();
            
        } else if (event.getUserIdentity().getType().equalsIgnoreCase("AssumedRole")) {
            
            if (event.getUserIdentity().getSessionContext() != null) {
                username = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();
                
            } else {
                
                String arn = event.getUserIdentity().getArn();
                int pos = arn.lastIndexOf('/');
                
                username = arn.substring(pos);
            }
                        
        } else if (event.getUserIdentity().getType().equalsIgnoreCase("FederatedUser")) {
            username = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();
            
        } else if (event.getUserIdentity().getType().equalsIgnoreCase("Root")) {
            
            if (event.getUserIdentity().getInvokedBy().contains(".amazonaws.com")) {
                
                String tmp = event.getUserIdentity().getInvokedBy();
                tmp = tmp.replaceFirst(".amazonaws.com", "");
                username = AwsService.getInstance().getFriendlyName(tmp);
                
            } else {
                username = event.getUserIdentity().getInvokedBy();
            }
            
        } else {
            username = "";
        }
        
        return username;
    }
    
    /**
     * Returns the name of the AWS service reference in the Event. This returns
     * a friendly name e.g. AWS AutoScaling and not autoscaling.amazonaws.com
     * 
     * @param event
     * @return 
     */
    public String getService(Event event) {
        
        return getServiceFromEventSource(event.getEventSource());
    }
    
    public String getServiceFromEventSource(String eventSource) {
        
        String tmp = eventSource.replaceFirst(".amazonaws.com", "");
        return AwsService.getInstance().getFriendlyName(tmp); 
    }
    
    public String getFormatedDateTime(long millis) {
        return SIMPLE_DATE_FORMATTER.format(millis);
    }
}
