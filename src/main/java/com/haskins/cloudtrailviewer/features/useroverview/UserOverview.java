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

package com.haskins.cloudtrailviewer.features.useroverview;

import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.features.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author mark
 */
public class UserOverview extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "User Overview";
    
    private final JPanel userComponentPanel = new JPanel(new GridBagLayout());
    private final GridBagConstraints gbc = new GridBagConstraints();
    
    private final Map<String, UserComponent> userMap = new HashMap<>();
    
    public UserOverview(FilteredEventDatabase eventsDatabase) {
        
        eventsDatabase.addListener(this);
        
        buidUI();
    }
    
    private void buidUI() {
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        userComponentPanel.setBackground(Color.white);
        userComponentPanel.setOpaque(true);
        
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JScrollPane jsp = new JScrollPane(userComponentPanel);
        jsp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jsp.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        
        this.add(jsp, BorderLayout.CENTER);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        
        Set<String> keys = userMap.keySet();
        List<String> sorted = GeneralUtils.asSortedList(keys);
        
        int count = 1;
        for (String userName : sorted) {
            
            if (count == sorted.size()) {
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
            }
            
            UserComponent component = userMap.get(userName);
            component.buildUI();
            
            userComponentPanel.add(component, gbc);
            
            count++;
        }
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
        return UserOverview.NAME;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("IAMUser")) {
            
            String username = event.getUserIdentity().getUserName();
            UserComponent component = userMap.get(username);
            if (component == null) {
                component = new UserComponent(username);
                userMap.put(username, component);
            }
            
            component.addEvent(event);
        }
    }
}
