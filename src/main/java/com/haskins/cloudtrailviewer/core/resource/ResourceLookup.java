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
package com.haskins.cloudtrailviewer.core.resource;


import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.resource.ResourceInfo;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mark
 */
public class ResourceLookup {
    
    
    public static ResourceInfo getResourceInfo(Event event) {
        
        ResourceInfo resourceInfo = new ResourceInfo();
        
        Map requestParameters = event.getRequestParameters();
        if (requestParameters != null) {
            
            Set<String> keys = requestParameters.keySet();
            Iterator<String> it = keys.iterator();
            while(it.hasNext()) {
                
                String type = it.next();
                Object resourcesObj = requestParameters.get(type);

                if (resourcesObj instanceof List || resourcesObj instanceof String) {

                    resourceInfo.addType(type);

                    if (resourcesObj instanceof List) {

                        List resources = (List)requestParameters.get(type);
                        if (!resources.isEmpty() && resources.get(0) instanceof String) {
                            
                            List<String> stringResources = (List)requestParameters.get(type);
                            for (String resource : stringResources) {
                                resourceInfo.addName(resource);
                            }
                        }
                        
                    } else {

                        String resource = (String)requestParameters.get(type);
                        resourceInfo.addName(resource);
                    }
                } 
            }
        }
        
        return resourceInfo;
    }
}
