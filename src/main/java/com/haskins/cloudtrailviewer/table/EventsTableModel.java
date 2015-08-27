/*    
 CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
 files.

 Copyright (C) 2015  Mark P. Haskins

 This program is free software: you can redistribute it and/or modify it under the
 terms of the GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,but WITHOUT ANY 
 WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 PARTICULAR PURPOSE.  See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.table;

import com.haskins.cloudtrailviewer.utils.TableUtils;
import com.haskins.cloudtrailviewer.core.EventDatabase;
import com.haskins.cloudtrailviewer.table.resource.ResourceLookup;
import com.haskins.cloudtrailviewer.model.event.Event;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mark.haskins
 */
public class EventsTableModel extends AbstractTableModel {

    public static final String[] COLUMN_NAMES = new String[]{
        "Date/Time",
        "User Name",
        "Service",
        "Name",
        "Resource Type",
        "Resource Name",
        "Event Version",
        "AWS Region",
        "Source IP Address",
        "User Agent",
        "Event Source",
        "Error Code",
        "Error Message",
        "Request Id",
        "Event Id",
        "Event Type",
        "API Version"
    };

    private final EventDatabase eventsDb;

    public EventsTableModel(EventDatabase eventsDatabase) {
        eventsDb = eventsDatabase;
    }

    public void reloadTableModel() {
        fireTableDataChanged();
    }

    public Event getEventAt(int i) {
        return eventsDb.getEventByIndex(i);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AbstractTableModel implementation
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String getColumnName(int index) {
        return COLUMN_NAMES[index];
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public int getRowCount() {

        int i = 0;

        if (eventsDb != null) {
            i = eventsDb.size();
        }

        return i;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Event event = getEventAt(rowIndex);

        Object value = null;

        switch (columnIndex) {

            case 0:
                value = TableUtils.getFormatedDateTime(event.getTimestamp());
                break;
            case 1:
                value = TableUtils.getInvokedBy(event);
                break;
            case 2:
                value = TableUtils.getService(event);
                break;
            case 3:
                value = event.getEventName();
                break;
            case 4:
                value = ResourceLookup.getResourceInfo(event).getTypes();
                break;
            case 5:
                value = ResourceLookup.getResourceInfo(event).getNames();
                break;
            case 6:
                value = event.getEventVersion();
                break;
            case 7:
                value = event.getAwsRegion();
                break;
            case 8:
                value = event.getSourceIPAddress();
                break;
            case 9:
                value = event.getEventSource();
                break;
            case 10:
                value = event.getUserAgent();
                break;
            case 11:
                value = event.getErrorCode();
                break;
            case 12:
                value = event.getErrorMessage();
                break;
             case 13:
                value = event.getRequestId();
                break;
            case 14:
                value = event.getEventId();
                break;
            case 15:
                value = event.getEventType();
                break;
            case 16:
                value = event.getApiVersion();
                break;
        }

        return value;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
