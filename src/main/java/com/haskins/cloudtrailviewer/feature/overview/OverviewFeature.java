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

package com.haskins.cloudtrailviewer.feature.overview;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.thirdparty.WrapLayout;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.table.TableUtils;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.awt.BorderLayout;
import java.awt.Color;
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
public class OverviewFeature extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "Service Overview";
    
    private final Map<String, OverviewPanel> servicesMap = new HashMap<>();
    
    private final JPanel servicesContainer = new JPanel(new WrapLayout());
    private final EventTablePanel eventTable = new EventTablePanel();
    
    private JSplitPane jsp;
    
    public OverviewFeature(FilteredEventDatabase eventsDatabase) {
        
        eventsDatabase.addListener(this);
        
        buildUI();
    }
    
    public void showEventsTable(List<Event> events) {
        
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
    public void eventLoadingComplete() { }

    @Override
    public boolean providesSideBar() {
        return false;
    }

    @Override
    public void toggleSideBar() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Service-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "Service API Overview";
    }
    
    @Override
    public String getName() {
        return OverviewFeature.NAME;
    }
    
    @Override
    public void is_hidden() { }
    
    @Override
    public void is_visible() { }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
                 
        String serviceName = TableUtils.getService(event);
        
        final OverviewPanel servicePanel;
        
        if (!servicesMap.containsKey(serviceName)) {
            
            servicePanel = new OverviewPanel(serviceName, this);
            
            servicesMap.put(serviceName, servicePanel);
            
            servicesContainer.removeAll();
            
            Set keys = servicesMap.keySet();
            List<String> sorted = GeneralUtils.asSortedList(keys);
            for (String service : sorted) {
                OverviewPanel panel = servicesMap.get(service);
                servicesContainer.add(panel);
            }            
        } else {
            servicePanel = servicesMap.get(serviceName);
        }
        
        servicePanel.addEvent(event);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        servicesContainer.setBackground(Color.white);
        JScrollPane sPane = new JScrollPane(servicesContainer);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        eventTable.setVisible(false);
        
        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sPane, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
   
}