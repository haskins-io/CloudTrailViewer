/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.core;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import com.haskins.cloudtrailviewer.model.filter.FilterListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public class FilteredEventDatabase extends EventDatabase implements FilterListener {

    private final List<Event> filteredEvents = new ArrayList<>();
    
    private final Filter filter;
    
    public FilteredEventDatabase (Filter filter) {
        
        this.filter = filter;
        this.filter.addListener(this);
    }
    
    public Filter getFilter() {
        return this.filter;
    }
    
    @Override
    public void addEvent(Event event) {
        super.addEvent(event);
        addInternal(event);
    }
        
    @Override
    public Event getEventByIndex(int index) {
        return filteredEvents.get(index);
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
    
    private void addInternal(Event event) {
    	
        if (filter.passesFilter(event)) {
            filteredEvents.add(event);
        }
    }
}
