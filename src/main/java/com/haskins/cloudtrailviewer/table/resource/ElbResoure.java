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

package com.haskins.cloudtrailviewer.table.resource;


import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Map;

/**
 *
 * @author mark
 */
public class ElbResoure implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @return either the resource name or an empty string if the EventName is not handled.
     */
    @Override
    public String getResource(Event event) {
        
        String resource = "";
        
        if (event.getEventName().equalsIgnoreCase("DescribeInstanceHealth")) {
            resource = describeInstanceHealth(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeLoadBalancers")) {
            resource = describeLoadBalancers(event);
        }
        
        return resource;
    }
    
    private String describeInstanceHealth(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("loadBalancerName")) {
            resource = (String)requestParameters.get("loadBalancerName");
        }
        
        return resource;
    }
    
    private String describeLoadBalancers(Event event) {
        
        String resource = "";
        
//        Map requestParameters = event.getRequestParameters();
//        if (requestParameters != null && requestParameters.containsKey("applicationName")) {
//            resource = (String)requestParameters.get("applicationName");
//        }
        
        return resource;
    }
}
