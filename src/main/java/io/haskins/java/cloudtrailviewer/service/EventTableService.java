package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
@Service
public class EventTableService {

    private List<EventTableServiceListener> listeners = new ArrayList<>();

    public void addListener(EventTableServiceListener l) {
        listeners.add(l);
    }

    public void setTableEvents(List<Event> events) {

        for (EventTableServiceListener l : listeners) {
            l.setEvents(events);
        }
    }
}
