/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.jcloudtrailerviewer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class PropertiesSingletonTest {
    
    public PropertiesSingletonTest() {
    }

    /**
     * Test of getInstance method, of class PropertiesSingleton.
     */
    @Test
    public void testGetInstance() {        
        assertNotNull(PropertiesSingleton.getInstance());
    }

    /**
     * Test of setProperty method, of class PropertiesSingleton.
     */
    @Test
    public void testSetProperty() {
        
        String key = "TestKey";
        String value = "TestValue";
        PropertiesSingleton.getInstance().setProperty(key, value);
        
        String newValue = PropertiesSingleton.getInstance().getProperty(key);
        
        assertEquals(value, newValue);
    }

    /**
     * Test of getProperty method, of class PropertiesSingleton.
     */
    @Test
    public void testGetProperty() {
        
        String key = "TestKey";
        String value = "TestValue";
        PropertiesSingleton.getInstance().setProperty(key, value);
        
        String newValue = PropertiesSingleton.getInstance().getProperty(key);
        
        assertEquals(value, newValue);
    }

    /**
     * Test of configLoaded method, of class PropertiesSingleton.
     */
    @Test
    public void testValidS3Credentials() {
        
        PropertiesSingleton.getInstance();
        boolean expResult = true;
        boolean result = PropertiesSingleton.getInstance().validS3Credentials();
        
        assertEquals(expResult, result);
    }
    
}
