/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2014  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.haskins.aws.cloudtrailviewer.models;

import java.util.Map;
import org.codehaus.jackson.annotate.JsonProperty;

public class Event {
    
    @JsonProperty("eventVersion")
    private String eventVersion = "";
    
    @JsonProperty("userIdentity")
    private UserIdentity userIdentity;
    
    @JsonProperty("eventTime")
    private String eventTime = "";
    
    @JsonProperty("eventSource")
    private String eventSource = "";
    
    @JsonProperty("eventName")
    private String eventName = "";
    
    @JsonProperty("awsRegion")
    private String awsRegion = "";
    
    @JsonProperty("sourceIPAddress")
    private String sourceIPAddress = "";
    
    @JsonProperty("userAgent")
    private String userAgent = "";
    
    @JsonProperty("requestParameters")
    private Map requestParameters;
    
    @JsonProperty("responseElements")
    private Map responseElements;
    
    @JsonProperty("requestID")
    private String requestId = "";
    
    @JsonProperty("eventID")
    private String eventId = "";
    
    
    // optional
    @JsonProperty("errorCode")
    private String errorCode = "";
    
    @JsonProperty("errorMessage")
    private String errorMessage = "";
    
    
    /** Internal Use **/
    private String rawJson;
    private String rawRequestParameters;
    private String rawResponseElements;
    private String rawUserIdentity;
    
    public void setRawJSON(String json) {
        this.rawJson = json;
    }
    public String getRawJSON() {
    	return this.rawJson;
    }
    
    public void setRawRequestParameters(String json) {
        this.rawRequestParameters = json;
    }
    public String getRawRequestParameters() {
        return this.rawRequestParameters;
    }
    
    public void setRawResponseElements(String json) {
        this.rawResponseElements = json;
    }
    public String getRawResponseElements() {
        return this.rawResponseElements;
    }
    
    public void setRawUserIdentity(String json) {
        this.rawUserIdentity = json;
    }
    public String getRawUserIdentity() {
        return this.rawUserIdentity;
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
    public Map getRequestParameters() {
        return requestParameters;
    }

    /**
     * @param requestParameters the requestParameters to set
     */
    public void setRequestParameters(Map requestParameters) {
        this.requestParameters = requestParameters;
    }

    /**
     * @return the responseElements
     */
    public Map getResponseElements() {
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
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
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
}
