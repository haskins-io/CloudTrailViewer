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
         "Date/Time", "User Name", "Event Name", "Resource Type", "Resource Name"
    };
            
    private List<Event> events;
    
    /**
     * Overrides the current data in the model with the passed data
     * @param data collection of events to be used by the model
     */
    public void setData(List<Event> data) {
        
        this.events = data;
        fireTableDataChanged(); 
    }
    
    /**
     * Adds a new Event to the existing data held by the model
     * @param event new Event to add
     */
    public void addEvent(Event event) {
        
        if (events == null) {
            events = new LinkedList<>();
        }
        
        events.add(event);
        fireTableDataChanged(); 
    }
    
    /**
     * Returns the event object held within the model at the required position
     * @param i position in model
     * @return 
     */
    public Event getEventAt(int i) {
        return events.get(i);
    }
    
    /**
     * Returns all the events in the model
     * @return collection of Events
     */
    public List<Event> getEvents() {
        return this.events;
    }
    
    /**
     * Returns the number of Events in the model
     * @return number of events
     */
    public int size() {
        return events.size();
    }
    
    /**
     * Forces the Model to reload the data.
     */
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
            
            case 0:
                value = event.getEventTime();
                break;
            case 1:
                value = TableUtils.getUserName(event);
                break;
            case 2:
                value = event.getEventName();
                break;
            case 3:
                value = ResourceLookup.getResourceInfo(event).getTypes();
                break;
            case 4:
                value = ResourceLookup.getResourceInfo(event).getNames();
                break;
        }

        return value;
    } 
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
