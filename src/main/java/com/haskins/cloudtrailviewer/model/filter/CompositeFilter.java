/*
 * Copyright (C) 2016 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.model.filter;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that can hold one or more filters and can run a AND test across all contained
 * filters to see if the passed Event matches.
 * 
 * @author markhaskins
 */
public class CompositeFilter {
    
    private final List<Filter> filters = new ArrayList<>();
    
    private boolean andFilters = true;
    
    public void addFilter(Filter f) {
        filters.add(f);
    }
    
    public void removeFilter(Filter f) {
        filters.remove(f);
    }
    
    public boolean allFiltersConfigured() {
        
        boolean allFiltersConfigured = true;
        
        for (Filter filter : filters) {
            
            if (filter.getNeedle() == null && filter.getNeedle().length() == 0) {
                allFiltersConfigured = false;
            }
        }
        
        return allFiltersConfigured;
    }
    
    public boolean passes(Event event) {
        
        boolean passed = true;
        
        for (Filter filter : filters) {
            
            if (andFilters) {
                passed &= filter.passesFilter(event);
            }
        }
        
        return passed;
    }
}
