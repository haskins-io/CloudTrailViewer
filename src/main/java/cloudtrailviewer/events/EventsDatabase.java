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


package cloudtrailviewer.events;

import cloudtrailviewer.filters.Filters;
import cloudtrailviewer.filters.FiltersListener;
import cloudtrailviewer.models.Event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class EventsDatabase implements EventLoaderListener, FiltersListener {
    
    private final Map<String, Event> masterEventsMap    = new HashMap<String, Event>();
    private final List<EventsDatabaseListener> listeners = new ArrayList<EventsDatabaseListener>();
    
    private final Filters filters;
     
    public EventsDatabase(EventLoader eventLoader, Filters filters) {
        
        this.filters = filters;
        this.filters.addListener(this);
        
        eventLoader.addListener(this);
    }
    
    public void addListeners(EventsDatabaseListener listener) {
        this.listeners.add(listener);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {
            
        for(Event event : events) {
            masterEventsMap.put(event.getEventId(), event);
        }
        
        for (EventsDatabaseListener listener : listeners) {
            listener.onEventsUpdated(masterEventsMap);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventFilterListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
        
        Map<String, Event> filteredEvents = filters.filterEvents(masterEventsMap.values());
        
        for (EventsDatabaseListener listener : listeners) {
            listener.onEventsUpdated(filteredEvents);
        }
    }
}
