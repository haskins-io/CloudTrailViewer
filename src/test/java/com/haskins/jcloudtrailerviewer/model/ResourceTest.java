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
public class ResourceTest {
    
    /**
     * Test of getARN method, of class Resource.
     */
    @Test
    public void testGetARN() {
        
        String menu = "Jake";
        Resource instance = new Resource();
        
        instance.setARN(menu);
        assertEquals(menu, instance.getARN());
    }

    /**
     * Test of setARN method, of class Resource.
     */
    @Test
    public void testSetARN() {
        
        String menu = "Jake";
        Resource instance = new Resource();
        
        instance.setARN(menu);
        assertEquals(menu, instance.getARN());
    }

    /**
     * Test of getAccountId method, of class Resource.
     */
    @Test
    public void testGetAccountId() {
        
        String menu = "Jake";
        Resource instance = new Resource();
        
        instance.setAccountId(menu);
        assertEquals(menu, instance.getAccountId());
    }

    /**
     * Test of setAccountId method, of class Resource.
     */
    @Test
    public void testSetAccountId() {
        
        String menu = "Jake";
        Resource instance = new Resource();
        
        instance.setAccountId(menu);
        assertEquals(menu, instance.getAccountId());
    }

    /**
     * Test of toString method, of class Resource.
     */
    @Test
    public void testToString() {
        
        String arn = "Jake";
        String accountid = "Elwood";
        
        Resource instance = new Resource();
        
        instance.setARN(arn);
        instance.setAccountId(accountid);
        
        assertEquals("Jake, Elwood, ", instance.toString());
    }
    
}
