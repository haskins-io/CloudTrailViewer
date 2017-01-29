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

import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.listener.EventServiceListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component that provides the Status Bar at the bottom of the application Window.
 *
 * Created by Mark on 19/01/17.
 */
@Component
public class StatusBarController implements EventServiceListener {

    @FXML private Label message;
    @FXML private Label loadedEvents;
    @FXML private Label fromDate;
    @FXML private Label toDate;
    @FXML private Label memoryUsage;

    private long earliestEvent;
    private long latestEvent;

    private long numEventsLoaded;

    @Autowired
    public StatusBarController(EventService eventService) {
        eventService.registerAsListener(this);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// EventServiceListener methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvent(Event event) {

        if (earliestEvent == -1 || event.getTimestamp() < earliestEvent) {
            earliestEvent = event.getTimestamp();

            updateLabel(fromDate, event.getEventTime());
        }

        if (latestEvent == -1 || event.getTimestamp() > latestEvent) {
            latestEvent = event.getTimestamp();

            updateLabel(toDate, event.getEventTime());
        }

        numEventsLoaded++;

        loadedEvents.setVisible(true);
        updateLabel(loadedEvents, "Events Loaded : " + numEventsLoaded);
    }

    @Override
    public void newEvents(List<Event> events) {

        earliestEvent = -1;
        latestEvent = -1;

        setVisibleEvents(events.size());

        for (Event event : events) {
            newEvent(event);
        }
    }

    @Override
    public void loadingFile(int fileNum, int totalFiles) {
        updateLabel(message, "Processing file " + fileNum + " of " + totalFiles);
    }

    @Override
    public void finishedLoading(boolean reload) {
        updateLabel(message, "");
    }

    @Override
    public void clearEvents() {
        numEventsLoaded = 0;
        message.setVisible(false);
        fromDate.setVisible(false);
        toDate.setVisible(false);
        loadedEvents.setVisible(false);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setVisibleEvents(int eventCount) {

        loadedEvents.setVisible(true);
        loadedEvents.setText("Current Events : " + eventCount);
    }

    private void updateLabel(Label l, String value) {
        Platform.runLater(() -> l.setText(value));
    }

}
