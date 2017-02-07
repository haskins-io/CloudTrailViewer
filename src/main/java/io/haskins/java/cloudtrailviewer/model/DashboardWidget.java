/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.model;

import java.io.Serializable;

/**
 * Class that models a widget
 *
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

    private Object payload;

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

    public void setPayload(Object obj) {
        this.payload = obj;
    }
    public Object getPayload() {
        return this.payload;
    }
}
