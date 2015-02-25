/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

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


package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.Comparator;

/**
 *
 * @author mark.haskins
 */
public class EventTimestampComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        
        if (o1 == null || o2 == null) {
            return 0;
        }
        
        if (o1.getTimestamp() > o2.getTimestamp()) {
            return 1;
        }
        
        if (o1.getTimestamp() < o2.getTimestamp()) {
            return -1;
        }
        
        return 0;
    }
}
