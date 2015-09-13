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
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class AsResource extends AbstractRequest implements Request {

    public static final String AUTO_SCALING_GROUP = "AutoScaling Group";
    public static final String LAUNCH_CONFIGURATION = "Launch Configuration";
    
    public AsResource() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("autoScalingGroupName", AUTO_SCALING_GROUP);
                put("launchConfigurationName", LAUNCH_CONFIGURATION);
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
        
        if (event.getEventName().equalsIgnoreCase("DescribeScalingActivities")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeAutoScalingGroups")) {
            resolveDescribeAutoScalingGroups(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ResumeProcesses")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SuspendProcesses")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteLaunchConfiguration")) {
            deleteLaunchConfiguration(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateAutoScalingGroup")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("TerminateInstanceInAutoScalingGroup")) {
            terminateInstanceInAutoScalingGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SetDesiredCapacity")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("PutScalingPolicy")) {
            putScalingPolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeScheduledActions")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeletePolicy")) {
            deletePolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteAutoScalingGroup")) {
            getAutoScalingGroupDetails(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateLaunchConfiguration")) {
            createLaunchConfiguration(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateAutoScalingGroup")) {
            getAutoScalingGroupDetails(event, resources);
            
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void getAutoScalingGroupDetails(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
        getTopLevelParameters(event, resources, "autoScalingGroupName");
    }
    
    private void createLaunchConfiguration(Event event, RequestInfo resources) {
        getTopLevelResource(LAUNCH_CONFIGURATION, "launchConfigurationName", event, resources);
        getTopLevelParameters(event, resources, "launchConfigurationName");
    }
     
    private void deletePolicy(Event event, RequestInfo resources) {
        getTopLevelResource("Policy", "policyName", event, resources);
        
        getTopLevelParameters(event, resources, "policyName");
    }
    
    private void putScalingPolicy(Event event, RequestInfo resources) {
        
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
        getTopLevelResource("Policy", "policyName", event, resources);
        
        getTopLevelParameters(event, resources, "autoScalingGroupName", "policyName");
    }
    
    private void terminateInstanceInAutoScalingGroup(Event event, RequestInfo resources) {
        getTopLevelResource(Ec2Resource.EC2_INSTANCE, "instanceId", event, resources);
        
        getTopLevelParameters(event, resources, "instanceId");
    }
    
    private void resolveDescribeAutoScalingGroups(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("autoScalingGroupNames")) {
            
            List<String> groups = (List)requestParameters.get("autoScalingGroupNames");
            for (String group : groups) {
                resources.addResource(AUTO_SCALING_GROUP, group);
            }
        }
        
        getTopLevelParameters(event, resources, "autoScalingGroupNames");
    }
    
    private void deleteLaunchConfiguration(Event event, RequestInfo resources) {
        getTopLevelResource(LAUNCH_CONFIGURATION, "launchConfigurationName", event, resources);
        
        getTopLevelParameters(event, resources, "launchConfigurationName");
    }

}
