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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class Ec2Resource implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void getResource(Event event, ResourceInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeTags")) {
            describeTags(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeInstances")) {
            describeInstances(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateTags")) {
            createTags(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RunInstances")) {
            runInstancees(event, resources);
        }
    }
    
    private void createTags(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("resourcesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("resourcesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource("EC2 Instance", (String)instance.get("resourceId"));
                    } 
                }
            }
        }
    }
    
    private void describeTags(Event event, ResourceInfo resources) {

    }
    
    private void describeInstances(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource("EC2 Instance", (String)instance.get("resourceId"));
                    } 
                }
            }
        }
    }
    
    private void runInstancees(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
//                    for (Map instance : items) {
//                        resources.addResource("EC2 Instance", (String)instance.get("resourceId"));
//                    } 
                }
            }  
        }
    }
}
