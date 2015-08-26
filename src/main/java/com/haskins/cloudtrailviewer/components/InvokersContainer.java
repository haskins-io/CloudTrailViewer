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
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class InvokersContainer extends OverviewContainer {
    
    private final Icon user_icon;
    private final Icon server_icon;
    
    private final JPanel usersPanel = new JPanel();
    private final JPanel rolesPanel = new JPanel();
    
    public InvokersContainer(Feature parent) {
        
        super(parent);
        
        this.setLayout(new GridLayout(1,2));
        
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));        
        rolesPanel.setLayout(new BoxLayout(rolesPanel, BoxLayout.Y_AXIS));
        
        this.add(usersPanel);
        this.add(rolesPanel);
        
        user_icon = ToolBarUtils.getIcon("User-32.png");
        server_icon = ToolBarUtils.getIcon("Server-32.png");
    }
    
    public void addEvent(Event event) {

        String type = event.getUserIdentity().getType();
        if (type.equalsIgnoreCase("IAMUser")) {

            addUser(event);

        } else if (type.equalsIgnoreCase("AssumedRole"))  {

            addRole(event);
        }
    }
    
    @Override
    public void finishedLoading() {

        usersPanel.removeAll();
        rolesPanel.removeAll();
        
        List<Map.Entry<String, NameValuePanel>> sortedPanels = entriesSortedByValues(eventsMap);
        for (Map.Entry<String, NameValuePanel> aPanel : sortedPanels) {
            
            String service = aPanel.getKey();
            NameValuePanel panel = eventsMap.get(service);
            
            Event event = panel.getSampleEvent();
            String type = event.getUserIdentity().getType();
            if (type.equalsIgnoreCase("IAMUser")) {

                usersPanel.add(panel); 

            } else if (type.equalsIgnoreCase("AssumedRole"))  {

                rolesPanel.add(panel); 
            }
        }
        
        usersPanel.add(Box.createVerticalGlue()); 
        usersPanel.add(new JLabel("Test"));
        
        rolesPanel.add(Box.createVerticalGlue()); 
        rolesPanel.add(new JLabel());
        
        this.revalidate();
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
            usersPanel.add(resourcePanel); 
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
                rolesPanel.add(resourcePanel); 

            }
            else {

                resourcePanel = eventsMap.get(role);
                resourcePanel.addEvent(event);
            }
        } 
    }
}
