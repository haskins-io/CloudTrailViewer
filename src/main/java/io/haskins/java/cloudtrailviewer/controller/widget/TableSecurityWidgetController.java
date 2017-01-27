package io.haskins.java.cloudtrailviewer.controller.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.dao.ResultSetRow;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
public class TableSecurityWidgetController extends TableWidgetController {

    private final List<String> securityEvents = new ArrayList<>();

    @Override
    public void newEvents(List<Event> events) {

        for (Event event : events) {
            if (securityEvents.contains(event.getEventName())) {
                newEvent(event);
            }
        }
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        loadSecurityEvents();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void loadSecurityEvents() {

        String query = "SELECT api_call FROM aws_security";
        List<ResultSetRow> rows = databaseService.executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            String aws_name = (String)row.get("api_call");
            securityEvents.add(aws_name);
        }
    }
}
