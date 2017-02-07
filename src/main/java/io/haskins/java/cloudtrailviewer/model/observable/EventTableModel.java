/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.model.observable;

import io.haskins.java.cloudtrailviewer.model.event.Event;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class that provides an Event TableModel
 *
 * Created by markhaskins on 26/01/2017.
 */
public class EventTableModel {

    private SimpleStringProperty eventTime = new SimpleStringProperty("");
    private SimpleStringProperty eventVersion = new SimpleStringProperty("");
    private SimpleStringProperty eventSource = new SimpleStringProperty("");
    private SimpleStringProperty eventName = new SimpleStringProperty("");
    private SimpleStringProperty awsRegion = new SimpleStringProperty("");
    private SimpleStringProperty userAgent = new SimpleStringProperty("");
    private SimpleStringProperty errorCode = new SimpleStringProperty("");
    private SimpleStringProperty errorMessage = new SimpleStringProperty("");
    private SimpleStringProperty sourceIPAddress = new SimpleStringProperty("");
    private SimpleStringProperty requestID = new SimpleStringProperty("");
    private SimpleStringProperty eventID = new SimpleStringProperty("");
    private SimpleStringProperty eventType = new SimpleStringProperty("");
    private SimpleStringProperty apiVersion = new SimpleStringProperty("");
    private SimpleStringProperty readOnly = new SimpleStringProperty("");
    private SimpleStringProperty recipientAccountId = new SimpleStringProperty("");
    private SimpleStringProperty sharedEventID = new SimpleStringProperty("");
    private SimpleStringProperty vpcEndpointId = new SimpleStringProperty("");

    private Event event;

    public EventTableModel(Event event) {

        this.event = event;

        setEventTime(event.getEventTime());
        setEventVersion(event.getEventVersion());
        setEventSource(event.getEventSource());
        setEventName(event.getEventName());
        setAwsRegion(event.getAwsRegion());
        setUserAgent(event.getUserAgent());
        setErrorCode(event.getErrorCode());
        setErrorMessage(event.getErrorMessage());
        setSourceIPAddress(event.getSourceIPAddress());
        setRequestID(event.getRequestId());
        setEventID(event.getEventId());
        setEventType(event.getEventType());
        setApiVersion(event.getApiVersion());
        setReadOnly(event.getReadOnly());
        setRecipientAccountId(event.getRecipientAccountId());
        setSharedEventID(event.getSharedEventID());
        setVpcEndpointId(event.getVpcEndpointId());
    }

    public String getEventTime() {
        return eventTime.get();
    }

    public void setEventTime(String eventTime) {
        this.eventTime.set(eventTime);
    }

    public String getEventVersion() {
        return eventVersion.get();
    }

    public void setEventVersion(String eventVersion) {
        this.eventVersion .set(eventVersion);
    }

    public String getEventSource() {
        return eventSource.get();
    }

    public void setEventSource(String eventSource) {
        this.eventSource.set(eventSource);
    }

    public String getEventName() {
        return eventName.get();
    }

    public void setEventName(String eventName) {
        this.eventName.set(eventName);
    }

    public String getAwsRegion() {
        return awsRegion.get();
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion.set(awsRegion);
    }

    public String getUserAgent() {
        return userAgent.get();
    }

    public void setUserAgent(String userAgent) {
        this.userAgent.set(userAgent);
    }

    public String getErrorCode() {
        return errorCode.get();
    }

    public void setErrorCode(String errorCode) {
        this.errorCode.set(errorCode);
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    public String getSourceIPAddress() {
        return sourceIPAddress.get();
    }

    public void setSourceIPAddress(String sourceIPAddress) {
        this.sourceIPAddress.set(sourceIPAddress);
    }

    public String getRequestID() {
        return requestID.get();
    }

    public void setRequestID(String requestID) {
        this.requestID.set(requestID);
    }

    public String getEventID() {
        return eventID.get();
    }

    public void setEventID(String eventID) {
        this.eventID.set(eventID);
    }

    public String getEventType() {
        return eventType.get();
    }

    public void setEventType(String eventType) {
        this.eventType.set(eventType);
    }

    public String getApiVersion() {
        return apiVersion.get();
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion.set(apiVersion);
    }

    public String getReadOnly() {
        return readOnly.get();
    }

    public void setReadOnly(String readOnly) {
        this.readOnly.set(readOnly);
    }

    public String getRecipientAccountId() {
        return recipientAccountId.get();
    }

    public void setRecipientAccountId(String recipientAccountId) {
        this.recipientAccountId.set(recipientAccountId);
    }

    public String getSharedEventID() {
        return sharedEventID.get();
    }

    public void setSharedEventID(String sharedEventID) {
        this.sharedEventID.set(sharedEventID);
    }

    public String getVpcEndpointId() {
        return vpcEndpointId.get();
    }

    public void setVpcEndpointId(String vpcEndpointId) {
        this.vpcEndpointId.set(vpcEndpointId);
    }

    public Event getEvent() {
        return this.event;
    }
}
