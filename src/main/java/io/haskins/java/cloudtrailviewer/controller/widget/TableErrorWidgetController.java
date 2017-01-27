package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.event.Event;

import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
public class TableErrorWidgetController extends TableWidgetController {

    @Override
    public void newEvents(List<Event> events) {
        for (Event event : events) {

            String errorName = event.getErrorCode();
            if (errorName.trim().length() > 0) {
                newEvent(event);
            }
        }
    }
}
