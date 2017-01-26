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

package io.haskins.java.cloudtrailviewer.model.aws.resource;

import com.google.gson.internal.LinkedTreeMap;
import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public class Ec2Resource extends AbstractResource implements Resource {

    public static final String EC2_INSTANCE = "EC2 Instance";
    
    private static final String SECURITY_GROUP_NAME = "Security Group Name";
    private static final String SECURITY_GROUP_ID = "Security Group ID";
    
    public Ec2Resource() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("instanceId", EC2_INSTANCE);
            }
        }); 
    }
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, ResourceInfo resources) {
        
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
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void attachVolume(Event event, ResourceInfo resources) {
        getTopLevelResource("Volume Id", "volumeId", event, resources);
        
        getTopLevelParameters(event, resources, "Volume Id");
    }
    
    private void authoriseSGEgress(Event event, ResourceInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
        
        getTopLevelParameters(event, resources, "groupId");
    }
    
    private void authoriseSGIngress(Event event, ResourceInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
        
        getTopLevelParameters(event, resources, "groupId");
    }
    
    private void createKeyPair(Event event, ResourceInfo resources) {
        getTopLevelResource("Key Name", "keyName", event, resources);
        
        getTopLevelParameters(event, resources, "keyName");
    }
    
    private void createSecurityGroup(Event event, ResourceInfo resources) {
        getTopLevelResource(SECURITY_GROUP_NAME, "groupName", event, resources);
        getTopLevelResource("Group Description", "groupDescription", event, resources);
        
        getTopLevelParameters(event, resources, "groupName", "groupDescription");
    }
    
    private void deleteNetworkInterface(Event event, ResourceInfo resources) {
        getTopLevelResource("Network Interface", "networkInterfaceId", event, resources);
        
        getTopLevelParameters(event, resources, "networkInterfaceId");
    }
    
    private void deleteSecurityGroup(Event event, ResourceInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
        
        getTopLevelParameters(event, resources, "groupId");
    }
    
    private void describeInstanceAttribute(Event event, ResourceInfo resources) {
        getTopLevelResource(EC2_INSTANCE, "instanceId", event, resources);
        
        getTopLevelParameters(event, resources, "instanceId");
    }
    
    private void describeVolumeAttributes(Event event, ResourceInfo resources) {
        getTopLevelResource("Volume Id", "volumeId", event, resources);
        
        getTopLevelParameters(event, resources, "volumeId");
    }
    
    private void detachNetworkInterface(Event event, ResourceInfo resources) {
        getTopLevelResource("Network Interface", "attachmentId", event, resources);
        
        getTopLevelParameters(event, resources, "attachmentId");
    }
    
    private void detachVolume(Event event, ResourceInfo resources) {
        getTopLevelResource("Volume Id", "volumeId", event, resources);
        
        getTopLevelParameters(event, resources, "volumeId");
    }
    
    private void revokeSGEgress(Event event, ResourceInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
        
        getTopLevelParameters(event, resources, "groupId");
    }
    
    private void revokeSGIngress(Event event, ResourceInfo resources) {
        getTopLevelResource(SECURITY_GROUP_ID, "groupId", event, resources);
        
        getTopLevelParameters(event, resources, "groupId");
    }
    
    private void createTags(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("resourcesSet")) {
            
            Map<String, ArrayList> resourceSet = (LinkedTreeMap)requestParameters.get("resourcesSet");
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
    
    private void describeTags(Event event, ResourceInfo resources) {

    }
    
    private void describeInstances(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, ArrayList> resourceSet = (LinkedTreeMap)requestParameters.get("instancesSet");
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
    
    private void runInstances(Event event, ResourceInfo resources) {
        
//        MapWidget requestParameters = event.getRequestParameters();
//        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
//            
//            MapWidget<String, LinkedHashMap> resourceSet = (LinkedHashMap)requestParameters.get("instancesSet");
//            if (resourceSet != null) {
//                List<MapWidget> items = (List)resourceSet.get("items");
//                if (items != null) {
//                    for (MapWidget instance : items) {
//                        resources.addResource("EC2 Instance", (String)instance.get("resourceId"));
//                    } 
//                }
//            }  
//        }
//        
//        getTopLevelParameters(event, resources, "Volume Id");
        
//        getTopLevelParameter("EC2 Instance", "instanceType", event, resources);
//        getTopLevelParameter("Availability Zone", "availabilityZone", event, resources);
    }
    
    private void terminateInstances(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, ArrayList> resourceSet = (LinkedTreeMap)requestParameters.get("instancesSet");
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
    
    private void stopInstances(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, ArrayList> resourceSet = (LinkedTreeMap)requestParameters.get("instancesSet");
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
    
    private void rebootInstances(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("instancesSet")) {
            
            Map<String, ArrayList>resourceSet = (LinkedTreeMap)requestParameters.get("instancesSet");
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
