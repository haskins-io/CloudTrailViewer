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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
abstract class AbstractFilter implements Filter {
    
    /** constant that defines the default dimension to be used by children */
    final static Dimension DEFAULT_SIZE = new Dimension(345,30);
    
    private final List<FilterListener> listeners = new ArrayList<>();
    
    /** String to hold the value that should be used for filtering */
    String needle = "";
    
    ////////////////////////////////////////////////////////////////////////////
    // Filter implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void setNeedle(String needle) {
        this.needle = needle;
        
        for (FilterListener l : listeners) {
            l.onFilterChanged();
        }
    }
    
    @Override
    public String getNeedle() {
        return this.needle;
    }
    
    @Override
    public void addListener(FilterListener l) {
        listeners.add(l);
    }
}
