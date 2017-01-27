package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;

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

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        editButton.setVisible(false);
    }
}
