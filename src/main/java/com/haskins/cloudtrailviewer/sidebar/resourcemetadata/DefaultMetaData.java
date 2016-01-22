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

package com.haskins.cloudtrailviewer.sidebar.resourcemetadata;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Arrays;

/**
 *
 * @author mark.haskins
 */
public class DefaultMetaData implements ResourceMetaData {
    
    private String eventSource;
    private String userAgent;
    private String userIdentityPrincipalId;
    private String userIdentityArn;
    private String userIdentityUsername;
    private String userIdentityInvokedBy;
    private String sessionContextPrincipalId;
    private String sessionContextArn;
    private String sessionContextUsername;
    
    private static final String[] MENU_ITEMS = new String[] {
        "Event Source", 
        "User Agent",
        "User Identity : Principal Id",
        "User Identity : Arn",
        "User Identity : Username",
        "User Identity : Invoked By",
        "Session Context : Arn",
        "Session Context : Principal Id",
        "Session Context : Username"
    };
    
    @Override
    public void populate(Event event) {
        
        eventSource = event.getEventSource();
        userAgent = event.getUserAgent();
        
        if (event.getUserIdentity() != null) {
            
            userIdentityPrincipalId = event.getUserIdentity().getPrincipalId();
            userIdentityArn = event.getUserIdentity().getArn();
            userIdentityUsername = event.getUserIdentity().getUserName();
            userIdentityInvokedBy = event.getUserIdentity().getInvokedBy();

            if (event.getUserIdentity().getSessionContext() != null  && event.getUserIdentity().getSessionContext().getSessionIssuer() != null) {
                
                sessionContextPrincipalId = event.getUserIdentity().getSessionContext().getSessionIssuer().getPrincipalId();
                sessionContextArn = event.getUserIdentity().getSessionContext().getSessionIssuer().getArn();
                sessionContextUsername = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();   
            }
        }
    }
    
    @Override
    public String[] getMenuItems() {
        return Arrays.copyOf(MENU_ITEMS, MENU_ITEMS.length);
    }
    
    @Override
    public String getValueForMenuItem(String menuItem) {
        
        String value = "";
        
        if (menuItem.equalsIgnoreCase("Event Source")) {
            value = this.eventSource;
            
        } else if (menuItem.equalsIgnoreCase("User Agent")) {
            value = this.userAgent;
            
        } else if (menuItem.equalsIgnoreCase("User Identity : Principal Id")) {
            value = this.userIdentityPrincipalId;
            
        } else if (menuItem.equalsIgnoreCase("User Identity : Arn")) {
            value = this.userIdentityArn;
            
        } else if (menuItem.equalsIgnoreCase("User Identity : Username")) {
            value = userIdentityUsername;
            
        } else if (menuItem.equalsIgnoreCase("User Identity : Invoked By")) {
            value = this.userIdentityInvokedBy;
            
        } else if (menuItem.equalsIgnoreCase("Session Context : Arn")) {
            value = this.sessionContextPrincipalId;
            
        } else if (menuItem.equalsIgnoreCase("Session Context : Principal Id")) {
            value = this.sessionContextArn;
            
        } else if (menuItem.equalsIgnoreCase("Session Context : Username")) {
            value = this.sessionContextUsername;
        }
        
        return value;
    }
}
