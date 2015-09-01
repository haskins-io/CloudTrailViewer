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

/**
 *
 * @author mark
 */
public class EcResource extends AbstractRequest implements Request {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("DescribeReplicationGroups")) {
            describeReplicationGroups(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DescribeCacheParameters")) {
            desribeCacheParameters(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteCacheParameterGroup")) {
            deleteCacheParameters(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteCacheCluster")) {
            deleteCluster(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateCacheParameterGroup")) {
            createCacheParameterGroup(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateCacheCluster")) {
            createCacheCluster(event, resources);
            
        }
    }
           
    private void createCacheCluster(Event event, RequestInfo resources) {
        getTopLevelResource("Cache Cluster", "cacheClusterId", event, resources); 
    }
    
    private void createCacheParameterGroup(Event event, RequestInfo resources) {
        getTopLevelResource("Cache Parameter Group", "cacheParameterGroupName", event, resources); 
    }
       
    private void deleteCluster(Event event, RequestInfo resources) {
        getTopLevelResource("Cache Cluster", "cacheClusterId", event, resources); 
    }
    
    private void deleteCacheParameters(Event event, RequestInfo resources) {
        getTopLevelResource("Cache Parameter Group", "cacheParameterGroupName", event, resources); 
    }
    
    private void desribeCacheParameters(Event event, RequestInfo resources) {
        getTopLevelResource("Cache Parameter Group", "cacheParameterGroupName", event, resources); 
    }
    
    private void describeReplicationGroups(Event event, RequestInfo resources) {
        getTopLevelResource("Replication Group", "replicationGroupId", event, resources); 
    }
    
}
