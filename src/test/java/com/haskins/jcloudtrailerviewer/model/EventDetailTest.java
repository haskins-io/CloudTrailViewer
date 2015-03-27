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
public class EventDetailTest {
    
    public void testConstructor() {
        
        String label = "Jake";
        String detail = "Elwood";
        
        EventDetail instance = new EventDetail(label, detail);
        
        assertEquals(label, instance.getLabel());
        assertEquals(detail, instance.getDetail());
    }
    
    /**
     * Test of setLabel method, of class EventDetail.
     */
    @Test
    public void testSetLabel() {
        
        String label = "Jake";
        EventDetail instance = new EventDetail("","");
        
        instance.setLabel(label);
        assertEquals(label, instance.getLabel());
    }

    /**
     * Test of getLabel method, of class EventDetail.
     */
    @Test
    public void testGetLabel() {
        
        String label = "Jake";
        EventDetail instance = new EventDetail("","");
        
        instance.setLabel(label);
        assertEquals(label, instance.getLabel());
    }

    /**
     * Test of setDetail method, of class EventDetail.
     */
    @Test
    public void testSetDetail() {
        
        String detail = "Elwood";
        EventDetail instance = new EventDetail("","");
        
        instance.setDetail(detail);
        assertEquals(detail, instance.getDetail());
    }

    /**
     * Test of getDetail method, of class EventDetail.
     */
    @Test
    public void testGetDetail() {
        
        String detail = "Elwood";
        EventDetail instance = new EventDetail("","");
        
        instance.setDetail(detail);
        assertEquals(detail, instance.getDetail());
    }
    
}
