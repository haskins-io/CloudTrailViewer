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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mark
 */
public class ResourceLookup {
    
    private static final Map<String, Resource> resolvers = new HashMap<>();
    static {
        resolvers.put("ec2.amazonaws.com", new Ec2Resource());
        resolvers.put("autoscaling.amazonaws.com", new AsResource());
        resolvers.put("elasticbeanstalk.amazonaws.com", new EbResource());
        resolvers.put("elasticloadbalancing.amazonaws.com", new ElbResoure());
        resolvers.put("rds.amazonaws.com", new RdsResource());
        resolvers.put("sns.amazonaws.com", new SnsResource());
        resolvers.put("cloudsearch.amazonaws.com", new CsResource());
        resolvers.put("cloudformation.amazonaws.com", new CfResource());
    }
    
    public static String getResource(Event event) {
        
	    Resource resourceResolver = resolvers.get(event.getEventSource());
	
	    if (resourceResolver != null) {
	       return resourceResolver.getResource(event);
	    }
	    else {
	      return "";
	    }
    }
}
