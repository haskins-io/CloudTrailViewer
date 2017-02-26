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

import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class that provides common XY Chart functionality
 *
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

        List<Map.Entry<String, Integer>> topEvents = getTopEvents();
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
                    item.getNode().setOnMousePressed((MouseEvent event) -> eventTableService.setTableEvents(singleSeries.get(serie.getName())));

                    Node node = item.getNode();
                    Tooltip t = new Tooltip(serie.getName() + " : " + item.getYValue());
                    Tooltip.install(node, t);
                }
            }
        }

        xAxis.setCategories(FXCollections.observableArrayList(categories));
    }

}
