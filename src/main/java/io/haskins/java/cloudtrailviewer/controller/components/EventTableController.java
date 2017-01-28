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

import io.haskins.java.cloudtrailviewer.model.observable.EventTableModel;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.EventServiceListener;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
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

    @FXML private TableView tableView;


    @Autowired
    public EventTableController(EventService eventService, EventTableService eventTableService) {
        eventTableService.addListener(this);
        eventService.registerAsListener(this);
    }

    /**
     * Updates the table with the provided events.
     * @param events a List of Events
     */
    public void setEvents(List<Event> events) {

        if (events != null && !events.isEmpty()) {
            tableView.getItems().clear();

            ObservableList<EventTableModel> data = tableView.getItems();

            List<EventTableModel> items = new ArrayList<>();

            for (Event event : events) {
                items.add(new EventTableModel(event));
            }

            data.addAll(items);
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
        tableView.getItems().clear();
    }
}
