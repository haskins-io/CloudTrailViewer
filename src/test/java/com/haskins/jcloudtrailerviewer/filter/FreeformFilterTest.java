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
package com.haskins.jcloudtrailerviewer.filter;

import com.haskins.jcloudtrailerviewer.AbstractTest;
import java.lang.reflect.Field;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class FreeformFilterTest extends AbstractTest {

    /**
     * Test of setNeedle method, of class FreeformFilter.
     */
    @Test
    public void testSetNeedle() {
        
        String expectedValue = "Lemming";
        FreeformFilter instance = new FreeformFilter();
        instance.setNeedle(expectedValue);
        
        String returnedValue = null;
        
        try {

            Field privateStringField = FreeformFilter.class.getDeclaredField("needle");
            privateStringField.setAccessible(true);
            returnedValue = (String)privateStringField.get(instance);
            
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            fail(ex.getMessage());
        }
        
        assertEquals(expectedValue, returnedValue);
    }

    /**
     * Test of passesFilter method, of class FreeformFilter.
     */
    @Test
    public void testPassesFilter() {
        
        FreeformFilter instance = new FreeformFilter();
        instance.setNeedle("Describe");
        
        boolean expResult = true;
        boolean result = instance.passesFilter(eventOne);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of passesFilter method, of class FreeformFilter.
     */
    @Test
    public void testFailsFilter() {
        
        FreeformFilter instance = new FreeformFilter();
        instance.setNeedle("Lemming");
        
        boolean expResult = false;
        boolean result = instance.passesFilter(eventOne);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of passesFilter method, of class FreeformFilter.
     */
    @Test
    public void testNoNeedle() {
        
        FreeformFilter instance = new FreeformFilter();
        
        boolean expResult = true;
        boolean result = instance.passesFilter(eventOne);
        
        assertEquals(expResult, result);
    }
}
