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
package com.haskins.cloudtrailviewer.table.resource;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mark
 */
public abstract class AbstractResource {
    
    protected void getTopLevelResource(String resourceName, String paramName, Event event, RequestInfo resources) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey(paramName)) {
            
            Object paramValue = requestParameters.get(paramName);
            if (paramValue instanceof String) {
                resources.addResource(resourceName, (String)requestParameters.get(paramName));
                
            } else if (paramValue instanceof ArrayList) {
                
                List<String> values = (ArrayList)paramValue;
                for (String value : values) {
                    resources.addResource(resourceName, value);
                }
                
            }
        }
    }
}
