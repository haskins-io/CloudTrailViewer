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
package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabase;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.resource.Ec2ResourceMetaData;
import com.haskins.cloudtrailviewer.model.resource.ResourceMetaData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author mark
 */
public class ResourcesChart extends AbstractChart implements ActionListener {
    
    private final Map<String, ResourceMetaData> resourceTypes = new HashMap<>();
    
    private final JMenu sourceMenu = new JMenu("Meta Data");
    
    public ResourcesChart(EventDatabase eventDatabase, EventTablePanel eventTable) {

        super(eventDatabase, eventTable);
        
        resourceTypes.put("RunInstances", new Ec2ResourceMetaData());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract methods implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void addCustomMenu() {

    }

    @Override
    public void update() {

        if (eventDb.size() == 0) {
            return;
        }
        
        Event event = eventDb.getEventByIndex(0);
        String eventName = event.getEventName();
        
        if(!resourceTypes.containsKey(eventName)) {
            return;
        }
        
        ResourceMetaData resource = resourceTypes.get(eventName);
        
        // change menu options if needed
        sourceMenu.removeAll();
        
        for (String menuItem : resource.getMenuItems()) {
            
            JRadioButtonMenuItem mnuItem = new JRadioButtonMenuItem(menuItem);
            mnuItem.setActionCommand(menuItem);
            mnuItem.addActionListener(this);
            
            sourceMenu.add(mnuItem);
        }
        
        // convert Events requestParameters MAP into usable objects based on Eventname
        
        
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        update();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener Implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }
}
