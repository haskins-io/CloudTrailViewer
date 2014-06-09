/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2014  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.haskins.aws.cloudtrailviewer.filters;

import com.haskins.aws.cloudtrailviewer.models.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class Filters implements EventFilterListener {
    
    private final List<EventFilter> filters = new ArrayList<>();
    private final List<FiltersListener> listeners = new ArrayList<>();
    
    public void addEventFilter(EventFilter filter) {
        filter.addListener(this);
        this.filters.add(filter);
    }
    
    public void addListener(FiltersListener l) {
        this.listeners.add(l);
    }
    
    public Map<String, Event> filterEvents(Collection<Event> events) {
        
        Map<String, Event> filteredEvents = new HashMap<>();
        
        for (Event event : events) {
            
            boolean passed = true;
            
            for (EventFilter filter : filters) {
                
                passed &= filter.passesFilter(event);
            }
            
            if (passed) {
                filteredEvents.put(event.getEventId(), event);
            }
        } 
        
        return filteredEvents;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventFilterListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
        
        for (FiltersListener l  : this.listeners) {
            
            l.onFilterChanged();
        }
    }
}
