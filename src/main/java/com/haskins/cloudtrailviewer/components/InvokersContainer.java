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
package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import javax.swing.Icon;

/**
 *
 * @author mark.haskins
 */
public class InvokersContainer extends OverviewContainer {
    
    private final Icon user_icon;
    private final Icon server_icon;
    
    public InvokersContainer(Feature parent) {
        super(parent);
        
        user_icon = ToolBarUtils.getIcon("User-32.png");
        server_icon = ToolBarUtils.getIcon("Server-32.png");
    }
    
    @Override
    public void addEvent(Event event) {

        String type = event.getUserIdentity().getType();
        if (type.equalsIgnoreCase("IAMUser")) {

            addUser(event);

        } else if (type.equalsIgnoreCase("AssumedRole"))  {

            addRole(event);
        }
            
    }
    
    private void addUser(Event event) {
        
        String username = event.getUserIdentity().getUserName();
        if (username == null) {
            username = event.getUserIdentity().getPrincipalId();
        }

        final NameValuePanel resourcePanel;
        
        if (!eventsMap.containsKey(username)) {

            resourcePanel = new NameValuePanel(username, user_icon, feature);
            resourcePanel.addEvent(event);
            
            eventsMap.put(username, resourcePanel);
            this.add(resourcePanel); 
        }
        else {

            resourcePanel = eventsMap.get(username);
            resourcePanel.addEvent(event);
        }
    }
    
    private void addRole(Event event) {
        
        boolean was_role = true;

        if (event.getEventName().equalsIgnoreCase("ConsoleLogin")) {
            was_role = false;
            addUser(event);
        }

        String role;
        if (event.getUserIdentity().getSessionContext() != null) {
            role = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();

        }
        else {
            role = event.getUserIdentity().getPrincipalId();
        }

        if (was_role) {

            final NameValuePanel resourcePanel;
            
            if (!eventsMap.containsKey(role)) {

                resourcePanel = new NameValuePanel(role, server_icon, feature);
                resourcePanel.addEvent(event);

                eventsMap.put(role, resourcePanel);
                this.add(resourcePanel); 

            }
            else {

                resourcePanel = eventsMap.get(role);
                resourcePanel.addEvent(event);
            }
        } 
    }
    
}
