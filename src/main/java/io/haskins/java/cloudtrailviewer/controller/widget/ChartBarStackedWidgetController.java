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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DataService;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Controller that provides a Stacked Bar Chart widget
 *
 * Created by markhaskins on 10/01/2017.
 */
public class ChartBarStackedWidgetController extends XYChartController {

//    @FXML private StackedBarChart chart;

    public BorderPane loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/ChartBarStackedWidget.fxml"));
        loader.setController(this);
        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    protected FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART_ALT);
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DataService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        chart.setPrefWidth(widget.getWidth());
        chart.setPrefHeight(widget.getHeight());

        chart.getXAxis().setTickLabelsVisible(false);
    }

    public void newEvents(List<? extends AwsData> events) {

        for (AwsData event : events) {
            newEvent(event);
        }
    }

    @Override
    public void newEvent(AwsData data) {

        String c = EventUtils.getEventProperty(this.widget.getCategoryField(), data);
        if (!categories.contains(c)) {
            categories.add(c);
        }

        String s = EventUtils.getEventProperty(this.widget.getSeriesField(), data);

        Map<String, List<AwsData>> catMap;
        if (multiSeries.containsKey(s)) {
            catMap = multiSeries.get(s);
        } else {
            catMap = new HashMap<>();
            multiSeries.put(s, catMap);
        }

        List<AwsData> evList;
        if (catMap.containsKey(c)) {
            evList = catMap.get(c);
        } else {
            evList = new ArrayList<>();
            catMap.put(c, evList);
        }

        evList.add(data);
    }

    public void loadingFile(int fileName, int totalFiles) { }

    public void finishedLoading(boolean reload) {

        if (reload) {
            chart.getData().clear();
        }

        xAxis.setLabel(widget.getSeriesField());
        xAxis.setCategories(FXCollections.observableArrayList(categories));

        yAxis.setLabel("Count");

        for (Map.Entry<String, Map<String, List<AwsData>>> entry : multiSeries.entrySet()) {

            String seriesName = entry.getKey();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(seriesName);

            Map<String, List<AwsData>> catData = entry.getValue();

            for (Map.Entry<String, List<AwsData>> data : catData.entrySet()) {

                String catName = data.getKey();
                List<AwsData> events = data.getValue();

                series.getData().add(new XYChart.Data<>(catName, events.size()));

            }

            chart.getData().add(series);
        }

        for (XYChart.Series<String,Number> series: chart.getData()){
            for (XYChart.Data<String, Number> item: series.getData()){

                String seriesName = series.getName();
                Map<String, List<AwsData>> catData = multiSeries.get(seriesName);

                for (Map.Entry<String, List<AwsData>> catData2 : catData.entrySet()) {

                    List<AwsData> events = catData2.getValue();
                    item.getNode().setOnMousePressed((MouseEvent event) -> eventTableService.setTableEvents(events));

                    Node node = item.getNode();
                    Tooltip t = new Tooltip(seriesName + " : " + item.getYValue());
                    Tooltip.install(node, t);

                }
            }
        }
    }

}
