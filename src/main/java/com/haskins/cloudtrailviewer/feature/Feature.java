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

package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.List;

/**
 * Interface that all Features need to implement
 * 
 * @author mark
 */
public interface Feature extends EventDatabaseListener {
    
    /**
     * Called when all events have loaded
     */
    void eventLoadingComplete();
    
    /**
     * Feature should return a boolean to indicate if it show appear on the 
     * Feature Toolbar
     * @return 
     */
    boolean showOnToolBar();
    
    /**
     * Feature should return a it's unique name
     * @return 
     */
    String getName();
    
    /**
     * Feature should return the name of the icon to be used on the Feature 
     * Toolbar
     * @return 
     */
    String getIcon();
    
    /**
     * Feature should return the tooltip to be shown when it's icon is hovered 
     * over on the Feature toolbar
     * @return 
     */
    String getTooltip();
    
    /**
     * informs the feature that it is about to be navigated away from so it can do
     * any actions that free up memory
     */
    void will_hide();
    
    /**
     * informs the feature that it is about to be navigated too so it can do
     * any actions that it needs to present it's data
     */
    void will_appear();
    
    /**
     * tells the feature to show it's EventTable populating it with the passed
     * events
     * @param events Events to populate table with 
     */
    void showEventsTable(List<Event> events);
    
    /**
     * Informs feature to clear down it's display removing all components any
     * locally stored events
     */
    void reset();
}
