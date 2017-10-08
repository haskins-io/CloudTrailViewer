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

package io.haskins.java.cloudtrailviewer.controller.widget;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.components.WidgetControlsController;
import io.haskins.java.cloudtrailviewer.controller.components.WidgetControlsControllerListener;
import io.haskins.java.cloudtrailviewer.controller.widget.cloudtrail.WidgetListener;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.observable.KeyIntegerValue;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.utils.DragResizeWidget;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import io.haskins.java.cloudtrailviewer.utils.OnDragResizeEventListener;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that all Widget controllers should extend.
 *
 * Created by markhaskins on 13/01/2017.
 */
public abstract class AbstractBaseController
        extends BorderPane
        implements DataServiceListener, OnDragResizeEventListener, WidgetControlsControllerListener {

    protected final static Logger LOGGER = Logger.getLogger("CloudTrail");

    public final static String WIDGET_TYPE_TOP = "Top";
    public final static String WIDGET_TYPE_ALL = "All";

    private final List<Event> allEvents = new ArrayList<>();

    protected final Map<String, List<Event>> keyValueMap = new HashMap<>();
    protected final ObservableList<KeyIntegerValue> keyValueData = FXCollections.observableArrayList();
    protected final Map<String, String> latlngs = new HashMap<>();

    protected DashboardWidget widget;

    protected EventTableService eventTableService;
    protected DatabaseService databaseService;

    protected BorderPane fxmlObject = null;

    private final List<WidgetListener> listeners = new ArrayList<>();

    @FXML private BorderPane widgetContainer;

    @FXML protected WidgetControlsController widgetControlsController;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// abstract methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public abstract BorderPane loadFXML();

    protected abstract FontAwesomeIconView getWidgetIcon();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {

        this.widget = widget;
        this.widget.setIcon(getWidgetIcon());

        this.eventTableService = eventTableService;
        this.databaseService = databaseService;

        widgetControlsController.init(this, widget);

        widgetContainer.setLayoutX(widget.getXPos());
        widgetContainer.setLayoutY(widget.getYPos());
        widgetContainer.setPrefWidth(widget.getWidth());
        widgetContainer.setPrefHeight(widget.getHeight());

        DragResizeWidget.makeResizable(widgetContainer, this);
    }

    public DashboardWidget getWidget() {
        return this.widget;
    }

    public BorderPane getFXMLObject() {
        return this.fxmlObject;
    }

    public void addWidgetListener(WidgetListener l) {
        listeners.add(l);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// protected methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected List<Map.Entry<String,Integer>> getTopEvents() {

        Map<String, Integer> eventsByOccurance = new HashMap<>();

        for (Map.Entry<String, List<Event>> entry : keyValueMap.entrySet()) {
            eventsByOccurance.put(entry.getKey(), entry.getValue().size());
        }

        if (!eventsByOccurance.isEmpty()) {

            List<Map.Entry<String,Integer>> sorted = entriesSortedByValues(eventsByOccurance);
            if (widget.getChartType().equalsIgnoreCase(WIDGET_TYPE_TOP) && widget.getTop() > 0) {
                return getTopX(sorted, widget.getTop());
            } else {
                return sorted;
            }

        } else {
            return new ArrayList<>();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addToKeyValueMap(Event event) {

        try {
            String propertyValue = EventUtils.getEventProperty(this.widget.getSeriesField(), event);

            if (propertyValue != null) {

                List<Event> events = keyValueMap.get(propertyValue);
                if (events == null) {
                    events = new ArrayList<>();
                }

                events.add(event);
                keyValueMap.put(propertyValue, events);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception caught trying to add Event to KeyValueMap");
        }

    }

    private List<Map.Entry<String,Integer>> getTopX(List<Map.Entry<String,Integer>> sorted, int top) {

        List<Map.Entry<String,Integer>> topEvents = new ArrayList<>();

        int count = top;
        if (sorted.size() < count) {
            count = sorted.size();
        }

        for (int i=0; i<count; i++) {
            topEvents.add(sorted.get(i));
        }

        return topEvents;
    }

    private <K,V extends Comparable<? super V>>  List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());

        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// DataServiceListener methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newEvent(AwsData data) {

        Event event = (Event)data;

        allEvents.add(event);

        try {
            String latLng = event.getLatLng();
            if (latLng != null && latLng.length() > 0 && !latlngs.containsKey(latLng)) {

                String propertyValue = EventUtils.getEventProperty(this.widget.getSeriesField(), event);
                latlngs.put(latLng, propertyValue);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception resolving Geo Data");
        }

        addToKeyValueMap(event);
    }

    public abstract void finishedLoading(boolean reload);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// DataServiceListener methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDrag(double x, double y) {

        widgetContainer.setLayoutX(x);
        widgetContainer.setLayoutY(y);

        widget.setXPos(x);
        widget.setYPos(y);
    }

    @Override
    public void onResize(double h, double w) {

        widgetContainer.setPrefWidth(w);
        widgetContainer.setPrefHeight(h);

        widget.setWidth(w);
        widget.setHeight(h);
    }

    @Override
    public void clearEvents() {
        allEvents.clear();

        keyValueMap.clear();
        keyValueData.clear();
        latlngs.clear();

        finishedLoading(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// FXML methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void removeWidget() {
        for (WidgetListener l : listeners) {
            l.removeWidget(this);
        }
    }

    public void editWidget() {

        DialogAction action = WidgetUtils.showWidgetDialog(widget, true);

        if (action.getActionCode() != DialogAction.ACTION_CANCEL) {

            widgetControlsController.setTitle(widget.getTitle());

            keyValueMap.clear();
            keyValueData.clear();

            for (Event event : allEvents) {
                addToKeyValueMap(event);
            }

            finishedLoading(true);
        }
    }
}
