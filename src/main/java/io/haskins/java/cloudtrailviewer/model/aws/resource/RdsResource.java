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

import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author mark
 */
public class RdsResource extends AbstractResource implements Resource {

    private static final String RDS_INSTANCE = "RDS Instance";
    
    public RdsResource() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("dBInstanceIdentifier", RDS_INSTANCE);
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
        
        if (event.getEventName().equalsIgnoreCase("RebootDBInstance")) {
            rebootDbInstance(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ModifyDBInstance")) {
            modifyDbInstance(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListTagsForResource")) {
            listTagsForResource(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeOptionGroupOptions")) {
            describeOptionGroupOptions(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeDBParameters")) {
            describeDBParameters(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteDBInstance")) {
            deleteDbInstance(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateDBParameterGroup")) {
            createDBParameterGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateDBInstance")) {
            createDbInstance(event, resources);
            
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void createDbInstance (Event event, ResourceInfo resources) {
        getTopLevelResource(RDS_INSTANCE, "dBInstanceIdentifier", event, resources);
        
        getTopLevelParameters(event, resources, "dBInstanceIdentifier");
    }
    
    private void createDBParameterGroup(Event event, ResourceInfo resources) {
        getTopLevelResource("Param Group", "dBParameterGroupName", event, resources);
        
        getTopLevelParameters(event, resources, "dBParameterGroupName");
    }
    
    private void deleteDbInstance (Event event, ResourceInfo resources) {
        getTopLevelResource(RDS_INSTANCE, "dBInstanceIdentifier", event, resources);
        
        getTopLevelParameters(event, resources, "dBInstanceIdentifier");
    }

    private void describeDBParameters(Event event, ResourceInfo resources) {
        getTopLevelResource("Param Group", "dBParameterGroupName", event, resources);
        
        getTopLevelParameters(event, resources, "dBParameterGroupName");
    }
    
    private void describeOptionGroupOptions (Event event, ResourceInfo resources) {
        getTopLevelResource("Engine", "engineName", event, resources);
        
        getTopLevelParameters(event, resources, "engineName");
    }
    
    private void listTagsForResource (Event event, ResourceInfo resources) {
        getTopLevelResource("Resource", "resourceName", event, resources);
        
        getTopLevelParameters(event, resources, "resourceName");
    }
    
    private void modifyDbInstance (Event event, ResourceInfo resources) {
        getTopLevelResource(RDS_INSTANCE, "dBInstanceIdentifier", event, resources);
        
        getTopLevelParameters(event, resources, "dBInstanceIdentifier");
    }
    
    private void rebootDbInstance (Event event, ResourceInfo resources) {
        getTopLevelResource(RDS_INSTANCE, "dBInstanceIdentifier", event, resources);
        
        getTopLevelParameters(event, resources, "dBInstanceIdentifier");
    }
}
