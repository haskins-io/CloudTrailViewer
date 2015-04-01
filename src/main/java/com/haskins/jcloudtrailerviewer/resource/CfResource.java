/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.jcloudtrailerviewer.resource;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.Map;

/**
 *
 * @author mark
 */
public class CfResource implements Resource {

    @Override
    public String getResource(Event event) {
        
        String resource = "";
        
        if (event.getEventName().equalsIgnoreCase("DescribeStacks")) {
            resource = describeStacks(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeStackResources")) {
            resource = describeStackResources(event);
        }
        
        return resource;
    }
    
    private String describeStacks(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("stackName")) {
            resource = (String)requestParameters.get("stackName");
        }
        
        return resource;
    }
    
    private String describeStackResources(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey("stackName")) {
            resource = (String)requestParameters.get("stackName");
        }
        
        return resource;
    }
}