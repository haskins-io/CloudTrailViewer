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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class OverviewContainer extends JPanel {

    protected final Map<String, NameValuePanel> eventsMap = new HashMap<>();

    protected final Feature feature;

    public OverviewContainer(Feature parent) {

        this.feature = parent;

        this.setLayout(new WrapLayout());
    }

    /**
     * Adds a collection of events to the panel
     * @param events 
     */
    public void setEvents(List<Event> events) {

        eventsMap.clear();
        this.removeAll();

        for (Event event : events) {
            addEvent(event, "EventName");
        }

        finishedLoading();
    }

    /**
     * Adds a single event to the panel
     * @param event
     * @param eventField 
     * @return total events
     */
    public int addEvent(Event event, String eventField) {
          
        int totalEvents = -1;
        
        try {
            
            String getProperty = "get" + eventField;
            Method method = event.getClass().getMethod(getProperty);
            Object eventFieldValue = method.invoke(event);

            String propertyValue = (String)eventFieldValue;

            final NameValuePanel resourcePanel;
            if (!eventsMap.containsKey(propertyValue)) {

                resourcePanel = new NameValuePanel(propertyValue, null, feature);

                eventsMap.put(propertyValue, resourcePanel);
                this.add(resourcePanel);

            } else {

                resourcePanel = eventsMap.get(propertyValue);
            }

            totalEvents = resourcePanel.addEvent(event); 
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            // going to ignore this and not add the event
        }
        
        return totalEvents;
    }

    /**
     * when called displays the events.
     */
    public void finishedLoading() {

        this.removeAll();
        
        List<Map.Entry<String, NameValuePanel>> sortedPanels = entriesSortedByValues(eventsMap);
        for (Map.Entry<String, NameValuePanel> aPanel : sortedPanels) {
            
            String service = aPanel.getKey();
            NameValuePanel panel = eventsMap.get(service);
            this.add(panel);
        }
        
        this.revalidate();
    }

    /**
     * Resets the panel, removing all data
     */
    public void reset() {
        
        this.removeAll();
        eventsMap.clear();
        this.revalidate();
        
    }
    
    List<Map.Entry<String, NameValuePanel>> entriesSortedByValues(Map<String, NameValuePanel> map) {

        List<Map.Entry<String, NameValuePanel>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries,
            new Comparator<Map.Entry<String, NameValuePanel>>() {
                @Override
                public int compare(Map.Entry<String, NameValuePanel> e1, Map.Entry<String, NameValuePanel> e2) {

                    int comparisonResult = 0;

                    if(e1.getValue().getEventCount() < e2.getValue().getEventCount()) {
                        comparisonResult = 1;

                    } else if(e1.getValue().getEventCount() > e2.getValue().getEventCount()) {
                        comparisonResult = -1;
                    }

                    return comparisonResult;
                }
            }
        );

        return sortedEntries;
    }
}
