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

import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Component that provides the Status Bar at the bottom of the application Window.
 *
 * Created by Mark on 19/01/17.
 */
@Component
public class StatusBarController implements DataServiceListener {

    @FXML public Label message;
    @FXML private Label loadedEvents;
    @FXML private Label fromDate;
    @FXML private Label toDate;

    private long earliestEventLong = -1;
    private String earliestEventString;

    private long latestEventLong = -1;
    private String latestEventString;

    private long numEventsLoaded;

    @FXML
    public void initialize() {
        loadedEvents.setText("Loaded Events : 0");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// DataServiceListener methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvent(AwsData data) {

        Event event = (Event)data;

        if (earliestEventLong == -1 || event.getTimestamp() < earliestEventLong) {
            earliestEventLong = event.getTimestamp();
            earliestEventString = event.getEventTime();
        }

        if (latestEventLong == -1 || event.getTimestamp() > latestEventLong) {
            latestEventLong = event.getTimestamp();
            latestEventString = event.getEventTime();
        }

        numEventsLoaded++;


        Platform.runLater(new Runnable() {
            @Override public void run() {
                loadedEvents.setText(String.valueOf("Loaded Events : " + numEventsLoaded));
            }
        });

    }

    @Override
    public void newEvents(List<? extends AwsData> events) {

        earliestEventLong = -1;
        latestEventLong = -1;

        for (AwsData event : events) {
            newEvent(event);
        }
    }

    @Override
    public void loadingFile(int fileNum, int totalFiles) { }

    @Override
    public void finishedLoading(boolean reload) {

        fromDate.setText(earliestEventString);
        toDate.setText(latestEventString);

        loadedEvents.setText(String.valueOf("Loaded Events : " + numEventsLoaded));
    }

    @Override
    public void clearEvents() {
        numEventsLoaded = 0;
        message.setVisible(false);
        fromDate.setVisible(false);
        toDate.setVisible(false);
    }

}
