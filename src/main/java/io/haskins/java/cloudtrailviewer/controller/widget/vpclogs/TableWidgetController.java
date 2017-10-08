package io.haskins.java.cloudtrailviewer.controller.widget.vpclogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.model.flowlog.VpcFlowLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class TableWidgetController extends AbstractBaseController {

    @FXML
    private TableView tableView;

    ObservableList<VpcFlowLog> data = FXCollections.observableArrayList();

    BorderPane fxmlObject = null;

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
    public void newEvents(List events) {

        tableView.getItems().clear();

        for (Object log : events) {
            data.add((VpcFlowLog) log);
        }

    }

    @Override
    public void loadingFile(int fileNum, int totalFiles) { }

    @Override
    public void finishedLoading(boolean reload) { }

    public void setLogs(List<VpcFlowLog> logs) {


    }
}
