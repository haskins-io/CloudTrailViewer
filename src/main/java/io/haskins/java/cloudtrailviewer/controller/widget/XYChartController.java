package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by markhaskins on 25/01/2017.
 */
abstract class XYChartController extends AbstractBaseController {

    @FXML XYChart<String,Number> chart;
    @FXML CategoryAxis xAxis;
    @FXML NumberAxis yAxis;

    final List<String> categories = new ArrayList<>();

    final Map<String, Map<String, List<Event>>> multiSeries = new HashMap<>();
    private final Map<String, List<Event>> singleSeries = new HashMap<>();

    @Override
    public void clearEvents() {

        categories.clear();
        multiSeries.clear();
        singleSeries.clear();

        super.clearEvents();
    }

    public void newEvents(List<Event> events) {

        for (Event event : events) {
            newEvent(event);
        }
    }

    @Override
    public void newEvent(Event event) {

        super.newEvent(event);

        String s = EventUtils.getEventProperty(this.widget.getSeriesField(), event);

        if (s != null) {

            List<Event> events;
            if (singleSeries.containsKey(s)) {
                events = singleSeries.get(s);
            } else {
                events = new ArrayList<>();
                singleSeries.put(s, events);
            }

            events.add(event);
        }
    }

    public void finishedLoading(boolean reload) {

        if (reload) {
            chart.getData().clear();
        }

        xAxis.setLabel(widget.getSeriesField());
        yAxis.setLabel("Count");

        List<Map.Entry<String, Integer>> topEvents = getTopEvents(widget.getTop());
        if (topEvents != null) {

            categories.clear();

            for (Map.Entry<String, Integer> entry : topEvents) {

                String seriesName = entry.getKey();
                categories.add(seriesName);

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(seriesName);
                series.getData().add(new XYChart.Data<>(seriesName, entry.getValue()));

                chart.getData().add(series);

            }

            for (XYChart.Series<String,Number> serie: chart.getData()){
                for (XYChart.Data<String, Number> item: serie.getData()){
                    item.getNode().setOnMousePressed((MouseEvent event) -> {
                        eventTableService.setTableEvents(singleSeries.get(serie.getName()));
                    });
                }
            }
        }

        xAxis.setCategories(FXCollections.observableArrayList(categories));
    }

}
