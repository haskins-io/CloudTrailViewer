package io.haskins.java.cloudtrailviewer.controller.widget.vpclogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

/**
 * Controller for a Vpc Flow Log Table.
 *
 * Created by markhaskins on 10/10/2017.
 */
public class FlowLogTableWidgetController extends AbstractBaseController {

    @FXML private TableView tableView;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BorderPane loadFXML() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/vpclogs/TableWidget.fxml"));
        loader.setController(this);

        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    protected FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.TABLE);
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {
        super.configure(widget, eventTableService, databaseService);

        tableView.setPrefHeight(widget.getHeight());
        tableView.setPrefWidth(widget.getWidth());

        widgetControlsController.hideEditButton();
    }

    public void newEvents(List<? extends AwsData> data) {

        for (AwsData d : data) {
            newEvent(d);
        }

    }

    public void loadingFile(int fileNum, int totalFiles) { }

    public void finishedLoading(boolean reload) {

        tableView.getItems().clear();

        ObservableList<VpcFlowLog> data = tableView.getItems();

        for (Object log : getAllData()) {


            data.add((VpcFlowLog) log);
        }

    }

    public void setLogs(List<VpcFlowLog> logs) {
        System.out.println("Setting Logs");
    }
}
