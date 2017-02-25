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

package io.haskins.java.cloudtrailviewer.controller.components;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.EventServiceListener;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * This component provides the TableView location at the bottom of the application.
 *
 * Created by markhaskins on 26/01/2017.
 */
@Component
public class EventTableController implements EventTableServiceListener, EventServiceListener {

    @FXML private TableView<Event> tableView;

    @FXML private Label searchLabel;
    @FXML private TextField searchField;
    @FXML private Label resultCount;

    private List<Event> allEvents = new ArrayList<>();
    private ObservableList<Event> filteredEvents = FXCollections.observableArrayList();

    private DashboardService dashboardService;

    @Autowired
    public EventTableController(EventService eventService, EventTableService eventTableService, DashboardService dashboardService) {

        eventTableService.addListener(this);
        eventService.registerAsListener(this);

        this.dashboardService = dashboardService;
    }

    @FXML private void resetSearch() {
        filteredEvents.clear();
        filteredEvents.addAll(allEvents);
    }

    public void initialize() {

        tableView.setRowFactory(tv -> {

            TableRow<Event> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if (event.getClickCount() ==2 && !row.isEmpty()) {

                    Event rowItem = row.getItem();

                    DashboardWidget widget = new DashboardWidget();
                    widget.setWidget("Json");
                    widget.setTitle("Event JSON");
                    widget.setWidth(425);
                    widget.setHeight(600);
                    widget.setPayload(rowItem);

                    dashboardService.addWidgetToDashboard(widget);
                }
            });

            return row;
        });

        tableView.setItems(filteredEvents);

        FilteredList<Event> filteredData = new FilteredList<>(filteredEvents, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            filteredData.setPredicate(event -> {
                if (newValue== null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (event.getRawJSON() == null) {
                    EventUtils.addRawJson(event);
                }

                if (event.getRawJSON().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Event> sortedData = new SortedList<Event>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

        searchLabel.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SEARCH));
        resultCount.textProperty().bind(Bindings.size(filteredData).asString());

    }
    /**
     * Updates the table with the provided events.
     * @param events a List of Events
     */
    public void setEvents(List<Event> events) {

        if (events != null && !events.isEmpty()) {

            allEvents.clear();
            filteredEvents.clear();

            allEvents.addAll(events);
            filteredEvents.addAll(events);
        }
    }

    @Override
    public void newEvent(Event event) {

    }

    @Override
    public void newEvents(List<Event> events) {

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

}
