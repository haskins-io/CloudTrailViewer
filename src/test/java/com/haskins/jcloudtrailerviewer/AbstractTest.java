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
package com.haskins.jcloudtrailerviewer;

import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.SessionContext;
import com.haskins.jcloudtrailerviewer.model.SessionIssuer;
import com.haskins.jcloudtrailerviewer.model.UserIdentity;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class AbstractTest {
    
    protected Event eventOne = new Event();
    protected Event eventTwo = new Event();
    protected Event eventThree = new Event();
    
    @Before
    public void setUp() {
        
        createEventOne();
        createEventTwo();
        createEventThree();
    }
    
    @Test
    public void testSetNeedle() {
        assertTrue("Test so things work", true);
    }
    
    private void createEventOne() {
        
        SessionIssuer si = new SessionIssuer();
        si.setType("Role");
        si.setPrincipalId("principal_id");
        si.setArn("arn:aws:iam::123456789012:role/iam_role");
        si.setAccountId("123456789012");
        si.setUserName("iam_role");
        
        SessionContext sc = new SessionContext();
        Map attr = new HashMap();
        attr.put("mfaAuthenticated", "false");
        attr.put("creationDate", "2015-01-11T13:49:38Z");
        sc.setAttributes(attr);
        sc.setSessionIssuer(si);
        
        UserIdentity ui = new UserIdentity();
        ui.setType("AssumedRole");
        ui.setPrincipalId("principal_id:session_name");
        ui.setArn("arn:aws:sts::123456789012:assumed-role/role_name/session_name");
        ui.setAccountId("123456789012");
        ui.setAccessKeyId("SomeRandomKey");
        ui.setSessionContext(sc);
        
        eventOne.setEventVersion("1.02");
        eventOne.setUserIdentity(ui);
        eventOne.setEventTime("2015-01-11T13:49:38Z");
        eventOne.setEventSource("ec2.amazonaws.com");
        eventOne.setEventName("DescribeInstances");
        eventOne.setAwsRegion("eu-west-1");
        eventOne.setSourceIPAddress("54.144.171.33");
        eventOne.setUserAgent("Boto/2.27.0 Python/2.7.5 Linux/3.14.20-20.44.amzn1.x86_64");
        eventOne.setRequestParameters(null);
        eventOne.setResponseElements(null);
        eventOne.setRequestId("80e265fd-6a64-48ec-a58e-268992df8426");
        eventOne.setEventId("c723fa67-ffbb-4a32-884a-4e0ff7d31e7a");
        eventOne.setEventType("AwsApiCall");
        eventOne.setRecipientAccountId("123456789012");
    }
    
    private void createEventTwo() {
        
        SessionIssuer si = new SessionIssuer();
        si.setType("Role");
        si.setPrincipalId("principal_id");
        si.setArn("arn:aws:iam::210987654321:role/iam_role");
        si.setAccountId("210987654321");
        si.setUserName("iam_role");
        
        SessionContext sc = new SessionContext();
        Map attr = new HashMap();
        attr.put("mfaAuthenticated", "false");
        attr.put("creationDate", "2015-01-11T13:49:38Z");
        sc.setAttributes(attr);
        sc.setSessionIssuer(si);
        
        UserIdentity ui = new UserIdentity();
        ui.setType("AssumedRole");
        ui.setPrincipalId("principal_id:session_name");
        ui.setArn("arn:aws:sts::210987654321:assumed-role/role_name/session_name");
        ui.setAccountId("210987654321");
        ui.setAccessKeyId("SomeRandomKey");
        ui.setSessionContext(sc);
        
        eventTwo.setEventVersion("1.02");
        eventTwo.setUserIdentity(ui);
        eventTwo.setEventTime("2014-01-11T13:49:38Z");
        eventTwo.setEventSource("elasticbeanstalk.amazonaws.com");
        eventTwo.setEventName("DescribeEnvironments");
        eventTwo.setAwsRegion("eu-central-1");
        eventTwo.setSourceIPAddress("54.14.71.3");
        eventTwo.setUserAgent("Boto/2.27.0 Python/2.7.5 Linux/3.14.20-20.44.amzn1.x86_64");
        eventTwo.setRequestParameters(null);
        eventTwo.setResponseElements(null);
        eventTwo.setRequestId("80e265fd-6a64-48ec-a58e-268992df8426");
        eventTwo.setEventId("c723fa67-ffbb-4a32-884a-4e0ff7d31e7a");
        eventTwo.setEventType("AwsApiCall");
        eventTwo.setRecipientAccountId("210987654321");
    }
    
    private void createEventThree() {
                
        SessionContext sc = new SessionContext();
        Map attr = new HashMap();
        attr.put("mfaAuthenticated", "false");
        attr.put("creationDate", "2015-01-11T13:49:38Z");
        sc.setAttributes(attr);
        
        UserIdentity ui = new UserIdentity();
        ui.setType("AssumedRole");
        ui.setPrincipalId("principal_id:session_name");
        ui.setArn("arn:aws:sts::210987654321:assumed-role/role_name/session_name");
        ui.setAccountId("210987654321");
        ui.setAccessKeyId("SomeRandomKey");
        ui.setSessionContext(sc);
        ui.setUserName("iam_role");
        
        eventThree.setEventVersion("1.02");
        eventThree.setUserIdentity(ui);
        eventThree.setEventTime("2014-01-11T13:49:38Z");
        eventThree.setEventSource("elasticbeanstalk.amazonaws.com");
        eventThree.setEventName("DescribeEnvironments");
        eventThree.setAwsRegion("eu-central-1");
        eventThree.setSourceIPAddress("54.14.71.3");
        eventThree.setUserAgent("Boto/2.27.0 Python/2.7.5 Linux/3.14.20-20.44.amzn1.x86_64");
        eventThree.setRequestParameters(null);
        eventThree.setResponseElements(null);
        eventThree.setRequestId("80e265fd-6a64-48ec-a58e-268992df8426");
        eventThree.setEventId("c723fa67-ffbb-4a32-884a-4e0ff7d31e7a");
        eventThree.setEventType("AwsApiCall");
        eventThree.setRecipientAccountId("210987654321");
    }
}
