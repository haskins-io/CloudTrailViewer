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
package com.haskins.cloudtrailviewer.components.securitypanel;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.thirdparty.WrapLayout;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class SecurityOverviewContainer extends JPanel {
    
    private final Map<String, SecurityOverviewPanel> securityMap = new HashMap<>();
    
    private final Feature feature;
    
    public SecurityOverviewContainer(Feature parent) {
        
        this.feature = parent;
        
        this.setLayout(new WrapLayout());
    }
    
    public void setEvents(List<Event> events) {
        
        securityMap.clear();
        this.removeAll();
        
        for (Event event : events) {
            addEvent(event);
        }
        
        this.revalidate();
    }
    
    public void addEvent(Event event) {
        
        String eventName = event.getEventName();
        
        final SecurityOverviewPanel securityPanel;
        
        if (!securityMap.containsKey(eventName)) {
            
            securityPanel = new SecurityOverviewPanel(eventName, feature);
            
            securityMap.put(eventName, securityPanel);
            
            this.removeAll();
            
            Set keys = securityMap.keySet();
            List<String> sorted = GeneralUtils.asSortedList(keys);
            for (String service : sorted) {
                SecurityOverviewPanel panel = securityMap.get(service);
                this.add(panel);
            }            
        } else {
            securityPanel = securityMap.get(eventName);
        }
        
        securityPanel.addEvent(event);
    }
   
}
