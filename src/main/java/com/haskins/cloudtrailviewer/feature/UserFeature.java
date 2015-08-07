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

package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.servicespanel.ServiceOverviewContainer;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.NameValueModel;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.CountComparator;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
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
    
    private final Map<String, NameValueModel> userMap = new HashMap<>();
    private final DefaultListModel<NameValueModel> userListModel = new DefaultListModel<>();
    
    private final Map<String, NameValueModel> roleMap = new HashMap<>();
    private final DefaultListModel<NameValueModel> roleListModel = new DefaultListModel<>();
    
    private final ServiceOverviewContainer servicesContainer;
    private final EventTablePanel eventTable = new EventTablePanel();
        
    public UserFeature() {
                
        servicesContainer = new ServiceOverviewContainer(this);
        buildUI();
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
    
        GeneralUtils.orderListByComparator(userListModel, new CountComparator()); 
        GeneralUtils.orderListByComparator(roleListModel, new CountComparator());
    }

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

        if (!userMap.containsKey(username)) {

            NameValueModel model = new NameValueModel(username, event);
            userMap.put(username, model);
            userListModel.addElement(model);
           
        } else {

            NameValueModel model = userMap.get(username);
            model.addEvent(event);
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

        } else {
            role = event.getUserIdentity().getPrincipalId();
        }

        if (was_role) {

            if (!roleMap.containsKey(role)) {

                NameValueModel model = new NameValueModel(role, event);
                roleMap.put(role, model);
                roleListModel.addElement(model);

            } else {

                NameValueModel model = roleMap.get(role);
                model.addEvent(event);
            }
        }
    }
    
    private void buildUI() {

        final JList userList = new JList(userListModel);
        userList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                          
                NameValueModel model = (NameValueModel)userList.getSelectedValue();
                
                eventTable.clearEvents();
                eventTable.setEvents(model.getEvents());
                
                servicesContainer.setEvents(model.getEvents());
            }
        });
        
        userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(-1);
        userList.setCellRenderer(new DefaultListCellRenderer() {
            
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                NameValueModel model = (NameValueModel)value;
                label.setText(model.getName() + " : " + model.getNumberOfEvents());
                
                return label;
            }
        });

        JScrollPane userPane = new JScrollPane(userList);
        userPane.setMinimumSize(new Dimension(300, 400));
        userPane.setPreferredSize(new Dimension(300, 400));
        userPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        final JList roleList = new JList(roleListModel);
        roleList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                          
                NameValueModel model = (NameValueModel)roleList.getSelectedValue();
                
                eventTable.clearEvents();
                eventTable.setEvents(model.getEvents());
                
                servicesContainer.setEvents(model.getEvents());
            }
        });
        
        roleList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        roleList.setLayoutOrientation(JList.VERTICAL);
        roleList.setVisibleRowCount(-1);
        roleList.setCellRenderer(new DefaultListCellRenderer() {
            
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                NameValueModel model = (NameValueModel)value;
                label.setText(model.getName() + " : " + model.getNumberOfEvents());
                
                return label;
            }
        });

        JScrollPane rolePane = new JScrollPane(roleList);
        rolePane.setMinimumSize(new Dimension(300, 400));
        rolePane.setPreferredSize(new Dimension(300, 400));
        rolePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.add("Users", userPane);
        tabbedPane.add("Roles", rolePane);
        
        eventTable.setVisible(true);
        
        JPanel detailPanel = new JPanel(new GridLayout(2,1));
        detailPanel.add(servicesContainer);
        detailPanel.add(eventTable);
        
        JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tabbedPane, detailPanel);
        jsp.setBackground(Color.WHITE);
        jsp.setDividerSize(3);

        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0)); 
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
}