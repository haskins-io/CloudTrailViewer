package io.haskins.java.cloudtrailviewer.service.listener;

import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.util.List;

/**
 * Created by markhaskins on 04/01/2017.
 */
public interface EventServiceListener {

    void newEvent(Event event);
    void newEvents(List<Event> events);

    void loadingFile(int fileName, int totalFiles);
    void finishedLoading(boolean reload);

    void clearEvents();
}
