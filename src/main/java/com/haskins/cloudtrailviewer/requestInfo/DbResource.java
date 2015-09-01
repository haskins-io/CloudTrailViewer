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
import java.util.Map;

/**
 *
 * @author mark.haskins
 */
public class DbResource extends AbstractRequest implements Request {

    private static final String DYNAMODB_TABLE = "DynamoDb Table";
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeTable")) {
            describeTable(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("UpdateTable")) {
            updateTable(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteTable")) {
            deleteTable(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateTable")) {
            createTable(event, resources);
        }
    }
        
    private void createTable(Event event, RequestInfo resources) {
        getTopLevelResource(DYNAMODB_TABLE, "tableName", event, resources); 
    } 
    
    private void deleteTable(Event event, RequestInfo resources) {
        getTopLevelResource(DYNAMODB_TABLE, "tableName", event, resources); 
    }    
    
    private void describeTable(Event event, RequestInfo resources) {
        getTopLevelResource(DYNAMODB_TABLE, "tableName", event, resources); 
    }
    
    private void updateTable(Event event, RequestInfo resources) {
        getTopLevelResource(DYNAMODB_TABLE, "tableName", event, resources); 
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("provisionedThroughput")) {
            
            Map throughput = (Map)requestParameters.get("provisionedThroughput");
            if (throughput != null) {
                
                if (throughput.containsKey("writeCapacityUnits")) {
                    resources.addParameter("Write Capacity", (String)throughput.get("writeCapacityUnits"));
                }
                
                if (throughput.containsKey("readCapacityUnits")) {
                    resources.addParameter("Read Capacity", (String)throughput.get("readCapacityUnits"));
                }
            }
        }
    }
}