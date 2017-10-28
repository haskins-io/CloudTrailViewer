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

package io.haskins.java.cloudtrailviewer.controller.widget;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DataService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Controller that provides a widget that shows Events that the user has defined as being security concerns.
 *
 * Created by markhaskins on 26/01/2017.
 */
public class TableSecurityWidgetController extends TableWidgetController {

    private final List<String> securityEvents = new ArrayList<>();

    @Override
    public void newEvents(List<? extends AwsData> data) {

        for (AwsData d : data) {

            Event event = (Event)d;

            if (securityEvents.contains(event.getEventName())) {
                newEvent(event);
            }
        }
    }

    protected FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.SHIELD);
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DataService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        widgetControlsController.hideEditButton();

        loadSecurityEvents();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void loadSecurityEvents() {

//        String query = "SELECT api_call FROM aws_security";
//        List<ResultSetRow> rows = databaseService.executeCursorStatement(query);
//        for (ResultSetRow row : rows) {
//            String aws_name = (String)row.get("api_call");
//            securityEvents.add(aws_name);
//        }
    }
}
