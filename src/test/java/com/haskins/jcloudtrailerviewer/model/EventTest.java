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
package com.haskins.jcloudtrailerviewer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class EventTest {
    

    /**
     * Test of setRawJSON method, of class Event.
     */
    @Test
    public void testSetRawJSON() {
        
        String json = "{}";
        Event instance = new Event();
        instance.setRawJSON(json);

        assertEquals(json, instance.getRawJSON());
    }

    /**
     * Test of getRawJSON method, of class Event.
     */
    @Test
    public void testGetRawJSON() {
        
        Event instance = new Event();
        String expResult = "{}";
        
        instance.setRawJSON(expResult);
        String result = instance.getRawJSON();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setTimestamp method, of class Event.
     */
    @Test
    public void testSetTimestamp() {
        
        long millis = 1000L;
        Event instance = new Event();
        instance.setTimestamp(millis);
        
        assertEquals(millis, instance.getTimestamp());
    }

    /**
     * Test of getTimestamp method, of class Event.
     */
    @Test
    public void testGetTimestamp() {
        
        Event instance = new Event();
        long expResult = 1000L;
        
        instance.setTimestamp(expResult);
        long result = instance.getTimestamp();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventVersion method, of class Event.
     */
    @Test
    public void testGetEventVersion() {
        
        Event instance = new Event();
        String expResult = "1.02";
        
        instance.setEventVersion(expResult);
        String result = instance.getEventVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventVersion method, of class Event.
     */
    @Test
    public void testSetEventVersion() {
        
        String eventVersion = "1.02";
        Event instance = new Event();
        instance.setEventVersion(eventVersion);
        
        assertEquals(eventVersion, instance.getEventVersion());
    }

    /**
     * Test of getUserIdentity method, of class Event.
     */
    @Test
    public void testGetUserIdentity() {
        
        Event instance = new Event();
        UserIdentity expResult = new UserIdentity();
        
        instance.setUserIdentity(expResult);
        UserIdentity result = instance.getUserIdentity();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setUserIdentity method, of class Event.
     */
    @Test
    public void testSetUserIdentity() {
        
        UserIdentity userIdentity = new UserIdentity();
        Event instance = new Event();
        instance.setUserIdentity(userIdentity);
        
        assertEquals(userIdentity, instance.getUserIdentity());
    }

    /**
     * Test of getEventTime method, of class Event.
     */
    @Test
    public void testGetEventTime() {
        
        Event instance = new Event();
        String expResult = "2015-01-11T13:49:38Z";
        
        instance.setEventTime(expResult);
        String result = instance.getEventTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventTime method, of class Event.
     */
    @Test
    public void testSetEventTime() {
        
        String eventTime = "2015-01-11T13:49:38Z";
        Event instance = new Event();
        instance.setEventTime(eventTime);
        
        assertEquals(eventTime, instance.getEventTime());
    }

    /**
     * Test of getEventSource method, of class Event.
     */
    @Test
    public void testGetEventSource() {
        
        Event instance = new Event();
        String expResult = "ec2.amazonaws.com";
        
        instance.setEventSource(expResult);
        String result = instance.getEventSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventSource method, of class Event.
     */
    @Test
    public void testSetEventSource() {
        
        String eventSource = "ec2.amazonaws.com";
        Event instance = new Event();
        instance.setEventSource(eventSource);
        
        assertEquals(eventSource, instance.getEventSource());
    }

    /**
     * Test of getEventName method, of class Event.
     */
    @Test
    public void testGetEventName() {
        
        Event instance = new Event();
        String expResult = "DescribeInstances";
        
        instance.setEventName(expResult);
        String result = instance.getEventName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventName method, of class Event.
     */
    @Test
    public void testSetEventName() {
        
        String eventName = "DescribeInstances";
        Event instance = new Event();
        instance.setEventName(eventName);
        
        assertEquals(eventName, instance.getEventName());
    }

    /**
     * Test of getAwsRegion method, of class Event.
     */
    @Test
    public void testGetAwsRegion() {
        
        Event instance = new Event();
        String expResult = "eu-west-1";
        
        instance.setAwsRegion(expResult);
        String result = instance.getAwsRegion();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAwsRegion method, of class Event.
     */
    @Test
    public void testSetAwsRegion() {
        
        String awsRegion = "eu-west-1";
        Event instance = new Event();
        instance.setAwsRegion(awsRegion);
        
        assertEquals(awsRegion, instance.getAwsRegion());
    }

    /**
     * Test of getSourceIPAddress method, of class Event.
     */
    @Test
    public void testGetSourceIPAddress() {
        
        Event instance = new Event();
        String expResult = "54.144.171.33";
        
        instance.setSourceIPAddress(expResult);
        String result = instance.getSourceIPAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSourceIPAddress method, of class Event.
     */
    @Test
    public void testSetSourceIPAddress() {
        
        String sourceIPAddress = "54.144.171.33";
        Event instance = new Event();
        instance.setSourceIPAddress(sourceIPAddress);
        
        assertEquals(sourceIPAddress, instance.getSourceIPAddress());
    }

    /**
     * Test of getUserAgent method, of class Event.
     */
    @Test
    public void testGetUserAgent() {
        
        Event instance = new Event();
        String expResult = "Boto/2.27.0 Python/2.7.5 Linux/3.14.20-20.44.amzn1.x86_64";
        
        instance.setUserAgent(expResult);
        String result = instance.getUserAgent();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUserAgent method, of class Event.
     */
    @Test
    public void testSetUserAgent() {
        
        String userAgent = "Boto/2.27.0 Python/2.7.5 Linux/3.14.20-20.44.amzn1.x86_64";
        Event instance = new Event();
        instance.setUserAgent(userAgent);
        
        assertEquals(userAgent, instance.getUserAgent());
    }

    /**
     * Test of getRequestParameters method, of class Event.
     */
    @Test
    public void testGetRequestParameters() {
        
        Event instance = new Event();
        
        Map expResult = new HashMap();
        
        instance.setRequestParameters(expResult);
        Map result = instance.getRequestParameters();
        
        assertEquals(expResult, result);

    }

    /**
     * Test of setRequestParameters method, of class Event.
     */
    @Test
    public void testSetRequestParameters() {
        
        Event instance = new Event();
        
        Map expResult = new HashMap();
        
        instance.setRequestParameters(expResult);
        Map result = instance.getRequestParameters();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getResponseElements method, of class Event.
     */
    @Test
    public void testGetResponseElements() {
        
        Event instance = new Event();
        
        Map expResult = new HashMap();
        
        instance.setResponseElements(expResult);
        Map result = instance.getResponseElements();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setResponseElements method, of class Event.
     */
    @Test
    public void testSetResponseElements() {
        
        Event instance = new Event();
        
        Map expResult = new HashMap();
        
        instance.setResponseElements(expResult);
        Map result = instance.getResponseElements();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequestId method, of class Event.
     */
    @Test
    public void testGetRequestId() {
        
        Event instance = new Event();
        String expResult = "80e265fd-6a64-48ec-a58e-268992df8426";
        
        instance.setRequestId(expResult);
        String result = instance.getRequestId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRequestId method, of class Event.
     */
    @Test
    public void testSetRequestId() {
        
        String requestId = "80e265fd-6a64-48ec-a58e-268992df8426";
        Event instance = new Event();
        instance.setRequestId(requestId);
        
        assertEquals(requestId, instance.getRequestId());
    }

    /**
     * Test of getEventId method, of class Event.
     */
    @Test
    public void testGetEventId() {
        
        Event instance = new Event();
        String expResult = "c723fa67-ffbb-4a32-884a-4e0ff7d31e7a";
        
        instance.setEventId(expResult);
        String result = instance.getEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventId method, of class Event.
     */
    @Test
    public void testSetEventId() {
        
        String eventId = "c723fa67-ffbb-4a32-884a-4e0ff7d31e7a";
        Event instance = new Event();
        instance.setEventId(eventId);
        
        assertEquals(eventId, instance.getEventId());
    }

    /**
     * Test of getErrorCode method, of class Event.
     */
    @Test
    public void testGetErrorCode() {
        Event instance = new Event();
        String expResult = "None";
        
        instance.setErrorCode(expResult);
        String result = instance.getErrorCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of setErrorCode method, of class Event.
     */
    @Test
    public void testSetErrorCode() {
        
        String errorCode = "None";
        Event instance = new Event();
        instance.setErrorCode(errorCode);
        
        assertEquals(errorCode, instance.getErrorCode());
    }

    /**
     * Test of getErrorMessage method, of class Event.
     */
    @Test
    public void testGetErrorMessage() {
        
        Event instance = new Event();
        String expResult = "None";
        
        instance.setErrorMessage(expResult);
        String result = instance.getErrorMessage();
        assertEquals(expResult, result);
    }

    /**
     * Test of setErrorMessage method, of class Event.
     */
    @Test
    public void testSetErrorMessage() {
        
        String errorMessage = "None";
        Event instance = new Event();
        instance.setErrorMessage(errorMessage);
        
        assertEquals(errorMessage, instance.getErrorMessage());
    }

    /**
     * Test of getReadOnly method, of class Event.
     */
    @Test
    public void testGetReadOnly() {
        
        Event instance = new Event();
        String expResult = "False";
        
        instance.setReadOnly(expResult);
        String result = instance.getReadOnly();
        assertEquals(expResult, result);
    }

    /**
     * Test of setReadOnly method, of class Event.
     */
    @Test
    public void testSetReadOnly() {
        
        String readOnly = "False";
        Event instance = new Event();
        instance.setReadOnly(readOnly);
        
        assertEquals(readOnly, instance.getReadOnly());
    }

    /**
     * Test of getResources method, of class Event.
     */
    @Test
    public void testGetResources() {

        Event instance = new Event();
        List expResult = new ArrayList();
        
        instance.setResources(expResult);
        List result = instance.getResources();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setResources method, of class Event.
     */
    @Test
    public void testSetResources() {

        Event instance = new Event();
        List expResult = new ArrayList();
        
        instance.setResources(expResult);
        List result = instance.getResources();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventType method, of class Event.
     */
    @Test
    public void testGetEventType() {
        
        Event instance = new Event();
        String expResult = "AwsApiCall";
        
        instance.setEventType(expResult);
        String result = instance.getEventType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEventType method, of class Event.
     */
    @Test
    public void testSetEventType() {
        
        String eventType = "AwsApiCall";
        Event instance = new Event();
        instance.setEventType(eventType);
        
        assertEquals(eventType, instance.getEventType());
    }

    /**
     * Test of getRecipientAccountId method, of class Event.
     */
    @Test
    public void testGetRecipientAccountId() {
        
        Event instance = new Event();
        String expResult = "123456789012";
        
        instance.setRecipientAccountId(expResult);
        String result = instance.getRecipientAccountId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRecipientAccountId method, of class Event.
     */
    @Test
    public void testSetRecipientAccountId() {
        
        String recipientAccountId = "123456789012";
        Event instance = new Event();
        instance.setRecipientAccountId(recipientAccountId);
        
        assertEquals(recipientAccountId, instance.getRecipientAccountId());
    }

    /**
     * Test of getAdditionalEventData method, of class Event.
     */
    @Test
    public void testGetAdditionalEventData() {
        
        Event instance = new Event();
        AdditionalEventData expResult = new AdditionalEventData();
        
        instance.setAdditionalEventData(expResult);
        AdditionalEventData result = instance.getAdditionalEventData();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAdditionalEventData method, of class Event.
     */
    @Test
    public void testSetAdditionalEventData() {
        
        Event instance = new Event();
        AdditionalEventData expResult = new AdditionalEventData();
        
        instance.setAdditionalEventData(expResult);
        AdditionalEventData result = instance.getAdditionalEventData();
        assertEquals(expResult, result);
    }

    /**
     * Test of getApiVersion method, of class Event.
     */
    @Test
    public void testGetApiVersion() {

        Event instance = new Event();
        String expResult = "1.02";
        
        instance.setApiVersion(expResult);
        String result = instance.getApiVersion();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setApiVersion method, of class Event.
     */
    @Test
    public void testSetApiVersion() {
        
        Event instance = new Event();
        String expResult = "1.02";
        
        instance.setApiVersion(expResult);
        String result = instance.getApiVersion();
        
        assertEquals(expResult, result);
    }

//    /**
//     * Test of toString method, of class Event.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        Event instance = new Event();
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
