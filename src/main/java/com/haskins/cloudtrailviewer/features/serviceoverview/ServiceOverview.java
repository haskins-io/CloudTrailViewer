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

package com.haskins.cloudtrailviewer.features.serviceoverview;

import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.features.Feature;
import com.haskins.cloudtrailviewer.layout.WrapLayout;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.table.TableUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author mark
 */
public class ServiceOverview extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "Service Overview";
    
    private final Map<String, ServiceOverviewPanel> servicesMap = new HashMap<>();
    
    private final JPanel servicesContainer = new JPanel(new WrapLayout());
    
    public ServiceOverview(FilteredEventDatabase eventsDatabase) {
        
        eventsDatabase.addListener(this);
        
        buildUI();
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        // do something
    }

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
        return ServiceOverview.NAME;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
                
        // add new ServicePanel to layout   
        String serviceName = TableUtils.getService(event);
        
        ServiceOverviewPanel service = servicesMap.get(serviceName);
        if (service == null) {
            service = new ServiceOverviewPanel(serviceName);
            servicesMap.put(serviceName, service);
            servicesContainer.add(service);
        }
        
        service.incrementCount();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        servicesContainer.setBackground(Color.white);
        JScrollPane sPane = new JScrollPane(servicesContainer);
        
        this.setLayout(new BorderLayout());
        this.add(sPane, BorderLayout.CENTER);
    }
}
