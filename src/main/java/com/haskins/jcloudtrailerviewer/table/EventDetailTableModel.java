/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.table;

import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.resource.ResourceLookup;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mark.haskins
 */
public class EventDetailTableModel extends AbstractTableModel {

    private Event detailEvent = null;

    public void showDetail(Event event) {

        detailEvent = event;
        fireTableRowsInserted(1, 2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AbstractTableModel implementation
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        int retVal = 0;

        if (detailEvent != null) {
            retVal = 11;
        }

        return retVal;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        Object value = "";

        switch (rowIndex) {

            case 0:
                value = "Event Time";
                if (columnIndex == 1) {
                    value = detailEvent.getEventTime();
                }
                break;
            case 1:
                value = "User Identity";
                if (columnIndex == 1) {

                    value = TableUtils.getUserName(detailEvent);
                }
                break;
            case 2:
                value = "Event Name";
                if (columnIndex == 1) {
                    value = detailEvent.getEventName();
                }
                break;
            case 3:
                value = "Source IP";
                if (columnIndex == 1) {
                    value = detailEvent.getSourceIPAddress();
                }
                break;
            case 4:
                value = "Resource Type";
                if (columnIndex == 1) {
                    value = ResourceLookup.getResourceInfo(detailEvent).getTypes();
                }
                break;
            case 5:
                value = "Resource Name";
                if (columnIndex == 1) {
                    value = ResourceLookup.getResourceInfo(detailEvent).getNames();
                }
                break;
            case 6:
                value = "Error Code";
                if (columnIndex == 1) {
                    value = detailEvent.getErrorCode();
                }
                break;
            case 7:
                value = "AWS Region";
                if (columnIndex == 1) {
                    value = detailEvent.getAwsRegion();
                }
                break;
            case 8:
                value = "Event Id";
                if (columnIndex == 1) {
                    value = detailEvent.getEventId();
                }
                break;
            case 9:
                value = "Request Id";
                if (columnIndex == 1) {
                    value = detailEvent.getRequestId();
                }
                break;
            case 10:
                value = "Event Source";
                if (columnIndex == 1) {
                    value = detailEvent.getEventSource();
                }
                break;
            case 11:
                value = "AWS Access Key";
                if (columnIndex == 1) {
                    value = detailEvent.getUserIdentity().getAccessKeyId();
                }
                break;
        }

        return value;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private final static String[] columnNames = new String[]{
        "Property", "Value"
    };
}
