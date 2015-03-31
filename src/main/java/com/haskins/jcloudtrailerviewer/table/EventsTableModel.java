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
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class EventsTableModel extends DefaultTableModel {

    private final static String[] columnNames = new String[] {
         "Date/Time", "User Name", "Event Name", "Resource Name"
    };
    
    private final static int COLUMN_DATE_TIME = 0;
    private final static int COLUMN_USER_NAME = 1;
    private final static int COLUMN_EVENT_NAME = 2;
    private final static int COLUMN_RESOURCE_NAME = 3;
        
    private List<Event> events;
    
    public void setData(List<Event> data) {
        
        this.events = data;
        fireTableDataChanged(); 
    }
    
    public void addEvent(Event event) {
        
        if (events == null) {
            events = new LinkedList<>();
        }
        
        events.add(event);
        fireTableDataChanged(); 
    }
    
    public Event getEventAt(int i) {
        
        return events.get(i);
    }
    
    public List<Event> getEvents() {
        return this.events;
    }
    
    public int size() {
        return events.size();
    }
    
    public void reloadTableModel() {
        fireTableDataChanged(); 
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
        
        int i = 0;
        
        if (events != null) {
            i = events.size();
        }
        
        return i;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        Event event = events.get(rowIndex);

        Object value = null;

        switch(columnIndex) {
            
            case COLUMN_DATE_TIME:
            {
                value = event.getEventTime();
                break;
            }
            case COLUMN_USER_NAME:
            {
                value = TableUtils.getUserName(event);
                break;
            }
            case COLUMN_EVENT_NAME:
            {
                value = event.getEventName();
                break;
            }
            case COLUMN_RESOURCE_NAME:
            {
                value = ResourceLookup.getResource(event);
                break;
            }
        }

        return value;
    } 
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
