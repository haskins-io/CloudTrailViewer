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
public class AdditionalEventDataTest {
    
    /**
     * Test of getSamlProviderArn method, of class AdditionalEventData.
     */
    @Test
    public void testGetSamlProviderArn() {
        
        AdditionalEventData instance = new AdditionalEventData();
        String expResult = "Lemming";
        instance.setSamlProviderArn(expResult);
        String result = instance.getSamlProviderArn();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSamlProviderArn method, of class AdditionalEventData.
     */
    @Test
    public void testSetSamlProviderArn() {

        String SamlProviderArn = "Lemming";
        AdditionalEventData instance = new AdditionalEventData();
        instance.setSamlProviderArn(SamlProviderArn);
        
        assertEquals(SamlProviderArn, instance.getSamlProviderArn());
    }

    /**
     * Test of getMobileVersion method, of class AdditionalEventData.
     */
    @Test
    public void testGetMobileVersion() {

        AdditionalEventData instance = new AdditionalEventData();
        String expResult = "Lemming";
        instance.setMobileVersion(expResult);
        String result = instance.getMobileVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMobileVersion method, of class AdditionalEventData.
     */
    @Test
    public void testSetMobileVersion() {

        String MobileVersion = "Lemming";
        AdditionalEventData instance = new AdditionalEventData();
        instance.setMobileVersion(MobileVersion);
        
        assertEquals(MobileVersion, instance.getMobileVersion());
    }

    /**
     * Test of getLoginTo method, of class AdditionalEventData.
     */
    @Test
    public void testGetLoginTo() {
        
        AdditionalEventData instance = new AdditionalEventData();
        String expResult = "Lemming";
        instance.setLoginTo(expResult);
        String result = instance.getLoginTo();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLoginTo method, of class AdditionalEventData.
     */
    @Test
    public void testSetLoginTo() {

        String LoginTo = "Lemming";
        AdditionalEventData instance = new AdditionalEventData();
        instance.setLoginTo(LoginTo);
        
        assertEquals(LoginTo, instance.getLoginTo());
    }

    /**
     * Test of getMFAUsed method, of class AdditionalEventData.
     */
    @Test
    public void testGetMFAUsed() {

        AdditionalEventData instance = new AdditionalEventData();
        String expResult = "Lemming";
        instance.setMFAUsed(expResult);
        String result = instance.getMFAUsed();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMFAUsed method, of class AdditionalEventData.
     */
    @Test
    public void testSetMFAUsed() {
        
        String MFAUsed = "Lemming";
        AdditionalEventData instance = new AdditionalEventData();
        instance.setMFAUsed(MFAUsed);
        
        assertEquals(MFAUsed, instance.getMFAUsed());
    }
    
}
