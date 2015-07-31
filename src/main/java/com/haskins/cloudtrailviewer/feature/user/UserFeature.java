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

package com.haskins.cloudtrailviewer.feature.user;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.thirdparty.SortedListModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mark
 */
public class UserFeature extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "User Feature";
    
    private final Map<String, List<Event>> userMap = new HashMap<>();
    private final SortedListModel userListModel = new SortedListModel<>();
    
    private final Map<String, List<Event>> roleMap = new HashMap<>();
    private final SortedListModel roleListModel = new SortedListModel<>();
    
    private final EventTablePanel eventTable = new EventTablePanel();
        
    public UserFeature() {
                
        buildUI();
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "User-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "User Overview";
    }
    
    @Override
    public String getName() {
        return UserFeature.NAME;
    }
    
    @Override
    public void will_hide() { }
    
    @Override
    public void will_appear() { }
    
    @Override
    public void showEventsTable(List<Event> events) {
                
        eventTable.clearEvents();
        eventTable.setEvents(events);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
                 
        String type = event.getUserIdentity().getType();
        if (type.equalsIgnoreCase("IAMUser") || type.equalsIgnoreCase("AssumedRole")) {
            
            if (type.equalsIgnoreCase("IAMUser")) {
                addUser(event);
            } else {
                addRole(event);
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void addUser(Event event) {
        
        String username = event.getUserIdentity().getUserName();
        if (username == null) {
            username = event.getUserIdentity().getPrincipalId();
        }
        
        List<Event> events;
        if (!userMap.containsKey(username)) {

            events = new ArrayList();
            userMap.put(username, events);
            userListModel.add(username);

        } else {
            events = userMap.get(username);
        } 
        
        events.add(event);
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
            
        } else {
            role = event.getUserIdentity().getPrincipalId();
        }

        if (was_role) {
            List<Event> events;
            if (!roleMap.containsKey(role)) {

                events = new ArrayList();
                roleMap.put(role, events);
                roleListModel.add(role);

            } else {
                events = roleMap.get(role);
            }

            events.add(event);
        }
    }
    
    private void buildUI() {

        final JList userList = new JList(userListModel);
        userList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                          
                String selected = (String)userList.getSelectedValue();
                
                eventTable.clearEvents();
                eventTable.setEvents(userMap.get(selected));
            }
        });
        
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(-1);

        JScrollPane userPane = new JScrollPane(userList);
        userPane.setMinimumSize(new Dimension(300, 400));
        userPane.setPreferredSize(new Dimension(300, 400));
        userPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        final JList roleList = new JList(roleListModel);
        roleList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                          
                String selected = (String)roleList.getSelectedValue();
                
                eventTable.clearEvents();
                eventTable.setEvents(roleMap.get(selected));
            }
        });
        
        roleList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        roleList.setLayoutOrientation(JList.VERTICAL);
        roleList.setVisibleRowCount(-1);

        JScrollPane rolePane = new JScrollPane(roleList);
        rolePane.setMinimumSize(new Dimension(300, 400));
        rolePane.setPreferredSize(new Dimension(300, 400));
        rolePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.add("Users", userPane);
        tabbedPane.add("Roles", rolePane);
        
        eventTable.setVisible(true);
        
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tabbedPane, eventTable);
        jsp.setBackground(Color.WHITE);
        jsp.setDividerSize(3);

        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0)); 
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
}