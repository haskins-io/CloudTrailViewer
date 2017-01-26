package io.haskins.java.cloudtrailviewer.controller.components;

import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.listener.EventServiceListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
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

    @Override
    public void newEvent(Event event) {

        if (earliestEvent == -1 || event.getTimestamp() < earliestEvent) {
            earliestEvent = event.getTimestamp();
            this.fromDate.setText(event.getEventTime());
        }

        if (latestEvent == -1 || event.getTimestamp() > latestEvent) {
            latestEvent = event.getTimestamp();
            this.toDate.setText(event.getEventTime());
        }

        numEventsLoaded++;

        loadedEvents.setVisible(true);
        loadedEvents.setText("Events Loaded : " + numEventsLoaded);
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
        message.setText("Processing file " + fileNum + " of " + totalFiles);
    }

    @Override
    public void finishedLoading(boolean reload) {
        message.setText("");
    }

    @Override
    public void clearEvents() {
        numEventsLoaded = 0;
        message.setVisible(false);
        fromDate.setVisible(false);
        toDate.setVisible(false);
        loadedEvents.setVisible(false);
    }

    private void setVisibleEvents(int eventCount) {

        loadedEvents.setVisible(true);
        loadedEvents.setText("Current Events : " + eventCount);
    }

}
