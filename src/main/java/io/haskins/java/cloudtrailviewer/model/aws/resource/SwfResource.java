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
package io.haskins.java.cloudtrailviewer.model.aws.resource;

import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author mark
 */
public class SwfResource extends AbstractResource implements Resource {

    private static final String DOMAIN = "Domain";
    
    
    public SwfResource() {
        
        this.resourceMap = Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("name", DOMAIN);
                put("domain", DOMAIN);
            }
        }); 
    }
    
    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, ResourceInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("RegisterActivityType")) {
            registerActivityType(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeprecateDomain")) {
            deprecateDomain(event, resources);
            
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void deprecateDomain(Event event, ResourceInfo resources) {
        getTopLevelResource(DOMAIN, "name", event, resources);
        getTopLevelParameters(event, resources, "name");
    }
    
    private void registerActivityType(Event event, ResourceInfo resources) {
        getTopLevelResource(DOMAIN, "domain", event, resources);
        getTopLevelParameters(event, resources, "domain");
    }
}
