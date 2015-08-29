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

package com.haskins.cloudtrailviewer.table.resource;

import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public class SnsResource extends AbstractResource implements Resource {

    /**
     * Return the resource for the passed Event
     * @param event Event from which the resource is require
     * @param resources 
     */
    @Override
    public void getResource(Event event, RequestInfo resources) {
        
        if (event.getEventName().equalsIgnoreCase("Unsubscribe")) {
            unsubscribe(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("Subscribe")) {
            subscribe(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SetTopicAttributes")) {
            setTopicAttribute(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("SetSubscriptionAttributes")) {
            setSubscriptionAttributes(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("ListSubscriptionsByTopic")) {
            listSubscriptionsByTopic(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetTopicAttributes")) {
            getTopicAttributes(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("GetSubscriptionAttributes")) {
            getSubscriptionAttributes(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteTopic")) {
            deleteTopic(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("DeleteEndpoint")) {
            deleteTopic(event, resources);
            
        } else if (event.getEventName().equalsIgnoreCase("CreateTopic")) {
            createTopic(event, resources);
            
        }
    }
    
    private void createTopic(Event event, RequestInfo resources) {
        getTopLevelResource("Topic", "name", event, resources);
    }

    private void deleteEndpoint(Event event, RequestInfo resources) {
        getTopLevelResource("Endpoint", "endpointArn", event, resources);
    }
    
    private void deleteTopic(Event event, RequestInfo resources) {
        getTopLevelResource("Topic", "topicArn", event, resources);
    }
    
    private void getSubscriptionAttributes(Event event, RequestInfo resources) {
        getTopLevelResource("Subscription", "subscriptionArn", event, resources);
    }
    
    private void getTopicAttributes(Event event, RequestInfo resources) {
        getTopLevelResource("Topic", "topicArn", event, resources);
    }
    
    private void listSubscriptionsByTopic(Event event, RequestInfo resources) {
        getTopLevelResource("Topic", "topicArn", event, resources);
    }
    
    private void setSubscriptionAttributes(Event event, RequestInfo resources) {
        getTopLevelResource("Subscription", "subscriptionArn", event, resources);
    }
    
    private void setTopicAttribute(Event event, RequestInfo resources) {
        getTopLevelResource("Topic", "topicArn", event, resources);
    }
    
    private void subscribe(Event event, RequestInfo resources) {
        getTopLevelResource("Topic", "topicArn", event, resources);
    }
    
    private void unsubscribe(Event event, RequestInfo resources) {
        getTopLevelResource("Subscription", "subscriptionArn", event, resources);
    }
    
}
