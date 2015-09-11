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

package com.haskins.cloudtrailviewer.requestInfo;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author mark
 */
public class ElbResoure extends AbstractRequest implements Request {

    public static final String ELB_NAME = "Elastic LoadBalancer";
    
    public ElbResoure() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("loadBalancerName", ELB_NAME);
                put("loadBalancerNames", ELB_NAME);
            }
        }); 
    }
        
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
        getTopLevelResource(ELB_NAME, "loadBalancerNames", event, resources); 
        
        getTopLevelParameters(event, resources, "loadBalancerName", "loadBalancerNames");
    }
}
