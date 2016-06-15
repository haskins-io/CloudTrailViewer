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
import com.haskins.cloudtrailviewer.sidebar.resourcemetadata.ResourceMetaData;
import static com.haskins.cloudtrailviewer.utils.ChartUtils.entriesSortedByValues;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

/**
 * Chart that display custom information for specific API calls.
 * 
 * @author mark
 */
public class ResourcesChart extends AbstractChart {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static final String PACKAGE = "com.haskins.cloudtrailviewer.sidebar.resourcemetadata.";
    private static final long serialVersionUID = -8842082114125098174L;

    private final Map<String, String> resourceTypes = new HashMap<>();

    private final JMenu sourceMenu = new JMenu("Meta Data");

    private String lastResouce = "";

    public ResourcesChart(EventDatabase eventDatabase, EventTablePanel eventTable) {

        super(eventDatabase, eventTable);

        resourceTypes.put("RunInstances", "RunInstanceMetaData");
        resourceTypes.put("CreateDBInstance", "CreateDbInstanceMetaData");
        resourceTypes.put("CreateCacheCluster", "CreateCacheClusterMetaData");
        resourceTypes.put("Default", "DefaultMetaData");
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract methods implementation
    //////////////////////////////////////////////////////////////////////////// 
    @Override
    public void addCustomMenu() { }

    @Override
    protected void update() {

        if (eventDb.size() == 0) {
            return;
        }

        Event event = eventDb.getEventByIndex(0);
        String eventName = event.getEventName();

        if (!resourceTypes.containsKey(eventName)) {
            eventName = "Default";
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

            int count = 0;
            for (String menuItem : resource.getMenuItems()) {

                JRadioButtonMenuItem mnuItem = new JRadioButtonMenuItem(menuItem);
                mnuItem.setActionCommand(menuItem);
                mnuItem.addActionListener(this);
                
                count++;
                if (count == 1) {
                    mnuItem.setSelected(true);
                }

                sourceMenu.add(mnuItem);
                customGroup.add(mnuItem);
            }
           
            menu.add(sourceMenu);
            
            lastResouce = eventName;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.WARNING, "Problem updating Chart menu", ex);
        }
    }

    private void updateChart(String eventName) {

        try {

            String fqcp = PACKAGE + resourceTypes.get(eventName);

            Class c = Class.forName(fqcp);

            String source = customGroup.getSelection().getActionCommand();
            
            Map<String, Integer> metadataByOccurance = new HashMap<>();
            for (Event event : eventDb.getEvents()) {

                ResourceMetaData md = (ResourceMetaData) c.newInstance();
                md.populate(event);

                int count = 0;
                List<String> values = md.getValuesForMenuItem(source);
                if (values != null) {
                    for (String value : values) {

                        if (metadataByOccurance.containsKey(value)) {
                            count = metadataByOccurance.get(value);
                        }

                        count++;
                        metadataByOccurance.put(value, count);
                    }
                }
            }
            
            List<Map.Entry<String,Integer>> sorted = entriesSortedByValues(metadataByOccurance);
            
            int top = getTopXValue();
            List<Map.Entry<String,Integer>> topX = chartUtils.getTopX(sorted, top);
            
            updateChart(topX);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.WARNING, "Problem updating chart", ex);
        }
    }
}
