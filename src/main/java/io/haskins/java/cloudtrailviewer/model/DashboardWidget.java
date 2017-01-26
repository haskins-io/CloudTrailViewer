package io.haskins.java.cloudtrailviewer.model;

import java.io.Serializable;

/**
 * Created by markhaskins on 04/01/2017.
 */
public class DashboardWidget implements Serializable {

    private String widget;
    private String title;

    private String type;

    private int top = 0;

    private String style;
    private String orientation;
    private String categoryField;
    private String seriesField;

    private double xPos;
    private double yPos;
    private double width;
    private double height;

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeriesField() {
        return seriesField;
    }

    public void setSeriesField(String seriesField) {
        this.seriesField = seriesField;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public void setCategoryField(String categoryField) {
        this.categoryField = categoryField;
    }

    public double getXPos() {
        return xPos;
    }

    public void setXPos(double col) {
        this.xPos = col;
    }

    public double getYPos() {
        return yPos;
    }

    public void setYPos(double row) {
        this.yPos = row;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
