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

package com.haskins.cloudtrailviewer.model;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public class NameValueModel {
        
    private final String errorName;
    private final List<Event> errorEvents = new ArrayList<>();

    public NameValueModel(String error, Event event) {
        this.errorName = error;
        this.errorEvents.add(event);
    }

    public String getName() {
        return this.errorName;
    }

    public void addEvent(Event event) {
        this.errorEvents.add(event);
    }
    public List<Event> getEvents() {
        return this.errorEvents;
    }

    public int getNumberOfEvents() {
        return this.errorEvents.size();
    }
}
