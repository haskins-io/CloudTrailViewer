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
package com.haskins.cloudtrailviewer.requestInfo;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Abstract class that all Resources should implement
 * 
 * @author mark
 */
public abstract class AbstractRequest {
            
    /**
     * Map for holding resource information
     */
    protected Map<String, String> resourceMap;
    
    /**
     * Adds the Resource value (if found) to the RequestInfo with the given name
     * @param resourceName Name to find to store value as e.g AutoScaling Group
     * @param paramName Name to parameter find e.g. autoScalingGroupName
     * @param event Event to process
     * @param requestInfo RequestInfo to update
     */
    protected void getTopLevelResource(String resourceName, String paramName, Event event, RequestInfo requestInfo) {
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null && requestParameters.containsKey(paramName)) {
            
            Object paramValue = requestParameters.get(paramName);
            addValueToRequestInfo(requestInfo, resourceName, paramValue, true);
        }
    }
    
    /**
     * Adds the Parameter value (if found) to the RequestInfo with the given name
     * @param event Event to process
     * @param requestInfo RequestInfo to update
     * @param ignore parameter names that should be ignored
     */
    protected void getTopLevelParameters(Event event, RequestInfo requestInfo, String... ignore) {
        
        List<String> ignoreList = Arrays.asList(ignore);
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null) {
            
            Set<String> keys = requestParameters.keySet();
            Iterator<String> it = keys.iterator();
            while(it.hasNext()) {
                
                String paramName = it.next();
                if (!ignoreList.contains(paramName)) {
                    
                    Object paramValue = requestParameters.get(paramName);
                    addValueToRequestInfo(requestInfo, paramName, paramValue, false);
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void addValueToRequestInfo(RequestInfo requestInfo, String resourceName, Object paramValue, boolean resource) {
        
        if (resourceMap.containsKey(resourceName)) {
            resourceName = resourceMap.get(resourceName);
        }
        
        try {
            if (paramValue instanceof String) {
                
                if (resource) {
                    requestInfo.addResource(resourceName, (String)paramValue);
                } else {
                    requestInfo.addParameter(resourceName, (String)paramValue);
                }
                
            } else if (paramValue instanceof Integer || paramValue instanceof Boolean) {
                
                if (resource) {
                    requestInfo.addResource(resourceName, String.valueOf(paramValue));
                } else {
                    requestInfo.addParameter(resourceName, String.valueOf(paramValue));
                }
                
            }  else if (paramValue instanceof ArrayList) {
                
                List<Object> values = (ArrayList)paramValue;
                for (Object value : values) {
                    
                    addValueToRequestInfo(requestInfo, resourceName, value, resource);
                }

            } else if (paramValue instanceof LinkedHashMap) {
               
                Map values = (LinkedHashMap)paramValue;
                Set<String> keys = values.keySet();
                Iterator<String> it = keys.iterator();
                while(it.hasNext()) {

                    String key = it.next();
                    Object value = values.get(key);
                    addValueToRequestInfo(requestInfo, key, value, resource);
                }
            }
        }
        catch (Exception e) {
            
        }
 
    }
}
