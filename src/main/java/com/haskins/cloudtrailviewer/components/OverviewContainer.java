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
package com.haskins.cloudtrailviewer.components;

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
public class OverviewContainer extends JPanel {

    private final Map<String, NameValuePanel> eventsMap = new HashMap<>();

    private final Feature feature;

    public OverviewContainer(Feature parent) {

        this.feature = parent;

        this.setLayout(new WrapLayout());
    }

    public void setEvents(List<Event> events) {

        eventsMap.clear();
        this.removeAll();

        for (Event event : events) {
            addEvent(event);
        }

        this.revalidate();
    }

    public void addEvent(Event event) {

        String eventName = event.getEventName();

        final NameValuePanel resourcePanel;

        if (!eventsMap.containsKey(eventName)) {

            resourcePanel = new NameValuePanel(eventName, feature);

            eventsMap.put(eventName, resourcePanel);

            this.removeAll();

            Set keys = eventsMap.keySet();
            List<String> sorted = GeneralUtils.asSortedList(keys);
            
            for (String service : sorted) {
                
                NameValuePanel panel = eventsMap.get(service);
                this.add(panel);
            }

        } else {

            resourcePanel = eventsMap.get(eventName);
        }

        resourcePanel.addEvent(event);
    }
}
