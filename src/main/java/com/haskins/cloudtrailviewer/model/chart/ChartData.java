
package com.haskins.cloudtrailviewer.model.chart;

import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark.haskins
 */
public class ChartData {
    
    /** Constant that defines a Horizontal Chart Plot */
    public static final PlotOrientation HORIZONTAL = PlotOrientation.HORIZONTAL;
    
    /** Constant that defines a Vertical Chart Plot */
    public static final PlotOrientation VERTICAL = PlotOrientation.VERTICAL;
    
    private String chartType = "Top";
    private String chartStyle = "Pie";
    private String chartSource = "EventName";
    private PlotOrientation orientation = VERTICAL;
    
    private boolean ignoreRoot = false;
    
    private int top = 5;

    /**
     * 
     * @return 
     */
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
