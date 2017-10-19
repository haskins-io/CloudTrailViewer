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

package io.haskins.java.cloudtrailviewer.controller.widget.cloudtrail;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
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
public class ChartPieWidgetController extends AbstractBaseController {

    @FXML private PieChart pieChart;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BorderPane loadFXML() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/cloudtrail/ChartPieWidget.fxml"));
        loader.setController(this);
        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    protected FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.PIE_CHART);
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        pieChart.setPrefWidth(widget.getWidth());
        pieChart.setPrefHeight(widget.getHeight());
    }

    public void newEvents(List<? extends AwsData> events) {

        for (AwsData event : events) {
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

        List<Map.Entry<String, Integer>> topEvents = getTopEvents();
        if (topEvents != null) {
            for (Map.Entry<String, Integer> entry : topEvents) {

                PieChart.Data pcd = new PieChart.Data(entry.getKey(), entry.getValue());
                items.add(pcd);
            }

            data.addAll(items);
        }

        for (PieChart.Data item: pieChart.getData()){

            item.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> eventTableService.setTableEvents(keyValueMap.get(item.getName())));

            Node node = item.getNode();
            Tooltip t = new Tooltip(item.getName() + " : " + item.getPieValue());
            Tooltip.install(node, t);
        }
    }
}

