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
package com.haskins.cloudtrailviewer.components.servicespanel;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.utils.TableUtils;
import com.haskins.cloudtrailviewer.thirdparty.WrapLayout;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Container that will hold Overview panels are they are discovered.
 * 
 * @author mark
 */
public class ServiceOverviewContainer extends JPanel {

    private static final long serialVersionUID = -7604555547759281086L;
    
    private final Map<String, ServicesOverviewPanel> servicesMap = new HashMap<>();
    
    private final Feature feature;
    
    public ServiceOverviewContainer(Feature parent) {
        
        this.feature = parent;
        
        this.setLayout(new WrapLayout());
    }
    
    /**
     * Clears any current events and loads the container with the passed events.
     * @param events 
     */
    public void setEvents(List<Event> events) {
        
        servicesMap.clear();
        this.removeAll();
        
        for (Event event : events) {
            addEvent(event);
        }
        
        this.revalidate();
    }
    
    /**
     * Adds the Events and updates the appropriate Service Panel. If no matching
     * service panels exists one will be created.
     * @param event 
     */
    public void addEvent(Event event) {
        
        String source = event.getEventSource();
        String serviceName = TableUtils.getService(event);
        
        final ServicesOverviewPanel servicePanel;
        
        if (!servicesMap.containsKey(source)) {
                        
            servicePanel = new ServicesOverviewPanel(serviceName, feature);
            
            servicesMap.put(source, servicePanel);
            
            this.removeAll();
            
            Set<String> keys = servicesMap.keySet();
            List<String> sorted = GeneralUtils.asSortedList(keys);
            for (String service : sorted) {
                ServicesOverviewPanel panel = servicesMap.get(service);
                this.add(panel);
            }

        } else {
            servicePanel = servicesMap.get(source);
        }
        
        servicePanel.addEvent(event);
    }
   
    /**
     * Called when events are cleared from the database. Removes all panels from
     * the container and Events from local collection.
     */
    public void reset() {
        
        this.removeAll();
        servicesMap.clear();
        
        this.revalidate();
    }
}
