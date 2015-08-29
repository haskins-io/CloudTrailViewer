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

/**
 *
 * @author mark
 */
public class EbResource extends AbstractResource implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void getResource(Event event, RequestInfo resources) {
        
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
            
        } else if (event.getEventName().equalsIgnoreCase("ValidateConfigurationSettings")) {
            validateConfigurationSettings(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateEnvironment")) {
            updateEnvironment(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("TerminateEnvironment")) {
            terminateEnvironment(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RetrieveEnvironmentInfo")) {
            retrieveEnvironmentInfo(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RestartAppServer")) {
            restartAppServer(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("RequestEnvironmentInfo")) {
            requestEnvironmentInfo(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeConfigurationOptions")) {
            describeConfigurationOptions(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteApplicationVersion")) {
            deleteApplicationVersion(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteApplication")) {
            deleteApplication(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateStorageLocation")) {
            createStorageLocation(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateEnvironment")) {
            createEnvironment(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateApplicationVersion")) {
            createApplicationVersion(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateApplication")) {
            createApplication(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CheckDNSAvailability")) {
            checkDnsAvailable(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeEnvironments")) {
            describeEnvironments(event, resources);
            
        }
    }
    
    private void describeEnvironments(Event event, RequestInfo resources) {
        getTopLevelResource("Environment", "environmentNames", event, resources);
        getTopLevelResource("Application", "applicationName", event, resources);
    }
    
    private void checkDnsAvailable(Event event, RequestInfo resources) {
        getTopLevelResource("cName", "cNAMEPrefix", event, resources);
    }
    
    private void createApplication(Event event, RequestInfo resources) {
        getTopLevelResource("Application Name", "applicationName", event, resources);
    }
    
    private void createApplicationVersion(Event event, RequestInfo resources) {
        getTopLevelResource("Application Name", "applicationName", event, resources);
        getTopLevelResource("Vesion Label", "versionLabel", event, resources);
    }
    
    private void createEnvironment(Event event, RequestInfo resources) {
        getTopLevelResource("Environment Name", "environmentName", event, resources);
    }
    
    private void createStorageLocation(Event event, RequestInfo resources) {
        getTopLevelResource("S3 Bucket", "s3Bucket", event, resources);
    }
    
    private void deleteApplication(Event event, RequestInfo resources) {
        getTopLevelResource("Application Name", "applicationName", event, resources);
    }
    
    private void deleteApplicationVersion(Event event, RequestInfo resources) {
        getTopLevelResource("Application Name", "applicationName", event, resources);
        getTopLevelResource("Vesion Label", "versionLabel", event, resources);
    }
    
    private void describeConfigurationOptions(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void requestEnvironmentInfo(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void restartAppServer(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void retrieveEnvironmentInfo(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void terminateEnvironment(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void describeEvents(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void describeEvironmentResources(Event event, RequestInfo resources) {
        getTopLevelResource("Environment ID", "environmentId", event, resources);
    }
    
    private void describeConfigurationSettings(Event event, RequestInfo resources) {
        getTopLevelResource("Environment Name", "environmentName", event, resources);
        getTopLevelResource("Application Name", "applicationName", event, resources);
    }
    
    private void describeApplicationVersions(Event event, RequestInfo resources) {
        getTopLevelResource("Application Name", "applicationName", event, resources);
    }
    
    private void swapEnvironmentCnames(Event event, RequestInfo resources) {
        getTopLevelResource("Environment Name", "destinationEnvironmentName", event, resources);
        getTopLevelResource("Environment Name", "sourceEnvironmentName", event, resources);
    }
    
    private void validateConfigurationSettings(Event event, RequestInfo resources) {
        getTopLevelResource("Application Name", "applicationName", event, resources);
    }
    
    private void updateEnvironment(Event event, RequestInfo resources) {
        getTopLevelResource("Environment Name", "sourceEnvironmentName", event, resources);
    }
}
