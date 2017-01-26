package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for a Pie Chart.
 *
 * Created by markhaskins on 10/01/2017.
 */
public class PieChartWidgetController extends AbstractBaseController {

    @FXML private PieChart pieChart;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BorderPane loadFXML() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/PieChartWidget.fxml"));
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

        pieChart.setPrefWidth(widget.getWidth());
        pieChart.setPrefHeight(widget.getHeight());
    }

    public void newEvents(List<Event> events) {

        for (Event event : events) {
            newEvent(event);
        }
    }

    public void loadingFile(int fileName, int totalFiles) { }

    public void finishedLoading(boolean reload) {

        ObservableList<PieChart.Data> data;
        if (reload) {
            pieChart.getData().clear();
            data = pieChart.getData();
        } else {
            data = pieChart.getData();
        }

        List<PieChart.Data> items = new ArrayList<>();

        List<Map.Entry<String, Integer>> topEvents = getTopEvents(widget.getTop());
        if (topEvents != null) {
            for (Map.Entry<String, Integer> entry : topEvents) {

                PieChart.Data pcd = new PieChart.Data(entry.getKey(), entry.getValue());
                items.add(pcd);
            }

            data.addAll(items);
        }

        for (PieChart.Data item: pieChart.getData()){
            item.getNode().setOnMousePressed((MouseEvent event) -> {
                eventTableService.setTableEvents(keyValueMap.get(item.getName()));
            });
        }
    }
}
