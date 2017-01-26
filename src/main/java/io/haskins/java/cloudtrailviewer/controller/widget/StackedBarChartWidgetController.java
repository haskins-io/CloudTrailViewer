package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by markhaskins on 10/01/2017.
 */
public class StackedBarChartWidgetController extends XYChartController {

    @FXML private StackedBarChart chart;

    public BorderPane loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/StackedBarChartWidget.fxml"));
        loader.setController(this);
        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService) {

        super.configure(widget, eventTableService);

        chart.setPrefWidth(widget.getWidth());
        chart.setPrefHeight(widget.getHeight());

        chart.getXAxis().setTickLabelsVisible(false);
    }

    public void newEvents(List<Event> events) {

        for (Event event : events) {
            newEvent(event);
        }
    }

    @Override
    public void newEvent(Event event) {

        String c = EventUtils.getEventProperty(this.widget.getCategoryField(), event);
        if (!categories.contains(c)) {
            categories.add(c);
        }

        String s = EventUtils.getEventProperty(this.widget.getSeriesField(), event);

        Map<String, List<Event>> catMap;
        if (multiSeries.containsKey(s)) {
            catMap = multiSeries.get(s);
        } else {
            catMap = new HashMap<>();
            multiSeries.put(s, catMap);
        }

        List<Event> evList;
        if (catMap.containsKey(c)) {
            evList = catMap.get(c);
        } else {
            evList = new ArrayList<>();
            catMap.put(c, evList);
        }

        evList.add(event);
    }

    public void loadingFile(int fileName, int totalFiles) { }

    public void finishedLoading(boolean reload) {

        if (reload) {
            chart.getData().clear();
        }

        xAxis.setLabel(widget.getSeriesField());
        xAxis.setCategories(FXCollections.observableArrayList(categories));

        yAxis.setLabel("Count");

        for (Map.Entry<String, Map<String, List<Event>>> entry : multiSeries.entrySet()) {

            String seriesName = entry.getKey();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(seriesName);

            Map<String, List<Event>> catData = entry.getValue();

            for (Map.Entry<String, List<Event>> data : catData.entrySet()) {

                String catName = data.getKey();
                List<Event> events = data.getValue();

                series.getData().add(new XYChart.Data<>(catName, events.size()));
            }

            chart.getData().add(series);
        }
    }

}
