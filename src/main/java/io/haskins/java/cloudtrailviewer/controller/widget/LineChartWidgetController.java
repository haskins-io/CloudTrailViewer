package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by markhaskins on 26/01/2017.
 */
public class LineChartWidgetController extends XYChartController {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BorderPane loadFXML() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/LineChartWidget.fxml"));
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

        xAxis.setTickLabelsVisible(false);
    }

    public void loadingFile(int fileName, int totalFiles) { }
}
