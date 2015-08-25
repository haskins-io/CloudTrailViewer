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

/**
 *
 * @author mark.haskins
 */
public class InvokersContainer extends OverviewContainer {
    
    public InvokersContainer(Feature parent) {
        super(parent);
    }
    
    @Override
    public void addEvent(Event event) {

        String eventName = event.getEventName();

        final NameValuePanel resourcePanel;

        if (!eventsMap.containsKey(eventName)) {

            String type = event.getUserIdentity().getType();
            if (type.equalsIgnoreCase("IAMUser") || type.equalsIgnoreCase("AssumedRole")) {
                resourcePanel = new NameValuePanel(eventName, null, feature);
                
            } else  {
                resourcePanel = new NameValuePanel(eventName, null, feature);
            }
            
            eventsMap.put(eventName, resourcePanel);
            this.add(resourcePanel);

        } else {

            resourcePanel = eventsMap.get(eventName);
        }

        resourcePanel.addEvent(event);
    }
}
