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
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.service.DataService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/ChartPieWidget.fxml"));
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
    public void configure(DashboardWidget widget, EventTableService eventTableService, DataService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        pieChart.setPrefWidth(widget.getWidth());
        pieChart.setPrefHeight(widget.getHeight());
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

        try {
            List<PieChart.Data> items = new ArrayList<>();

            TermStats[] top = dataService.getTop(widget.getTop(), widget.getSeriesField());
            for (TermStats stat : top) {

                PieChart.Data pcd = new PieChart.Data(stat.termtext.utf8ToString(), stat.docFreq);
                items.add(pcd);
            }

            data.addAll(items);

            for (PieChart.Data item: pieChart.getData()){

                item.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                    try {


                        TopDocs result = LuceneUtils.performQuery(this.widget.getType(), widget.getSeriesField(), item.getName());
                        eventTableService.setTableEvents(result, widget.getType());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

                Node node = item.getNode();
                Tooltip t = new Tooltip(item.getName() + " : " + item.getPieValue());
                Tooltip.install(node, t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newEvent(org.apache.lucene.document.Document document) { }
}

