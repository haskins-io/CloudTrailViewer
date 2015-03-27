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
public class EventTimestampComparatorTest extends AbstractTest {
    
    /**
     * Test of compare method, of class EventTimestampComparator.
     */
    @Test
    public void testCompare1() {
        
        EventTimestampComparator instance = new EventTimestampComparator();
        
        EventUtils.addTimestamp(eventOne);
        EventUtils.addTimestamp(eventTwo);
        
        int expResult = 1;
        int result = instance.compare(eventOne, eventTwo);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of compare method, of class EventTimestampComparator.
     */
    @Test
    public void testCompare2() {
        
        EventTimestampComparator instance = new EventTimestampComparator();
        
        EventUtils.addTimestamp(eventOne);
        EventUtils.addTimestamp(eventTwo);
        
        int expResult = -1;
        int result = instance.compare(eventTwo, eventOne);
        
        assertEquals(expResult, result);
    }
}
