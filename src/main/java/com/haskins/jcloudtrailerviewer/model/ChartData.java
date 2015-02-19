package com.haskins.jcloudtrailerviewer.model;

/**
 *
 * @author mark.haskins
 */
public class ChartData {
    
    private String chartType = "";
    private String chartStyle = "";
    private String chartSource = "";
    
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
    
}
