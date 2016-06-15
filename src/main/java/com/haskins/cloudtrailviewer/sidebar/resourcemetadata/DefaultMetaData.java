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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public class DefaultMetaData implements ResourceMetaData {
    
    private List<String> eventSource = new ArrayList<String>();
    private List<String> userAgent = new ArrayList<String>();
    private List<String> userIdentityPrincipalId = new ArrayList<String>();
    private List<String> userIdentityArn = new ArrayList<String>();
    private List<String> userIdentityUsername = new ArrayList<String>();
    private List<String> userIdentityInvokedBy = new ArrayList<String>();
    private List<String> sessionContextPrincipalId = new ArrayList<String>();
    private List<String> sessionContextArn = new ArrayList<String>();
    private List<String> sessionContextUsername = new ArrayList<String>();
    
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
        
        eventSource.add(event.getEventSource());
        userAgent.add(event.getUserAgent());
        
        if (event.getUserIdentity() != null) {
            
            userIdentityPrincipalId.add(event.getUserIdentity().getPrincipalId());
            userIdentityArn.add(event.getUserIdentity().getArn());
            userIdentityUsername.add(event.getUserIdentity().getUserName());
            userIdentityInvokedBy.add(event.getUserIdentity().getInvokedBy());

            if (event.getUserIdentity().getSessionContext() != null  && event.getUserIdentity().getSessionContext().getSessionIssuer() != null) {
                
                sessionContextPrincipalId.add(event.getUserIdentity().getSessionContext().getSessionIssuer().getPrincipalId());
                sessionContextArn.add(event.getUserIdentity().getSessionContext().getSessionIssuer().getArn());
                sessionContextUsername.add(event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName());
            }
        }
    }
    
    @Override
    public String[] getMenuItems() {
        return Arrays.copyOf(MENU_ITEMS, MENU_ITEMS.length);
    }
    
    @Override
    public List<String> getValuesForMenuItem(String menuItem) {

        List<String> value = new ArrayList<String>();
        
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
