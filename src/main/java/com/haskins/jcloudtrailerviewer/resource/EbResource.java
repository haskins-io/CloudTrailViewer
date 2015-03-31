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
public class EbResource implements Resource {

    @Override
    public String getResource(Event event) {
        
        String resource = "";
        
        if (event.getEventName().equalsIgnoreCase("DescribeEvents")) {
            resource = describeEvents(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeEnvironmentResources")) {
            resource = describeEvironmentResources(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeConfigurationSettings")) {
            resource = describeConfigurationSettings(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeApplicationVersions")) {
            resource = describeApplicationVersions(event);
        } else if (event.getEventName().equalsIgnoreCase("DescribeStackResources")) {
            resource = describeStackResources(event);
        }
        
        return resource;
    }
    
    private String describeEvents(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters.containsKey("environmentId")) {
            resource = (String)requestParameters.get("environmentId");
        }
        
        return resource;
    }
    
    private String describeEvironmentResources(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters.containsKey("environmentId")) {
            resource = (String)requestParameters.get("environmentId");
        }
        
        return resource;
    }
    
    private String describeConfigurationSettings(Event event) {
        
        StringBuilder resource = new StringBuilder();
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters.containsKey("environmentName")) {
            resource.append((String)requestParameters.get("environmentName"));
            resource.append(" : ");
            resource.append((String)requestParameters.get("applicationName"));
        }
        
        return resource.toString();
    }
    
    private String describeApplicationVersions(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters.containsKey("applicationName")) {
            resource = (String)requestParameters.get("applicationName");
        }
        
        return resource;
    }
    
    private String describeStackResources(Event event) {
        
        String resource = "";
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters.containsKey("stackName")) {
            resource = (String)requestParameters.get("stackName");
        }
        
        return resource;
    }
}
