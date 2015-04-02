/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.model;

import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark.haskins
 */
public class ChartData {
    
    public static final PlotOrientation HORIZONTAL = PlotOrientation.HORIZONTAL;
    public static final PlotOrientation VERTICAL = PlotOrientation.VERTICAL;
    
    private String chartType = "Top";
    private String chartStyle = "Pie";
    private String chartSource = "EventName";
    private PlotOrientation orientation = VERTICAL;
    
    private boolean ignoreRoot = false;
    
    private int top = 5;

    public String getChartType() {
        return chartType;
    }
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartStyle() {
        return chartStyle;
    }
    public void setChartStyle(String chartStyle) {
        this.chartStyle = chartStyle;
    }

    public String getChartSource() {
        return chartSource;
    }
    public void setChartSource(String chartSource) {
        this.chartSource = chartSource;
    }

    public int getTop() {
        return top;
    }
    public void setTop(int top) {
        this.top = top;
    }

    public boolean isIgnoreRoot() {
        return ignoreRoot;
    }
    public void setIgnoreRoot(boolean ignoreRoot) {
        this.ignoreRoot = ignoreRoot;
    }

    public PlotOrientation getOrientation() {
        return orientation;
    }
    public void setOrientation(PlotOrientation orientation) {
        this.orientation = orientation;
    }
    
}
