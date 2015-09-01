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
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class AsResource extends AbstractRequest implements Request {

    public static final String AUTO_SCALING_GROUP = "AutoScaling Group";
    private static final String LAUNCH_CONFIGURATION = "Launch Configuration";
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeScalingActivities")) {
            resolveDescribeScalingActivities(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeAutoScalingGroups")) {
            resolveDescribeAutoScalingGroups(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ResumeProcesses")) {
            resumeProcesses(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SuspendProcesses")) {
            suspendProcesses(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteLaunchConfiguration")) {
            deleteLaunchConfiguration(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateAutoScalingGroup")) {
            updateAutoScalingGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("TerminateInstanceInAutoScalingGroup")) {
            terminateInstanceInAutoScalingGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SetDesiredCapacity")) {
            setDesiredCapacity(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("PutScalingPolicy")) {
            putScalingPolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeScheduledActions")) {
            describeScheduledActions(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeletePolicy")) {
            deletePolicy(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteAutoScalingGroup")) {
            deleteAutoScalingGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateLaunchConfiguration")) {
            createLaunchConfiguration(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateAutoScalingGroup")) {
            createAutoScalingGroup(event, resources);
            
        }
    }
    
    private void createAutoScalingGroup(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
    }
    
    private void createLaunchConfiguration(Event event, RequestInfo resources) {
        getTopLevelResource(LAUNCH_CONFIGURATION, "launchConfigurationName", event, resources);
    }
    
    private void deleteAutoScalingGroup(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
    }
        
    private void deletePolicy(Event event, RequestInfo resources) {
        getTopLevelResource("Policy", "policyName", event, resources);
    }
    
    private void describeScheduledActions(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
    }
    
    private void putScalingPolicy(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
        getTopLevelResource("Policy", "policyName", event, resources);
    }
    
    private void setDesiredCapacity(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
        
        getTopLevelParameter("Desired Capacity", "desiredCapacity", event, resources);
        getTopLevelParameter("Honour Cooldown", "honorCooldown", event, resources);
    }
    
    private void terminateInstanceInAutoScalingGroup(Event event, RequestInfo resources) {
        getTopLevelResource(Ec2Resource.EC2_INSTANCE, "instanceId", event, resources);
    }
    
    private void resolveDescribeScalingActivities(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
    }
    
    private void resolveDescribeAutoScalingGroups(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("autoScalingGroupNames")) {
            
            List<String> groups = (List)requestParameters.get("autoScalingGroupNames");
            for (String group : groups) {
                resources.addResource(AUTO_SCALING_GROUP, group);
            }
        }
    }
    
    private void resumeProcesses(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
    }
    
    private void suspendProcesses(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
        
        getTopLevelParameter("Scaling Processes", "scalingProcesses", event, resources);
    }
    
    private void deleteLaunchConfiguration(Event event, RequestInfo resources) {
        getTopLevelResource(LAUNCH_CONFIGURATION, "launchConfigurationName", event, resources);
    }
    
    private void updateAutoScalingGroup(Event event, RequestInfo resources) {
        getTopLevelResource(AUTO_SCALING_GROUP, "autoScalingGroupName", event, resources);
        
        getTopLevelParameter("Min Size", "minSize", event, resources);
        getTopLevelParameter("Default Cool Down", "defaultCooldown", event, resources);
        getTopLevelParameter("VPC Zone", "vPCZoneIdentifier", event, resources);
        getTopLevelParameter("Max Size", "maxSize", event, resources);
        getTopLevelParameter("Availability Zones", "availabilityZones", event, resources);
        getTopLevelParameter("Launch configuration", "launchConfigurationName", event, resources);
    }
}
