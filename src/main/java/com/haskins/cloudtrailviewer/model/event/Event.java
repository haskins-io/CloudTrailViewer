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

package com.haskins.cloudtrailviewer.model.event;

import com.haskins.cloudtrailviewer.model.event.deprecated.Resource;
import com.haskins.cloudtrailviewer.requestInfo.RequestInfo;
import com.haskins.cloudtrailviewer.requestInfo.ResourceLookup;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

public class Event implements Serializable {

    private static final long serialVersionUID = 7492738943200865856L;
    
    private String eventTime = "";
    private String eventVersion = "";
    private UserIdentity userIdentity;
    private String eventName = "";
    private String awsRegion = "";
    private String sourceIPAddress = "";
    private String userAgent = "";
    private String eventSource = "";
    private String errorCode = "";
    private String errorMessage = "";
    private Map<String, ?> requestParameters;
    private Map responseElements;
    private String requestID = "";
    private String eventID = "";
    private String eventType = "";
    private String apiVersion = "";
    private String recipientAccountId = "";
        

    /** Internal Use **/
    private String rawJson;
    private long timestamp;
    private RequestInfo resourceInfo = null;
    
    private String country = null;
    private String city = null;
    private String latLng = null;
    
    public void setCountry(String country) {
        this.country = country;
    }
    private String getCountry() {
        return this.country;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return this.city;
    }
    
    public void setLatLng(String LatLong) {
        this.latLng = LatLong;
    }
    private String getLatLng() {
        return this.latLng;
    }
    
    public void setRawJSON(String json) {
        this.rawJson = json;
    }
    public String getRawJSON() {
    	return this.rawJson;
    }
    
    public void setTimestamp(long millis) {
        this.timestamp = millis;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public RequestInfo getResourceInfo() {
        
        if (resourceInfo == null) {
            resourceInfo = new RequestInfo();
            if (requestParameters != null) {
                ResourceLookup.getResourceInfo(this, resourceInfo);
            }
            
        }
        
        return resourceInfo;
    }

    /**
     * @return the eventVersion
     */
    public String getEventVersion() {
        return eventVersion;
    }

    /**
     * @param eventVersion the eventVersion to set
     */
    public void setEventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
    }

    /**
     * @return the userIdentity
     */
    public UserIdentity getUserIdentity() {
        return userIdentity;
    }

    /**
     * @param userIdentity the userIdentity to set
     */
    public void setUserIdentity(UserIdentity userIdentity) {
        this.userIdentity = userIdentity;
    }

    /**
     * @return the eventTime
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return the eventSource
     */
    public String getEventSource() {
        return eventSource;
    }

    /**
     * @param eventSource the eventSource to set
     */
    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return the awsRegion
     */
    public String getAwsRegion() {
        return awsRegion;
    }

    /**
     * @param awsRegion the awsRegion to set
     */
    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    /**
     * @return the sourceIPAddress
     */
    public String getSourceIPAddress() {
        return sourceIPAddress;
    }

    /**
     * @param sourceIPAddress the sourceIPAddress to set
     */
    public void setSourceIPAddress(String sourceIPAddress) {
        this.sourceIPAddress = sourceIPAddress;
    }

    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * @return the requestParameters
     */
    public Map<String, ?> getRequestParameters() {
        return requestParameters;
    }

    /**
     * @param requestParameters the requestParameters to set
     */
    public void setRequestParameters(Map<String, ?> requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * @return the responseElements
     */
    private Map getResponseElements() {
        return responseElements;
    }

    /**
     * @param responseElements the responseElements to set
     */
    public void setResponseElements(Map responseElements) {
        this.responseElements = responseElements;
    }

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return requestID;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestID = requestId;
    }

    /**
     * @return the eventId
     */
    public String getEventId() {
        return eventID;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(String eventId) {
        this.eventID = eventId;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(String recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    /**
     * @return the apiVersion
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * @param apiVersion the apiVersion to set
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
    public DefaultMutableTreeNode populateTree() {
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Event");
        
        if (getEventName() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Name");
            node.add(new DefaultMutableTreeNode(getEventName()));
            root.add(node);
        }
        
        if (getEventVersion() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Version");
            node.add(new DefaultMutableTreeNode(getEventVersion()));
            root.add(node);
        }

        if (getEventTime() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Time");
            node.add(new DefaultMutableTreeNode(getEventTime()));
            root.add(node);
        }
        
        if (getEventSource() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Source");
            node.add(new DefaultMutableTreeNode(getEventSource()));
            root.add(node);
        }
        
        if (getRequestParameters() != null && resourceInfo != null && 
                (!resourceInfo.getResourcesMap().isEmpty() || !resourceInfo.getParameterMap().isEmpty())) {
            
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Request Parameters");
            
            Map<String, List<String>> resourceMap = resourceInfo.getResourcesMap();
            for (Map.Entry<String, List<String>> entry : resourceMap.entrySet()) {
                
                if (entry.getValue().size() > 0) {
                    
                    DefaultMutableTreeNode resouceNode = new DefaultMutableTreeNode(entry.getKey());
                    for (String resource : entry.getValue()) {
                        resouceNode.add(new DefaultMutableTreeNode(resource));
                    }
                
                    node.add(resouceNode);
                }
            }
            
            Map<String, List<String>> parameterMap = resourceInfo.getParameterMap();
            for (Map.Entry<String, List<String>> entry : parameterMap.entrySet()) {
                
                if (entry.getValue().size() > 0) {
                    
                    DefaultMutableTreeNode parameterNode = new DefaultMutableTreeNode(entry.getKey());
                    for (String parameter : entry.getValue()) {
                        parameterNode.add(new DefaultMutableTreeNode(parameter));
                    }
                
                    node.add(parameterNode);
                }
            }
            
            root.add(node);
        }
        
        if (getSourceIPAddress() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Source IP Address");
            node.add(new DefaultMutableTreeNode(getSourceIPAddress()));
            root.add(node);
        }

        if (getAwsRegion() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("AWS Region");
            node.add(new DefaultMutableTreeNode(getAwsRegion()));
            root.add(node);
        }
        
        if (getUserAgent()!= null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("User Agent");
            node.add(new DefaultMutableTreeNode(getUserAgent()));
            root.add(node);
        }
        
        if (getErrorCode() != null && getErrorCode().trim().length() > 0) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Error Code");
            node.add(new DefaultMutableTreeNode(getErrorCode()));
            root.add(node);
        }
        
        if (getErrorMessage()!= null && getErrorCode().trim().length() > 0) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Error Message");
            node.add(new DefaultMutableTreeNode(getErrorMessage()));
            root.add(node);
        }
        
        if (getRequestId()!= null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Request Id");
            node.add(new DefaultMutableTreeNode(getRequestId()));
            root.add(node);
        }
        
        if (getEventId()!= null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Event Id");
            node.add(new DefaultMutableTreeNode(getEventId()));
            root.add(node);
        }
        
        if (getEventType()!= null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Event Type");
            node.add(new DefaultMutableTreeNode(getEventType()));
            root.add(node);
        }
        
        if (getApiVersion()!= null && getErrorCode().trim().length() > 0) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Api Version");
            node.add(new DefaultMutableTreeNode(getApiVersion()));
            root.add(node);
        }
        
        if (getRecipientAccountId()!= null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Recipient Account Id");
            node.add(new DefaultMutableTreeNode(getRecipientAccountId()));
            root.add(node);
        }

        if (getUserIdentity() != null) {
            root.add(userIdentity.getTree());
        }
        
        if (getCountry() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Country");
            node.add(new DefaultMutableTreeNode(getCountry()));
            root.add(node);
        }
        
        if (getCity() != null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("City");
            node.add(new DefaultMutableTreeNode(getCity()));
            root.add(node);
        }
        
        if (getLatLng()!= null) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode("Long/Lat");
            node.add(new DefaultMutableTreeNode(getLatLng()));
            root.add(node);
        }
        
        
        return root;
    }
    
    @Override
    public String toString() {
        
        StringBuilder modelData = new StringBuilder();
        
        if (getEventVersion() != null) { modelData.append(getEventVersion()).append(", "); }
        if (getUserIdentity() != null) { modelData.append(getUserIdentity().toString()).append(", "); }
        if (getEventTime() != null) { modelData.append(getEventTime()).append(", "); }
        if (getEventSource() != null) { modelData.append(getEventSource()).append(", "); }
        if (getEventName() != null) { modelData.append(getEventName()).append(", "); }
        if (getAwsRegion() != null) { modelData.append(getAwsRegion()).append(", "); }
        if (getSourceIPAddress() != null) { modelData.append(getSourceIPAddress()).append(", "); }
        if (getUserAgent() != null) { modelData.append(getUserAgent()).append(", "); }
        if (getRequestParameters() != null) { modelData.append(getRequestParameters().toString()).append(", "); }
        if (getResponseElements() != null) { modelData.append(getResponseElements().toString()).append(", "); }
        if (getRequestId() != null) { modelData.append(getRequestId()).append(", "); }
        if (getEventId() != null) { modelData.append(getEventId()).append(", "); }
        if (getErrorCode() != null) { modelData.append(getErrorCode()).append(", "); }
        if (getErrorMessage() != null) { modelData.append(getErrorMessage()).append(", "); }
        if (getReadOnly() != null) { modelData.append(getReadOnly()).append(", "); }
        if (getResources() != null) { modelData.append(getResources().toString()).append(", "); }
        if (getEventType() != null) { modelData.append(getEventType()).append(", "); }
        if (getRecipientAccountId() != null) { modelData.append(getRecipientAccountId()).append(", "); }
        if (getAdditionalEventData() != null) { modelData.append(getAdditionalEventData().toString()).append(", "); }
        
        return modelData.toString();
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Deprected Event parameters
    ////////////////////////////////////////////////////////////////////////////
    private Map additionalEventData;
    private String readOnly = "";  
    private List<Resource> resources;

    private Map getAdditionalEventData() {
        return additionalEventData;
    }

    public void setAdditionalEventData(Map additionalEventData) {
        this.additionalEventData = additionalEventData;
    }

    private String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }
    
    private List getResources() {
        return resources;
    }

    public void setResources(List resources) {
        this.resources = resources;
    } 
}
