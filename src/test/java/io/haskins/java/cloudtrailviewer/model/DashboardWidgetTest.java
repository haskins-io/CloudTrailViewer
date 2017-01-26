package io.haskins.java.cloudtrailviewer.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by markhaskins on 24/01/2017.
 */
public class DashboardWidgetTest {

    private DashboardWidget dw;

    @Before
    public void init() {
        dw = new DashboardWidget();
    }

    @Test
    public void getWidget() throws Exception {
        String widget = "PieChart";
        dw.setWidget(widget);
        assertEquals(widget, dw.getWidget());
    }

    @Test
    public void getType() throws Exception {
        String type = "Top";
        dw.setType(type);
        assertEquals(type, dw.getType());
    }

    @Test
    public void getSeriesField() throws Exception {
        String series = "EventName";
        dw.setSeriesField(series);
        assertEquals(series, dw.getSeriesField());
    }

    @Test
    public void getTitle() throws Exception {
        String title = "Top Events";
        dw.setTitle(title);
        assertEquals(title, dw.getTitle());
    }

    @Test
    public void getStyle() throws Exception {
        String style = "Stacked";
        dw.setStyle(style);
        assertEquals(style, dw.getStyle());
    }

    @Test
    public void getOrientation() throws Exception {
        String orientation = "Vertical";
        dw.setOrientation(orientation);
        assertEquals(orientation, dw.getOrientation());
    }

    @Test
    public void getCategoryField() throws Exception {
        String category = "EventDateTime";
        dw.setCategoryField(category);
        assertEquals(category, dw.getCategoryField());
    }

    @Test
    public void getXPos() throws Exception {
        int pos = 10;
        dw.setXPos(pos);
        assertEquals(pos, dw.getXPos(), 0);
    }

    @Test
    public void getYPos() throws Exception {
        int pos = 10;
        dw.setYPos(pos);
        assertEquals(pos, dw.getYPos(), 0);
    }

    @Test
    public void getWidth() throws Exception {
        int pos = 10;
        dw.setWidth(pos);
        assertEquals(pos, dw.getWidth(), 0);
    }

    @Test
    public void getHeight() throws Exception {
        int pos = 10;
        dw.setHeight(pos);
        assertEquals(pos, dw.getHeight(), 0);
    }

    @Test
    public void getTop() throws Exception {
        int top = 5;
        dw.setTop(top);
        assertEquals(top, dw.getTop(), 0);
    }
}