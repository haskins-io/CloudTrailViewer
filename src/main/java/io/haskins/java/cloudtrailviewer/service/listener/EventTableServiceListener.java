package io.haskins.java.cloudtrailviewer.service.listener;

import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
public interface EventTableServiceListener {

    void setEvents(List<Event> events);
}
