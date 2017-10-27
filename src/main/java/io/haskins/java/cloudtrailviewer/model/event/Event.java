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

package io.haskins.java.cloudtrailviewer.model.event;

import com.google.gson.annotations.SerializedName;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.event.deprecated.Resource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Event extends AwsData implements Serializable {

    private static final long serialVersionUID = 7492738943200865856L;


    @SerializedName(value="eventVersion", alternate={"eventversion"}) private String eventVersion = "";
    @SerializedName(value="userIdentity", alternate={"useridentity"}) private UserIdentity userIdentity;
    @SerializedName(value="eventSource", alternate={"eventsource"}) private String eventSource = "";
    @SerializedName(value="eventName", alternate={"eventname"}) private String eventName = "";
    @SerializedName(value="awsRegion", alternate={"awsregion"}) private String awsRegion = "";
    @SerializedName(value="userAgent", alternate={"useragent"}) private String userAgent = "";
    @SerializedName(value="errorCode", alternate={"errorcode"}) private String errorCode = "";
    @SerializedName(value="errorMessage", alternate={"errormessage"}) private String errorMessage = "";
    @SerializedName(value="requestParameters", alternate={"requestparameters"}) private Map<String, ?> requestParameters;
    @SerializedName(value="responseElements", alternate={"responseelements"}) private Map responseElements;
//    @SerializedName(value="additionalEventData", alternate={"additionaleventdata"}) private Map additionalEventData;
    @SerializedName(value="sourceIPAddress", alternate={"sourceipaddress"}) private String sourceIPAddress = "";
    @SerializedName(value="requestID", alternate={"requestIid"}) private String requestID = "";
    @SerializedName(value="eventID", alternate={"eventid"}) private String eventID = "";
    @SerializedName(value="eventType", alternate={"eventtype"}) private String eventType = "";
    @SerializedName(value="apiVersion", alternate={"apiversion"}) private String apiVersion = "";
    @SerializedName(value="readOnly", alternate={"readonly"}) private String readOnly = "";
    @SerializedName(value="recipientAccountId", alternate={"recipientaccountid"}) private String recipientAccountId = "";
    @SerializedName(value="serviceEventDetails", alternate={"serviceeventdetails"}) private Map serviceEventDetails;
    @SerializedName(value="sharedEventID", alternate={"sharedeventid"}) private String sharedEventID = "";
    @SerializedName(value="vpcEndpointId", alternate={"vpcendpointid"}) private String vpcEndpointId;


    /** Internal Use **/
    private String rawJson;


    public void setRawJSON(String json) {
        this.rawJson = json;
    }
    public String getRawJSON() {
    	return this.rawJson;
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


    /**
     * @return the responseElements
     */
    public Map getServiceEventDetails() {
        return serviceEventDetails;
    }

    /**
     * @param serviceEventDetails the responseElements to set
     */
    public void setServiceEventDetails(Map serviceEventDetails) {
        this.serviceEventDetails = serviceEventDetails;
    }

    /**
     * @return the sharedEventID
     */
    public String getSharedEventID() {
        return sharedEventID;
    }

    /**
     * @param sharedEventID the apiVersion to set
     */
    public void setSharedEventID(String sharedEventID) {
        this.sharedEventID = sharedEventID;
    }


    /**
     * @return the vpcEndpointId
     */
    public String getVpcEndpointId() {
        return vpcEndpointId;
    }

    /**
     * @param vpcEndpointId the apiVersion to set
     */
    public void setVpcEndpointId(String vpcEndpointId) {
        this.vpcEndpointId = vpcEndpointId;
    }


//    private Map getAdditionalEventData() {
//        return additionalEventData;
//    }
//
//    public void setAdditionalEventData(Map additionalEventData) {
//        this.additionalEventData = additionalEventData;
//    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// Methods used by EventTable
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getUserIdentityArn() {
        return this.getUserIdentity().getArn();
    }
    public String getUserIdentityType() {
        return this.getUserIdentity().getType();
    }
    public String getUserIdentityUserName() {
        return this.getUserIdentity().getUserName();
    }
    public String getUserIdentityPrincipalID() {
        return this.getUserIdentity().getPrincipalId();
    }
    public String getUserIdentityAccountId() {
        return this.getUserIdentity().getAccountId();
    }
    public String getUserIdentityAccessKeyId() {
        return this.getUserIdentity().getAccessKeyId();
    }
    public String getUserIdentityInvokedBy() {
        return this.getUserIdentity().getInvokedBy();
    }
    public String getUserIdentityWebIdFederationData() {
        return this.getUserIdentity().getWebIdFederationData();
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
        if (getEventType() != null) { modelData.append(getEventType()).append(", "); }
        if (getRecipientAccountId() != null) { modelData.append(getRecipientAccountId()).append(", "); }
//        if (getAdditionalEventData() != null) { modelData.append(getAdditionalEventData().toString()).append(", "); }
        if (getContinent() != null) { modelData.append(getContinent()).append(", "); }
        if (getCountry() != null) { modelData.append(getCountry()).append(", "); }
        if (getCity() != null) { modelData.append(getCity()).append(", "); }
        if (getLatLng() != null) { modelData.append(getLatLng()).append(", "); }
        
        return modelData.toString();
    }

}
