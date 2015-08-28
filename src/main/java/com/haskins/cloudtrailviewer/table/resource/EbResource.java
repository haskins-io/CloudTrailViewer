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
public class EbResource implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void getResource(Event event, ResourceInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeEvents")) {
            describeEvents(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeEnvironmentResources")) {
            describeEvironmentResources(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeConfigurationSettings")) {
            describeConfigurationSettings(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeApplicationVersions")) {
            describeApplicationVersions(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SwapEnvironmentCNAMEs")) {
            swapEnvironmentCnames(event, resources);
        }
    }
    
    private void describeEvents(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("environmentId")) {
            resources.addResource("Environment ID", (String)requestParameters.get("environmentId"));
        }
    }
    
    private void describeEvironmentResources(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("environmentId")) {
            resources.addResource("Environment ID", (String)requestParameters.get("environmentId"));
        }
    }
    
    private void describeConfigurationSettings(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("environmentName")) {
            
            resources.addResource("Environment Name", (String)requestParameters.get("environmentName"));
            resources.addResource("Application Name", (String)requestParameters.get("applicationName"));
        }
    }
    
    private void describeApplicationVersions(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("applicationName")) {
            resources.addResource("Application Name", (String)requestParameters.get("applicationName"));
        }
    }
    
    private void swapEnvironmentCnames(Event event, ResourceInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("destinationEnvironmentName")) {
            resources.addResource("Environment Name", (String)requestParameters.get("destinationEnvironmentName"));
        }
        
        if (requestParameters != null && requestParameters.containsKey("sourceEnvironmentName")) {
            resources.addResource("Environment Name", (String)requestParameters.get("sourceEnvironmentName"));
        }
    }
}
