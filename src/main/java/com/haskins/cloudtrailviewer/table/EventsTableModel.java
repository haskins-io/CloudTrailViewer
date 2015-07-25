
package com.haskins.cloudtrailviewer.table;

import com.haskins.cloudtrailviewer.core.EventDatabase;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.core.resource.ResourceLookup;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.TimeStampComparator;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author mark.haskins
 */
public class EventsTableModel extends AbstractTableModel implements EventDatabaseListener {
    
    public final String[] columnNames = new String[] {
         "Date/Time", "User Name", "Service", "Name", "Resource Type", "Resource Name"
    };
            
    private final EventDatabase eventsDb;
        
    public EventsTableModel(EventDatabase eventsDatabase) {
        eventsDb = eventsDatabase;
        eventsDb.addListener(this);
    }
            
    public void orderTimeStamps() {
        
        Collections.sort(eventsDb.getEvents(), new TimeStampComparator());
        reloadTableModel();
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
        return columnNames[index];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
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

        switch(columnIndex) {
            
            case 0:
                value = TableUtils.getFormatedDateTime(event.getTimestamp());;
                break;
            case 1:
                value = TableUtils.getUserName(event);
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
        }

        return value;
    } 
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // EventDatabaseListener implementation
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        reloadTableModel();
    }
}
