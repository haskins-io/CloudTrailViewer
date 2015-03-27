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
package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class EventUtilsTest extends AbstractTest {
    
    /**
     * Test of addTimestamp method, of class EventUtils.
     */
    @Test
    public void testAddTimestamp() {
        
        EventUtils.addTimestamp(eventOne);
        assertEquals(1420984178000l, eventOne.getTimestamp());
    }

    /**
     * Test of getTimestamp method, of class EventUtils.
     */
    @Test
    public void testGetTimestamp() {

        long expResult = 1420984178000l;
        long result = EventUtils.getTimestamp("2015-01-11T13:49:38Z");
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventProperty method, of class EventUtils.
     */
    @Test
    public void testGetEventProperty() {
        
        String property = "eventName";
        String expResult = "DescribeInstances";
        
        String result = EventUtils.getEventProperty(property, eventOne);
        
        assertEquals(expResult, result);
    }
    
}
