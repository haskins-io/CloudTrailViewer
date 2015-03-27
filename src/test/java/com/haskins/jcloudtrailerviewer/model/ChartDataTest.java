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
public class ChartDataTest {
    
    /**
     * Test of getChartType method, of class ChartData.
     */
    @Test
    public void testGetChartType() {
        
        ChartData instance = new ChartData();
        String expResult = "Top";

        instance.setChartType(expResult);
        String result = instance.getChartType();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setChartType method, of class ChartData.
     */
    @Test
    public void testSetChartType() {

        String chartType = "Top";
        ChartData instance = new ChartData();
        instance.setChartType(chartType);
        
        assertEquals(chartType, instance.getChartType());
    }

    /**
     * Test of getChartStyle method, of class ChartData.
     */
    @Test
    public void testGetChartStyle() {

        ChartData instance = new ChartData();
        
        String expResult = "Pie";
        
        instance.setChartStyle(expResult);
        String result = instance.getChartStyle();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setChartStyle method, of class ChartData.
     */
    @Test
    public void testSetChartStyle() {
        
        String chartStyle = "Pie";
        ChartData instance = new ChartData();
        instance.setChartStyle(chartStyle);
        
        assertEquals(chartStyle, instance.getChartStyle());
    }

    /**
     * Test of getChartSource method, of class ChartData.
     */
    @Test
    public void testGetChartSource() {

        ChartData instance = new ChartData();
        String expResult = "EventName";
        
        instance.setChartSource(expResult);
        String result = instance.getChartSource();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setChartSource method, of class ChartData.
     */
    @Test
    public void testSetChartSource() {
        
        String chartSource = "EventName";
        ChartData instance = new ChartData();
        instance.setChartSource(chartSource);
        
        assertEquals(chartSource, instance.getChartSource());
    }

    /**
     * Test of getTop method, of class ChartData.
     */
    @Test
    public void testGetTop() {
        
        ChartData instance = new ChartData();
        int expResult = 5;
        
        instance.setTop(expResult);
        int result = instance.getTop();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setTop method, of class ChartData.
     */
    @Test
    public void testSetTop() {
        
        int top = 5;
        ChartData instance = new ChartData();
        instance.setTop(top);
        
        assertEquals(top, instance.getTop());
    }

    /**
     * Test of isIgnoreRoot method, of class ChartData.
     */
    @Test
    public void testIsIgnoreRoot() {

        ChartData instance = new ChartData();
        boolean expResult = true;
        
        instance.setIgnoreRoot(expResult);
        boolean result = instance.isIgnoreRoot();
        
        assertEquals(expResult, result);

    }

    /**
     * Test of setIgnoreRoot method, of class ChartData.
     */
    @Test
    public void testSetIgnoreRoot() {
        
        boolean ignoreRoot = false;
        ChartData instance = new ChartData();
        instance.setIgnoreRoot(ignoreRoot);
        
        assertEquals(ignoreRoot, instance.isIgnoreRoot());
    }
    
}
