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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class ElbResoure extends AbstractRequest implements Request {

    public static final String ELB_NAME = "Elastic LoadBalancer";
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
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
            
        } else if (event.getEventName().equalsIgnoreCase("SetLoadBalancerPoliciesOfListener")) {
            setPoliciesOfListener(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RebuildEnvironment")) {
            rebuildEnvironment(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeLoadBalancerPolicies")) {
            describeLoadBalancerProperties(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeLoadBalancerAttributes")) {
            describeLoadBalancerAttributes(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateLoadBalancerPolicy")) {
            createLBPolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateLoadBalancer")) {
            createLoadBalancer(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ConfigureHealthCheck")) {
            configureHealthCheck(event, resources);
            
        }
    }
    
    private void configureHealthCheck(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void createLoadBalancer(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void createLBPolicy(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void describeLoadBalancerAttributes(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void describeLoadBalancerProperties(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void rebuildEnvironment(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void setPoliciesOfListener(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
        
        getTopLevelParameter("Policy", "policyNames", event, resources);
        getTopLevelParameter("ELB Port", "loadBalancerPort", event, resources);
    }
    
    private void describeInstanceHealth(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void describeLoadBalancers(Event event, RequestInfo resources) {
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("loadBalancerNames")) {
            
            List<String> elbNames = (List)requestParameters.get("loadBalancerNames");
            for (String name : elbNames) {
                resources.addResource(ELB_NAME, name);
            } 
        }
    }
    
    private void deleteLoadBalancer(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
    
    private void modifyLoadBalancerAttributes(Event event, RequestInfo resources) {
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
    }
        
    private void de_registerInstancesWithLoadBalancer(Event event, RequestInfo resources) {

        Map requestParameters = event.getRequestParameters();
        
        // load Balancer name
        getTopLevelResource(ELB_NAME, "loadBalancerName", event, resources); 
        
        // instances
        if (requestParameters != null && requestParameters.containsKey("instances")) {
            
            List<LinkedHashMap> instances = (List)requestParameters.get("instances");
            if (instances != null) {
                for (Map instance : instances) {
                    resources.addResource(Ec2Resource.EC2_INSTANCE, (String)instance.get("instanceId"));
                } 
            }
        }
    }
}
