package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.KeyIntegerValue;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.EventServiceListener;
import io.haskins.java.cloudtrailviewer.utils.DragResizeWidget;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import io.haskins.java.cloudtrailviewer.utils.FXMLUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.*;
import java.util.logging.Logger;

/**
 * Abstract class that all Widget controllers should extend.
 *
 * Created by markhaskins on 13/01/2017.
 */
public abstract class AbstractBaseController extends BorderPane implements EventServiceListener, DragResizeWidget.OnDragResizeEventListener {

    final static Logger LOGGER = Logger.getLogger("CloudTrail");

    final static String WIDGET_TYPE_TOP = "Top";

    private final List<Event> allEvents = new ArrayList<>();

    final Map<String, List<Event>> keyValueMap = new HashMap<>();
    final ObservableList<KeyIntegerValue> keyValueData = FXCollections.observableArrayList();
    final Map<String, String> latlngs = new HashMap<>();

    DashboardWidget widget;
    EventTableService eventTableService;

    BorderPane fxmlObject = null;

    private final List<WidgetListener> listeners = new ArrayList<>();

    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private Label titleLabel;
    @FXML private BorderPane widgetControlsContainer;
    @FXML private BorderPane widgetContainer;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// abstract methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public abstract BorderPane loadFXML();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void configure(DashboardWidget widget, EventTableService eventTableService) {

        this.widget = widget;
        this.eventTableService = eventTableService;

        this.titleLabel.setText(widget.getTitle());

        widgetControlsContainer.prefWidth(220);
        widgetControlsContainer.prefHeight(65);

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
    List<Map.Entry<String,Integer>> getTopEvents(int top) {

        Map<String, Integer> eventsByOccurance = new HashMap<>();

        for (Map.Entry<String, List<Event>> entry : keyValueMap.entrySet()) {
            eventsByOccurance.put(entry.getKey(), entry.getValue().size());
        }

        if (!eventsByOccurance.isEmpty()) {

            List<Map.Entry<String,Integer>> sorted = entriesSortedByValues(eventsByOccurance);
            return getTopX(sorted, top);

        } else {
            return new ArrayList<>();
        }
    }

    @FXML
    void editWidget() {

        DialogAction action = FXMLUtils.showDialog(widget, true);

        if (action.getActionCode() != DialogAction.ACTION_CANCEL) {

            this.titleLabel.setText(widget.getTitle());

            keyValueMap.clear();
            keyValueData.clear();

            for (Event event : allEvents) {
                addToKeyValueMap(event);
            }

            finishedLoading(true);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addToKeyValueMap(Event event) {

        String propertyValue = EventUtils.getEventProperty(this.widget.getSeriesField(), event);

        if (propertyValue != null) {

            List<Event> events;
            if (!keyValueMap.containsKey(propertyValue)) {

                events = new ArrayList<>();

            } else {

                events = keyValueMap.get(propertyValue);
            }

            events.add(event);
            keyValueMap.put(propertyValue, events);
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

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K,V>>() {
                    @Override
                    public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// EventServiceListener methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void newEvent(Event event) {

        allEvents.add(event);

        String latLng = event.getLatLng();
        if (latLng!= null && !latlngs.containsKey(latLng)) {

            String propertyValue = EventUtils.getEventProperty(this.widget.getSeriesField(), event);
            latlngs.put(latLng, propertyValue);
        }

        if (widget.getType().equalsIgnoreCase(WIDGET_TYPE_TOP)) {
            addToKeyValueMap(event);
        }
    }

    public abstract void finishedLoading(boolean reload);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// EventServiceListener methods
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
    @FXML public void removeWidget() {
        for (WidgetListener l : listeners) {
            l.removeWidget(this);
        }
    }
}
