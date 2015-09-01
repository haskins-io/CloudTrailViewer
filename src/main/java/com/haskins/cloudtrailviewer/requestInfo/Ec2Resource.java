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
public class Ec2Resource extends AbstractRequest implements Request {

    public static final String EC2_INSTANCE = "EC2 Instance";
    
    private static final String SECURITY_GROUP_NAME = "Security Group Name";
    private static final String SECURITY_GROUP_ID = "Security Group ID";
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeTags")) {
            describeTags(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeInstances")) {
            describeInstances(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateTags")) {
            createTags(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RunInstances")) {
            runInstances(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("TerminateInstances")) {
            terminateInstances(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("StopInstances")) {
            stopInstances(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RevokeSecurityGroupIngress")) {
            revokeSGIngress(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RevokeSecurityGroupEgress")) {
            revokeSGEgress(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RebootInstances")) {
            rebootInstances(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DetachVolume")) {
            detachVolume(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DetachNetworkInterface")) {
            detachNetworkInterface(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeVolumeAttribute")) {
            describeVolumeAttributes(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeInstanceAttribute")) {
            describeInstanceAttribute(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteSecurityGroup")) {
            deleteSecurityGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteNetworkInterface")) {
            deleteNetworkInterface(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateSecurityGroup")) {
            createSecurityGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateKeyPair")) {
            createKeyPair(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("AuthorizeSecurityGroupIngress")) {
            authoriseSGIngress(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("AuthorizeSecurityGroupEgress")) {
            authoriseSGEgress(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("AttachVolume")) {
            attachVolume(event, resources);
            
        }
    }
    
    private void attachVolume(Event event, RequestInfo resources) {
        getTopLevelResource("Volume Id", "volumeId", event, resources);
        
        getTopLevelParameter(EC2_INSTANCE, "instanceId", event, resources);
        getTopLevelParameter("Device", "device", event, resources);
        getTopLevelParameter("Delete on Terminate  ", "deleteOnTermination", event, resources);
    }
    
    private void authoriseSGEgress(Event event, RequestInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
    }
    
    private void authoriseSGIngress(Event event, RequestInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
    }
    
    private void createKeyPair(Event event, RequestInfo resources) {
        getTopLevelResource("Key Name", "keyName", event, resources);
    }
    
    private void createSecurityGroup(Event event, RequestInfo resources) {
        getTopLevelResource(SECURITY_GROUP_NAME, "groupName", event, resources);
        getTopLevelResource("Group Description", "groupDescription", event, resources);
    }
    
    private void deleteNetworkInterface(Event event, RequestInfo resources) {
        getTopLevelResource("Network Interface", "networkInterfaceId", event, resources);
    }
    
    private void deleteSecurityGroup(Event event, RequestInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
    }
    
    private void describeInstanceAttribute(Event event, RequestInfo resources) {
        getTopLevelResource(EC2_INSTANCE, "instanceId", event, resources);
    }
    
    private void describeVolumeAttributes(Event event, RequestInfo resources) {
        getTopLevelResource("Volume Id", "volumeId", event, resources);
    }
    
    private void detachNetworkInterface(Event event, RequestInfo resources) {
        getTopLevelResource("Network Interface", "attachmentId", event, resources);
    }
    
    private void detachVolume(Event event, RequestInfo resources) {
        getTopLevelResource("Volume Id", "volumeId", event, resources);
    }
    
    private void revokeSGEgress(Event event, RequestInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
    }
    
    private void revokeSGIngress(Event event, RequestInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
    }
    
    private void createTags(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("resourcesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("resourcesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource(EC2_INSTANCE, (String)instance.get("resourceId"));
                    } 
                }
            }
        }
    }
    
    private void describeTags(Event event, RequestInfo resources) {

    }
    
    private void describeInstances(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource(EC2_INSTANCE, (String)instance.get("instanceId"));
                    } 
                }
            }
        }
    }
    
    private void runInstances(Event event, RequestInfo resources) {
        
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
        
        getTopLevelParameter("EC2 Instance", "instanceType", event, resources);
        getTopLevelParameter("Availability Zone", "availabilityZone", event, resources);
    }
    
    private void terminateInstances(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource(EC2_INSTANCE, (String)instance.get("instanceId"));
                    } 
                }
            }
        }
    }
    
    private void stopInstances(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource(EC2_INSTANCE, (String)instance.get("instanceId"));
                    } 
                }
            }
        }
    }
    
    private void rebootInstances(Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
            if (resourceSet != null) {
                List<Map> items = (List)resourceSet.get("items");
                if (items != null) {
                    for (Map instance : items) {
                        resources.addResource(EC2_INSTANCE, (String)instance.get("instanceId"));
                    } 
                }
            }
        }
    }
}
