package io.haskins.java.cloudtrailviewer.controller.components.vpclogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VpcLogEventTableController implements EventTableServiceListener, DataServiceListener {

    @FXML private TableView tableView;

    @FXML private Label searchLabel;
    @FXML private TextField searchField;
    @FXML private Label resultCount;

    private ObservableList<AwsData> filteredEvents = FXCollections.observableArrayList();

    private EventService eventService;

    @Autowired
    public VpcLogEventTableController(EventService eventService, EventTableService eventTableService) {

        this.eventService = eventService;

        eventTableService.addListener(this);
        eventService.registerAsListener(this);
    }

    @FXML
    public void initialize() {

        tableView.setItems(filteredEvents);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FilteredList<AwsData> filteredData = new FilteredList<>(filteredEvents, p -> true);

        SortedList<AwsData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

        searchLabel.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        resultCount.textProperty().bind(Bindings.size(filteredData).asString());
    }

    /**
     * Updates the table with the provided events.
     * @param events a List of Events
     */
    public void setEvents(List<AwsData> events) {

        AwsData d = events.get(0);

        if (d instanceof VpcFlowLog) {

            filteredEvents.clear();
            filteredEvents.addAll(events);
        }
    }

    public void scannedEvent() { }

    @Override
    public void newEvent(AwsData event) {

    }

    @Override
    public void newEvents(List<? extends AwsData> events) {

    }

    @Override
    public void loadingFile(int fileNum, int totalFiles) {

    }

    @Override
    public void finishedLoading(boolean reload) {

    }

    @Override
    public void clearEvents() {

    }

}
