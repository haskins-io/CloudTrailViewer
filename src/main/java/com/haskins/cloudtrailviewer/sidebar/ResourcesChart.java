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
import com.haskins.cloudtrailviewer.model.resource.ResourceMetaData;
import static com.haskins.cloudtrailviewer.utils.ChartUtils.entriesSortedByValues;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author mark
 */
public class ResourcesChart extends AbstractChart implements ActionListener {

    private static final String PACKAGE = "com.haskins.cloudtrailviewer.model.resource.";

    private final Map<String, String> resourceTypes = new HashMap<>();

    private final JMenu sourceMenu = new JMenu("Meta Data");

    private String lastResouce = "";

    public ResourcesChart(EventDatabase eventDatabase, EventTablePanel eventTable) {

        super(eventDatabase, eventTable);

        resourceTypes.put("RunInstances", "Ec2ResourceMetaData");
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

        if (!resourceTypes.containsKey(eventName)) {
            return;
        }

        if (!eventName.equalsIgnoreCase(lastResouce)) {
            updateMenu(eventName);
        }

        updateChart(eventName);
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

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void updateMenu(String eventName) {

        try {

            sourceMenu.removeAll();

            String fqcp = PACKAGE + resourceTypes.get(eventName);

            Class c = Class.forName(fqcp);
            ResourceMetaData resource = (ResourceMetaData) c.newInstance();

            for (String menuItem : resource.getMenuItems()) {

                JRadioButtonMenuItem mnuItem = new JRadioButtonMenuItem(menuItem);
                mnuItem.setActionCommand(menuItem);
                mnuItem.addActionListener(this);

                sourceMenu.add(mnuItem);
            }

            lastResouce = eventName;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ResourcesChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateChart(String eventName) {

        try {

            String fqcp = PACKAGE + resourceTypes.get(eventName);

            Class c = Class.forName(fqcp);

            Map<String, Integer> metadataByOccurance = new HashMap<>();
            for (Event event : eventDb.getEvents()) {

                ResourceMetaData md = (ResourceMetaData) c.newInstance();
                md.populate(event);
                
                String meta = md.getValueForMenuItem(eventName);
                
                int count = 0;
                if (metadataByOccurance.containsKey(meta)) {
                    count = metadataByOccurance.get(meta);
                }
                
                count++;
                metadataByOccurance.put(meta, count);
            }
            
            List<Map.Entry<String,Integer>> sorted = entriesSortedByValues(metadataByOccurance);
            updateChart(sorted);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ResourcesChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
