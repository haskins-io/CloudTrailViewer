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

package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import com.haskins.cloudtrailviewer.model.filter.FilterListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A database that provides a Filtered view on the master EventDatabase.
 * 
 * @author mark
 */
public class FilteredEventDatabase extends EventDatabase implements FilterListener {

    private final List<Event> filteredEvents = new ArrayList<>();
    
    private final Filter filter;
    
    /**
     * Default Constructor
     * @param filter Filter to be use for filtering
     */
    public FilteredEventDatabase (Filter filter) {
        
        this.filter = filter;
        this.filter.addListener(this);
    }
    
    /**
     * Returns the filter currently being used
     * @return A Filter object
     */
    public Filter getFilter() {
        return this.filter;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Overridden EventDatabase methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void addEvent(Event event) {
        super.addEvent(event);
        addInternal(event);
    }
        
    @Override
    public Event getEventByIndex(int index) {
        
        Event event = null;
        try {
            event = filteredEvents.get(index);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            
        }
        
        return event;
    }
     
    @Override
    public void clear() {
        filteredEvents.clear();
        super.clear();
    }
    
    @Override
    public int size() {
        return filteredEvents.size();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// FilterListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
     
        filteredEvents.clear();

        int size = super.size();
        for (int i = 0; i < size; i++) {

            Event event = super.getEventByIndex(i);
            addInternal(event);
            fireUpdate(event);
        }

        // fireUpdate(null);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void addInternal(Event event) {
    	
        if (filter.passesFilter(event)) {
            filteredEvents.add(event);
        }
    }
}
