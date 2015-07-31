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
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import com.haskins.cloudtrailviewer.utils.TimeStampComparator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author mark
 */
public class UserFeature extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "User Overview";
    
    private final Map<String, UserPanel> userMap = new HashMap<>();
    private final GridBagConstraints gbc = new GridBagConstraints();
    
    private final JPanel userOverviewPanel = new JPanel();
    private final EventTablePanel eventTable = new EventTablePanel();
    private JSplitPane jsp;
    
    boolean sorted = false;
    
    public UserFeature(FilteredEventDatabase eventsDatabase) {
        
        eventsDatabase.addListener(this);
        buidUI();
    }
    
    private void buidUI() {
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        userOverviewPanel.setLayout(new GridBagLayout());
        userOverviewPanel.setBackground(Color.white);
        userOverviewPanel.setOpaque(true);
       
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JScrollPane sPane = new JScrollPane(userOverviewPanel);
        sPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        sPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        
        eventTable.setVisible(false);
        
        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sPane, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
    
    public void showEvents(List<Event> events) {
        
        if (!sorted) {
            Collections.sort(events, new TimeStampComparator());
        }
       
        if (!eventTable.isVisible()) {
            jsp.setDividerLocation(0.5);
            jsp.setDividerSize(3); 
            eventTable.setVisible(true);
        }

        eventTable.clearEvents();
        eventTable.setEvents(events);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {

    }

    @Override
    public boolean providesSideBar() {
        return false;
    }

    @Override
    public void toggleSideBar() {
        
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
        return "View Events performs by an IAM User";
    }
    
    @Override
    public String getName() {
        return UserFeature.NAME;
    }
    
    @Override
    public void will_hide() { }
    
    @Override
    public void will_appear() {
        
        Set<String> keys = userMap.keySet();
        List<String> sorted = GeneralUtils.asSortedList(keys);
        
        int count = 1;
        for (String userName : sorted) {
            
            if (count == sorted.size()) {
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
            }
            
            UserPanel component = userMap.get(userName);
            component.buildUI();
            
            userOverviewPanel.add(component, gbc);
            
            count++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("IAMUser")) {
            
            String username = event.getUserIdentity().getUserName();
            UserPanel component = userMap.get(username);
            if (component == null) {
                component = new UserPanel(this, username);
                userMap.put(username, component);
            }
            
            component.addEvent(event);
        }
    }
}
