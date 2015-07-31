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

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.List;

/**
 * Interface that all Features need to implement
 * 
 * @author mark
 */
public interface Feature {
    
    /**
     * Called when all events have loaded
     */
    public void eventLoadingComplete();
    
    
    /**
     * Feature should return a boolean to indicate if it show appear on the 
     * Feature Toolbar
     * @return 
     */
    public boolean showOnToolBar();
    
    /**
     * Feature should return a it's unique name
     * @return 
     */
    public String getName();
    
    /**
     * Feature should return the name of the icon to be used on the Feature 
     * Toolbar
     * @return 
     */
    public String getIcon();
    
    /**
     * Feature should return the tooltip to be shown when it's icon is hovered 
     * over on the Feature toolbar
     * @return 
     */
    public String getTooltip();
    
    public void will_hide();
    
    public void will_appear();
    
    public void showEventsTable(List<Event> events);
}
