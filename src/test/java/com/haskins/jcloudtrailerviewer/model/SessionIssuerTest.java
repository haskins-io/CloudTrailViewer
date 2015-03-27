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
public class SessionIssuerTest {
    
    /**
     * Test of getType method, of class SessionIssuer.
     */
    @Test
    public void testGetType() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setType(expectedResult);
        assertEquals(expectedResult, instance.getType());
    }

    /**
     * Test of setType method, of class SessionIssuer.
     */
    @Test
    public void testSetType() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setType(expectedResult);
        assertEquals(expectedResult, instance.getType());
    }

    /**
     * Test of getPrincipalId method, of class SessionIssuer.
     */
    @Test
    public void testGetPrincipalId() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setPrincipalId(expectedResult);
        assertEquals(expectedResult, instance.getPrincipalId());
    }

    /**
     * Test of setPrincipalId method, of class SessionIssuer.
     */
    @Test
    public void testSetPrincipalId() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setPrincipalId(expectedResult);
        assertEquals(expectedResult, instance.getPrincipalId());
    }

    /**
     * Test of getArn method, of class SessionIssuer.
     */
    @Test
    public void testGetArn() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setArn(expectedResult);
        assertEquals(expectedResult, instance.getArn());
    }

    /**
     * Test of setArn method, of class SessionIssuer.
     */
    @Test
    public void testSetArn() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setArn(expectedResult);
        assertEquals(expectedResult, instance.getArn());
    }

    /**
     * Test of getAccountId method, of class SessionIssuer.
     */
    @Test
    public void testGetAccountId() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setAccountId(expectedResult);
        assertEquals(expectedResult, instance.getAccountId());
    }

    /**
     * Test of setAccountId method, of class SessionIssuer.
     */
    @Test
    public void testSetAccountId() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setAccountId(expectedResult);
        assertEquals(expectedResult, instance.getAccountId());
    }

    /**
     * Test of getUserName method, of class SessionIssuer.
     */
    @Test
    public void testGetUserName() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setUserName(expectedResult);
        assertEquals(expectedResult, instance.getUserName());
    }

    /**
     * Test of setUserName method, of class SessionIssuer.
     */
    @Test
    public void testSetUserName() {
        
        String expectedResult = "Jake";
        SessionIssuer instance = new SessionIssuer();
        
        instance.setUserName(expectedResult);
        assertEquals(expectedResult, instance.getUserName());
    }

//    /**
//     * Test of toString method, of class SessionIssuer.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        SessionIssuer instance = new SessionIssuer();
//        String expResult = "";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
