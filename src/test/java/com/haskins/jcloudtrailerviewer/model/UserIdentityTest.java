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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class UserIdentityTest {

    public UserIdentityTest() {
    }

    /**
     * Test of getType method, of class UserIdentity.
     */
    @Test
    public void testGetType() {

        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setType(expectedResult);
        assertEquals(expectedResult, instance.getType());
    }

    /**
     * Test of setType method, of class UserIdentity.
     */
    @Test
    public void testSetType() {

        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setType(expectedResult);
        assertEquals(expectedResult, instance.getType());
    }

    /**
     * Test of getPrincipalId method, of class UserIdentity.
     */
    @Test
    public void testGetPrincipalId() {

        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setPrincipalId(expectedResult);
        assertEquals(expectedResult, instance.getPrincipalId());
    }

    /**
     * Test of setPrincipalId method, of class UserIdentity.
     */
    @Test
    public void testSetPrincipalId() {

        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setPrincipalId(expectedResult);
        assertEquals(expectedResult, instance.getPrincipalId());
    }

    /**
     * Test of getArn method, of class UserIdentity.
     */
    @Test
    public void testGetArn() {

        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setArn(expectedResult);
        assertEquals(expectedResult, instance.getArn());
    }

    /**
     * Test of setArn method, of class UserIdentity.
     */
    @Test
    public void testSetArn() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setArn(expectedResult);
        assertEquals(expectedResult, instance.getArn());
    }

    /**
     * Test of getAccountId method, of class UserIdentity.
     */
    @Test
    public void testGetAccountId() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setAccountId(expectedResult);
        assertEquals(expectedResult, instance.getAccountId());
    }

    /**
     * Test of setAccountId method, of class UserIdentity.
     */
    @Test
    public void testSetAccountId() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setAccountId(expectedResult);
        assertEquals(expectedResult, instance.getAccountId());
    }

    /**
     * Test of getAccessKeyId method, of class UserIdentity.
     */
    @Test
    public void testGetAccessKeyId() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setAccessKeyId(expectedResult);
        assertEquals(expectedResult, instance.getAccessKeyId());
    }

    /**
     * Test of setAccessKeyId method, of class UserIdentity.
     */
    @Test
    public void testSetAccessKeyId() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setAccessKeyId(expectedResult);
        assertEquals(expectedResult, instance.getAccessKeyId());
    }

    /**
     * Test of getUserName method, of class UserIdentity.
     */
    @Test
    public void testGetUserName() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setUserName(expectedResult);
        assertEquals(expectedResult, instance.getUserName());
    }

    /**
     * Test of setUserName method, of class UserIdentity.
     */
    @Test
    public void testSetUserName() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setUserName(expectedResult);
        assertEquals(expectedResult, instance.getUserName());
    }

    /**
     * Test of getInvokedBy method, of class UserIdentity.
     */
    @Test
    public void testGetInvokedBy() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setInvokedBy(expectedResult);
        assertEquals(expectedResult, instance.getInvokedBy());
    }

    /**
     * Test of setInvokedBy method, of class UserIdentity.
     */
    @Test
    public void testSetInvokedBy() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setInvokedBy(expectedResult);
        assertEquals(expectedResult, instance.getInvokedBy());
    }

//    /**
//     * Test of getSessionContext method, of class UserIdentity.
//     */
//    @Test
//    public void testGetSessionContext() {
//        System.out.println("getSessionContext");
//        UserIdentity instance = new UserIdentity();
//        SessionContext expResult = null;
//        SessionContext result = instance.getSessionContext();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setSessionContext method, of class UserIdentity.
//     */
//    @Test
//    public void testSetSessionContext() {
//        System.out.println("setSessionContext");
//        SessionContext sessionContext = null;
//        UserIdentity instance = new UserIdentity();
//        instance.setSessionContext(sessionContext);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getWebIdFederationData method, of class UserIdentity.
     */
    @Test
    public void testGetWebIdFederationData() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setWebIdFederationData(expectedResult);
        assertEquals(expectedResult, instance.getWebIdFederationData());
    }

    /**
     * Test of setWebIdFederationData method, of class UserIdentity.
     */
    @Test
    public void testSetWebIdFederationData() {
        String expectedResult = "Jake";
        UserIdentity instance = new UserIdentity();

        instance.setWebIdFederationData(expectedResult);
        assertEquals(expectedResult, instance.getWebIdFederationData());
    }

//    /**
//     * Test of toString method, of class UserIdentity.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        UserIdentity instance = new UserIdentity();
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
