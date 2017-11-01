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

import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import org.apache.lucene.document.Document;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.TopDocs;

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

    final Map<String, Map<String, List<AwsData>>> multiSeries = new HashMap<>();
    private final Map<String, List<AwsData>> singleSeries = new HashMap<>();

    @Override
    public void clearEvents() {

        categories.clear();
        multiSeries.clear();
        singleSeries.clear();

        super.clearEvents();
    }

    @Override
    public void newEvent(Document documen) {

//        super.newEvent(data);
//
//        String s = EventUtils.getEventProperty(this.widget.getSeriesField(), data);
//
//        if (s != null) {
//
//            List<AwsData> events;
//            if (singleSeries.containsKey(s)) {
//                events = singleSeries.get(s);
//            } else {
//                events = new ArrayList<>();
//                singleSeries.put(s, events);
//            }
//
//            events.add(data);
//        }
    }

    public void finishedLoading(boolean reload) {

        if (reload) {
            chart.getData().clear();
        }

        xAxis.setLabel(widget.getSeriesField());
        yAxis.setLabel("Count");

        try {
            TermStats[] top = dataService.getTop(widget.getTop(), widget.getSeriesField());
            if (top != null) {

                categories.clear();

                for (TermStats stat : top) {

                    String seriesName = stat.termtext.utf8ToString();
                    categories.add(seriesName);

                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(seriesName);
                    series.getData().add(new XYChart.Data<>(seriesName, stat.docFreq));

                    chart.getData().add(series);

                }

                for (XYChart.Series<String,Number> serie: chart.getData()){
                    for (XYChart.Data<String, Number> item: serie.getData()){
                        item.getNode().setOnMousePressed((MouseEvent event) -> {

                            try {
                                TopDocs result = LuceneUtils.performQuery(widget.luceneDir(), widget.getSeriesField(), serie.getName());
                                eventTableService.setTableEvents(result, widget.getType());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        Node node = item.getNode();
                        Tooltip t = new Tooltip(serie.getName() + " : " + item.getYValue());
                        Tooltip.install(node, t);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        xAxis.setCategories(FXCollections.observableArrayList(categories));
    }

}
