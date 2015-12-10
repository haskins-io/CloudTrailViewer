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

package com.haskins.cloudtrailviewer.utils;

import com.haskins.cloudtrailviewer.model.NameValueModel;
import java.util.Comparator;

/**
 * Comparator used to compare two NameValueModels to order by Count in descending
 * order
 * 
 * @author mark.haskins
 */
public class CountComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        int comparisonResult = 0;

        NameValueModel event1 = (NameValueModel)o1;
        NameValueModel event2 = (NameValueModel)o2;

        if(event1.getNumberOfEvents() < event2.getNumberOfEvents())
        {
            comparisonResult = 1;
        }
        else if(event1.getNumberOfEvents() > event2.getNumberOfEvents())
        {
            comparisonResult = -1;
        }

        return comparisonResult;
    }
    
}
