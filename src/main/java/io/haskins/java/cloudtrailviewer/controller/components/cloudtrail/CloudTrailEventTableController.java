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

package io.haskins.java.cloudtrailviewer.controller.components.cloudtrail;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * This component provides the TableView location at the bottom of the application.
 *
 * Created by markhaskins on 26/01/2017.
 */
@Component
public class CloudTrailEventTableController implements EventTableServiceListener, DataServiceListener {

    @FXML private TableView<AwsData> tableView;

    @FXML private Label searchLabel;
    @FXML private TextField searchField;
    @FXML private Label resultCount;

    @FXML private HBox hboxPopup;
    @FXML private Button save;
    @FXML private Button popupMenu;

    private ContextMenu colPopup = new ContextMenu();

    private ObservableList<AwsData> filteredEvents = FXCollections.observableArrayList();

    private EventService eventService;
    private DashboardService dashboardService;

    @Autowired
    public CloudTrailEventTableController(EventService eventService, EventTableService eventTableService, DashboardService dashboardService) {

        this.eventService = eventService;

        eventTableService.addListener(this);
        eventService.registerAsListener(this);

        this.dashboardService = dashboardService;
    }

    @FXML
    private void showPopupMenu() {
        colPopup.show(popupMenu, Side.LEFT, 0, 0);
    }

    @FXML
    private void save() throws IOException {

        Stage dialogStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Table Data");
        File file = fileChooser.showSaveDialog(dialogStage);
        if (file != null) {

            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            List<AwsData> events = this.tableView.getItems();
            for (AwsData e : events) {

                bw.write(e.toString());
                bw.newLine();
            }

            bw.close();
        }

    }

    @FXML
    public void initialize() {

        tableView.setRowFactory(tv -> {

            TableRow<AwsData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() ==2 && !row.isEmpty()) {

                    Event rowItem = (Event)row.getItem();

                    DashboardWidget widget = new DashboardWidget();
                    widget.setWidget("Json");
                    widget.setTitle("Event JSON");
                    widget.setWidth(425);
                    widget.setHeight(600);
                    widget.setPayload(rowItem);

                    dashboardService.addWidgetToDashboard(widget, this.eventService);
                }
            });

            return row;
        });

        tableView.setItems(filteredEvents);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FilteredList<AwsData> filteredData = new FilteredList<>(filteredEvents, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(event -> {

            if (newValue== null || newValue.isEmpty()) {
                return true;
            }

            Event e = (Event)event;

            String lowerCaseFilter = newValue.toLowerCase();
            if (e.getRawJSON() == null) {
                EventUtils.addRawJson(e);
            }

            return e.getRawJSON().toLowerCase().contains(lowerCaseFilter);
        }));

        SortedList<AwsData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

        searchLabel.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));

        resultCount.textProperty().bind(Bindings.size(filteredData).asString());

        HBox.setHgrow(hboxPopup, Priority.ALWAYS);
        hboxPopup.setAlignment(Pos.CENTER_RIGHT);

        save.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SAVE));
        popupMenu.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COG));

        createPopupMenu();
    }

    /**
     * Updates the table with the provided events.
     * @param events a List of Events
     */
    public void setEvents(List<AwsData> events) {

        AwsData d = events.get(0);

        if (events != null && !events.isEmpty() && d instanceof Event) {

            filteredEvents.clear();
            filteredEvents.addAll(events);
        }
    }

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
        filteredEvents.clear();
    }

    private void createPopupMenu() {

        ObservableList<TableColumn<AwsData,?>> cols = tableView.getColumns();
        for (TableColumn col : cols) {
            String name = col.textProperty().get();

            CheckMenuItem item = new CheckMenuItem(name);
            item.setSelected(col.isVisible());
            item.setOnAction(event -> {
                col.setVisible(!col.isVisible());
                item.setSelected(col.isVisible());
            });

            colPopup.getItems().add(item);
        }
    }
}
