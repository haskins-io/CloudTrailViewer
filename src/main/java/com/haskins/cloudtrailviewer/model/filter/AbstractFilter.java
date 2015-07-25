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

package com.haskins.cloudtrailviewer.model.filter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public abstract class AbstractFilter implements Filter {
    
    private final List<FilterListener> listeners = new ArrayList<>();
    
    protected String needle;
    
    @Override
    public void setNeedle(String needle) {
        this.needle = needle;
        
        for (FilterListener l : listeners) {
            l.onFilterChanged();
        }
    }
    
    @Override
    public void addListener(FilterListener l) {
        listeners.add(l);
    }
}
