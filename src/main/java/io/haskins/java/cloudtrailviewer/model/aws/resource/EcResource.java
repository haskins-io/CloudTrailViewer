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

/**
 *
 * @author mark
 */
public class EcResource extends AbstractResource implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void populateRequestInfo(Event event, ResourceInfo resources) {
        
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
      
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void createCacheCluster(Event event, ResourceInfo resources) {
        getTopLevelResource("Cache Cluster", "cacheClusterId", event, resources); 
        
        getTopLevelParameters(event, resources, "cacheClusterId");
    }
    
    private void createCacheParameterGroup(Event event, ResourceInfo resources) {
        getTopLevelResource("Cache Parameter Group", "cacheParameterGroupName", event, resources); 
        
        getTopLevelParameters(event, resources, "cacheParameterGroupName");
    }
       
    private void deleteCluster(Event event, ResourceInfo resources) {
        getTopLevelResource("Cache Cluster", "cacheClusterId", event, resources); 
        
        getTopLevelParameters(event, resources, "cacheClusterId");
    }
    
    private void deleteCacheParameters(Event event, ResourceInfo resources) {
        getTopLevelResource("Cache Parameter Group", "cacheParameterGroupName", event, resources); 
        
        getTopLevelParameters(event, resources, "cacheParameterGroupName");
    }
    
    private void desribeCacheParameters(Event event, ResourceInfo resources) {
        getTopLevelResource("Cache Parameter Group", "cacheParameterGroupName", event, resources); 
        
        getTopLevelParameters(event, resources, "cacheParameterGroupName");
    }
    
    private void describeReplicationGroups(Event event, ResourceInfo resources) {
        getTopLevelResource("Replication Group", "replicationGroupId", event, resources); 
        
        getTopLevelParameters(event, resources, "replicationGroupId");
    }
    
}
