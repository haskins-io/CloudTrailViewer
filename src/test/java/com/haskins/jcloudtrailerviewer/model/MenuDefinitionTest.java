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
public class MenuDefinitionTest {
    
    /**
     * Test of getMenu method, of class MenuDefinition.
     */
    @Test
    public void testGetMenu() {
        
        String menu = "Jake";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setMenu(menu);
        assertEquals(menu, instance.getMenu());
    }

    /**
     * Test of setMenu method, of class MenuDefinition.
     */
    @Test
    public void testSetMenu() {
        
        String menu = "Jake";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setMenu(menu);
        assertEquals(menu, instance.getMenu());
    }

    /**
     * Test of getSubMenu method, of class MenuDefinition.
     */
    @Test
    public void testGetSubMenu() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setSubMenu(menu);
        assertEquals(menu, instance.getSubMenu());
    }

    /**
     * Test of setSubMenu method, of class MenuDefinition.
     */
    @Test
    public void testSetSubMenu() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setSubMenu(menu);
        assertEquals(menu, instance.getSubMenu());
    }

    /**
     * Test of getName method, of class MenuDefinition.
     */
    @Test
    public void testGetName() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setName(menu);
        assertEquals(menu, instance.getName());
    }

    /**
     * Test of setName method, of class MenuDefinition.
     */
    @Test
    public void testSetName() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setName(menu);
        assertEquals(menu, instance.getName());
    }
//
//    /**
//     * Test of getActions method, of class MenuDefinition.
//     */
//    @Test
//    public void testGetActions() {
//        System.out.println("getActions");
//        MenuDefinition instance = new MenuDefinition();
//        List<String> expResult = null;
//        List<String> result = instance.getActions();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setActions method, of class MenuDefinition.
//     */
//    @Test
//    public void testSetActions() {
//        System.out.println("setActions");
//        List<String> actions = null;
//        MenuDefinition instance = new MenuDefinition();
//        instance.setActions(actions);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getProperty method, of class MenuDefinition.
     */
    @Test
    public void testGetProperty() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setProperty(menu);
        assertEquals(menu, instance.getProperty());
    }

    /**
     * Test of setProperty method, of class MenuDefinition.
     */
    @Test
    public void testSetProperty() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setProperty(menu);
        assertEquals(menu, instance.getProperty());
    }

    /**
     * Test of getContains method, of class MenuDefinition.
     */
    @Test
    public void testGetContains() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setContains(menu);
        assertEquals(menu, instance.getContains());
    }

    /**
     * Test of setContains method, of class MenuDefinition.
     */
    @Test
    public void testSetContains() {
        
        String menu = "Elwood";
        MenuDefinition instance = new MenuDefinition();
        
        instance.setContains(menu);
        assertEquals(menu, instance.getContains());
    }
    
}
