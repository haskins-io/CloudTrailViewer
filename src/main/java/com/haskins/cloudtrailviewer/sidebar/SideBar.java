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

package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.core.Printable;
import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public interface SideBar extends Printable {
    
    public void eventLoadingComplete();
    
    public boolean showOnToolBar();
    public String getName();
    public String getIcon();
    public String getTooltip();  
    
    public void setCurrentEvent(Event event);
}
