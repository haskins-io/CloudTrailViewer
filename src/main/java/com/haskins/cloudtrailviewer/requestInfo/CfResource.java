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

/**
 *
 * @author mark
 */
public class CfResource extends AbstractRequest implements Request {

    public static final String CLOUDFORMATION_STACK = "CloudFormation Stack";
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
                
        if (event.getEventName().equalsIgnoreCase("DescribeStacks")) {
            getStackName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeStackResource")) {
            getStackName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeStackResources")) {
            getStackName(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateStack")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("ListStackResources")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("GetTemplate")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("GetTemplate")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("GetStackPolicy")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("DescribeStackEvents")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("DeleteStack")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("CreateStack")) {
            getStackName(event, resources);

        } else if (event.getEventName().equalsIgnoreCase("CancelUpdateStack")) {
            getStackName(event, resources);

        }
    }
    
    private void getStackName(Event event, RequestInfo resources) {
        getTopLevelResource(CLOUDFORMATION_STACK, "stackName", event, resources);
    }
}