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
public class EbResource extends AbstractRequest implements Request {

    private static final String APPLICATION_NAME = "Application Name";
    private static final String ENVIRONMENT_NAME = "Environment Name";
    private static final String ENVIRONMENT_ID = "Environment ID";
    
    public EbResource() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("applicationName", APPLICATION_NAME);
                put("environmentName", ENVIRONMENT_NAME);
                put("environmentId", ENVIRONMENT_ID);
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
        
        if (event.getEventName().equalsIgnoreCase("DescribeEvents")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeEnvironmentResources")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeConfigurationSettings")) {
            describeConfigurationSettings(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeApplicationVersions")) {
            populateApplicationName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SwapEnvironmentCNAMEs")) {
            swapEnvironmentCnames(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ValidateConfigurationSettings")) {
            populateApplicationName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateEnvironment")) {
            populateEnvironmentName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("TerminateEnvironment")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RetrieveEnvironmentInfo")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RestartAppServer")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RequestEnvironmentInfo")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeConfigurationOptions")) {
            populateEnvironmentId(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteApplicationVersion")) {
            deleteApplicationVersion(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteApplication")) {
            populateApplicationName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateStorageLocation")) {
            createStorageLocation(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateEnvironment")) {
            populateEnvironmentName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateApplicationVersion")) {
            createApplicationVersion(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateApplication")) {
            populateApplicationName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CheckDNSAvailability")) {
            checkDnsAvailable(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeEnvironments")) {
            describeEnvironments(event, resources);
            
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void populateApplicationName(Event event, RequestInfo resources) {
        getTopLevelResource(APPLICATION_NAME, "applicationName", event, resources);
        
        getTopLevelParameters(event, resources, "applicationName");
    }
    
    private void populateEnvironmentName(Event event, RequestInfo resources) {
        getTopLevelResource(ENVIRONMENT_NAME, "environmentName", event, resources);
        
        getTopLevelParameters(event, resources, "environmentName");
    }
    
    private void populateEnvironmentId(Event event, RequestInfo resources) {
        getTopLevelResource(ENVIRONMENT_ID, "environmentId", event, resources);
        
        getTopLevelParameters(event, resources, "environmentId");
    }
    
    private void describeEnvironments(Event event, RequestInfo resources) {
        getTopLevelResource(ENVIRONMENT_NAME, "environmentNames", event, resources);
        getTopLevelResource(APPLICATION_NAME, "applicationName", event, resources);
        
        getTopLevelParameters(event, resources, "applicationName","environmentNames");
    }
    
    private void checkDnsAvailable(Event event, RequestInfo resources) {
        getTopLevelResource("cName", "cNAMEPrefix", event, resources);
    }
    
    private void createApplicationVersion(Event event, RequestInfo resources) {
        getTopLevelResource(APPLICATION_NAME, "applicationName", event, resources);
        getTopLevelResource("Version Label", "versionLabel", event, resources);
        
        getTopLevelParameters(event, resources, "applicationName","versionLabel");
    }
    
    private void createStorageLocation(Event event, RequestInfo resources) {
        getTopLevelResource("S3 Bucket", "s3Bucket", event, resources);
    }

    private void deleteApplicationVersion(Event event, RequestInfo resources) {
        getTopLevelResource(APPLICATION_NAME, "applicationName", event, resources);
        getTopLevelResource("Version Label", "versionLabel", event, resources);
        
        getTopLevelParameters(event, resources, "applicationName","versionLabel");
    }
   
    private void describeConfigurationSettings(Event event, RequestInfo resources) {
        getTopLevelResource(ENVIRONMENT_NAME, "environmentName", event, resources);
        getTopLevelResource(APPLICATION_NAME, "applicationName", event, resources);
        
        getTopLevelParameters(event, resources, "applicationName","environmentName");
    }
    
    private void swapEnvironmentCnames(Event event, RequestInfo resources) {
        getTopLevelResource(ENVIRONMENT_NAME, "destinationEnvironmentName", event, resources);
        getTopLevelResource(ENVIRONMENT_NAME, "sourceEnvironmentName", event, resources);
    }
}
