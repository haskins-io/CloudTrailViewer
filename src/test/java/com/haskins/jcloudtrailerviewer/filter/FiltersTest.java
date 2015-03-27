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
import com.haskins.jcloudtrailerviewer.model.Event;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark.haskins
 */
public class FiltersTest extends AbstractTest {
    
    /**
     * Test of addEventFilter method, of class Filters.
     */
    @Test
    public void testAddEventFilter() {
        
        FreeformFilter filter = new FreeformFilter();
        
        Filters instance = new Filters();
        instance.addEventFilter(filter);
        
        List<EventFilter> filters = null;
        
        try {

            Field privateListField = Filters.class.getDeclaredField("filters");
            privateListField.setAccessible(true);
            filters = (List)privateListField.get(instance);
            
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
            fail(ex.getMessage());
        }
        
        if (filters != null) {
            assertEquals(1, filters.size());
        }
    }

    /**
     * Test of setFilterCriteria method, of class Filters.
     */
    @Test
    public void testSetFilterCriteria() {
       
        FreeformFilter filter = new FreeformFilter();
        
        Filters instance = new Filters();
        instance.addEventFilter(filter);
        
        String expectedValue = "Lemming";
        instance.setFilterCriteria(expectedValue);
        
        List<EventFilter> filters = null;
        
        try {

            Field privateListField = Filters.class.getDeclaredField("filters");
            privateListField.setAccessible(true);
            filters = (List)privateListField.get(instance);
            
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
           fail(ex.getMessage());
        }
        
        if (filters != null) {
            
            FreeformFilter filter2 = (FreeformFilter)filters.get(0);
            
            String returnedValue = null;
            try {

                Field privateStringField = FreeformFilter.class.getDeclaredField("needle");
                privateStringField.setAccessible(true);
                returnedValue = (String)privateStringField.get(filter2);

            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
                fail(ex.getMessage());
            }

            assertEquals(expectedValue, returnedValue);
        }
    }

    /**
     * Test of filterEvents method, of class Filters.
     */
    @Test
    public void testFilterEvents1() {
        
        List<Event> events = new ArrayList<>();
        events.add(eventOne);
        
        RegionFilter filter = new RegionFilter();
        
        Filters filters = new Filters();
        filters.addEventFilter(filter);
        filters.setFilterCriteria("eu-west-1");
        
        List<Event> result = filters.filterEvents(events);
        
        assertEquals(1, result.size());
    }
    
    /**
     * Test of filterEvents method, of class Filters.
     */
    @Test
    public void testFilterEvents2() {
        
        List<Event> events = new ArrayList<>();
        events.add(eventOne);
        events.add(eventTwo);
        
        RegionFilter filter = new RegionFilter();
        
        Filters filters = new Filters();
        filters.addEventFilter(filter);
        filters.setFilterCriteria("eu-west-1");
        
        List<Event> result = filters.filterEvents(events);
        
        assertEquals(1, result.size());
    }

    /**
     * Test of passesFilter method, of class Filters.
     */
    @Test
    public void testPassesFilter() {
                
        RegionFilter regionFilter = new RegionFilter();
        AccountFilter acctFilter = new AccountFilter();
        
        Filters filters = new Filters();
        filters.addEventFilter(regionFilter);
        filters.addEventFilter(acctFilter);
        
        filters.setFilterCriteria("123456789012");
        
        assertEquals(true, filters.passesFilter(eventOne));
    }   
}
