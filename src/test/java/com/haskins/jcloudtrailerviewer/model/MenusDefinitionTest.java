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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class MenusDefinitionTest {
    
    /**
     * Test of getMenus method, of class MenusDefinition.
     */
    @Test
    public void testGetMenus() {
        
        MenusDefinition instance = new MenusDefinition();
        List<MenuDefinition> expResult = new ArrayList();
        
        instance.setMenus(expResult);

        assertEquals(expResult, instance.getMenus());
    }

    /**
     * Test of setMenus method, of class MenusDefinition.
     */
    @Test
    public void testSetMenus() {
        
        MenusDefinition instance = new MenusDefinition();
        List<MenuDefinition> expResult = new ArrayList();
        
        instance.setMenus(expResult);

        assertEquals(expResult, instance.getMenus());
    }
    
}
