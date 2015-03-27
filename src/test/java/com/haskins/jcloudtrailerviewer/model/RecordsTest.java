/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.jcloudtrailerviewer.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class RecordsTest {
    
    public RecordsTest() {
    }

    /**
     * Test of getLogEvents method, of class Records.
     */
    @Test
    public void testGetLogEvents() {
        
        Records instance = new Records();
        List<Event> expResult = new ArrayList();
        
        instance.setLogEvents(expResult);

        assertEquals(expResult, instance.getLogEvents());
    }

    /**
     * Test of setLogEvents method, of class Records.
     */
    @Test
    public void testSetLogEvents() {
        
        Records instance = new Records();
        List<Event> expResult = new ArrayList();
        
        instance.setLogEvents(expResult);

        assertEquals(expResult, instance.getLogEvents());
    }
    
}
