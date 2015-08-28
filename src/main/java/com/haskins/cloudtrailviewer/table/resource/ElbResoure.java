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
public class ElbResoure implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void getResource(Event event, ResourceInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeInstanceHealth")) {
            describeInstanceHealth(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeLoadBalancers")) {
            describeLoadBalancers(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RegisterInstancesWithLoadBalancer")) {
            de_registerInstancesWithLoadBalancer(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeregisterInstancesFromLoadBalancer")) {
            de_registerInstancesWithLoadBalancer(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteLoadBalancer")) {
            deleteLoadBalancer(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ModifyLoadBalancerAttributes")) {
            modifyLoadBalancerAttributes(event, resources);
        }
    }
    
    private void describeInstanceHealth(Event event, ResourceInfo resources) {
        populateLoadBalancerName(event, resources);
    }
    
    private void describeLoadBalancers(Event event, ResourceInfo resources) {
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("loadBalancerNames")) {
            
            List<String> elbNames = (List)requestParameters.get("loadBalancerNames");
            for (String name : elbNames) {
                resources.addResource("ELB Name", name);
            } 
        }
    }
    
    private void deleteLoadBalancer(Event event, ResourceInfo resources) {
        populateLoadBalancerName(event, resources);
    }
    
    private void modifyLoadBalancerAttributes(Event event, ResourceInfo resources) {
        populateLoadBalancerName(event, resources);
    }
        
    private void de_registerInstancesWithLoadBalancer(Event event, ResourceInfo resources) {

        Map requestParameters = event.getRequestParameters();
        
        // load Balancer name
        if (requestParameters != null && requestParameters.containsKey("loadBalancerName")) {
            resources.addResource("ELB Name", (String)requestParameters.get("loadBalancerName"));
        }
        
        // instances
        if (requestParameters != null && requestParameters.containsKey("instances")) {
            
            List<LinkedHashMap> instances = (List)requestParameters.get("instances");
            if (instances != null) {
                for (Map instance : instances) {
                    resources.addResource("EC2 Instance", (String)instance.get("instanceId"));
                } 
            }
        }
    }
    
    
    private void populateLoadBalancerName(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("loadBalancerName")) {
            resources.addResource("ELB Name", (String)requestParameters.get("loadBalancerName"));
        }
    }
}
