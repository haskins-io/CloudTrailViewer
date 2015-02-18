package com.haskins.jcloudtrailerviewer.table;

import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark.haskins
 */
public class EventsTableModel extends DefaultTableModel implements EventsDatabaseListener {

    private final static int COLUMN_DATE_TIME = 0;
    private final static int COLUMN_EVENT_NAME = 1;
    private final static int COLUMN_USERNAME = 2;
    private final static int COLUMN_EVENT_SOURCE = 3;
    private final static int COLUMN_REGION = 4;
    private final static int COLUMN_USER_AGENT = 5;
    
    private static EventsDatabase eventsDatabase;
    
    private List<Event> filteredEvents;
    
    public EventsTableModel(EventsDatabase database) {
        
        eventsDatabase = database;
        
        filteredEvents = new ArrayList();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // EventsDatabaseListener implementation
    ///////////////////////////////////////////////////////////////////////////
    
    @Override
    public void onEventsUpdated(CopyOnWriteArrayList<Event> updatedEvents)
    {          
        if (updatedEvents != null) {
            filteredEvents = updatedEvents;
            fireTableDataChanged(); 
        }
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
    public int getRowCount()
    {
        int i = 0;
        
        if (filteredEvents != null) {
            i = filteredEvents.size();
        }
        
        return i;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {        
        Event event = filteredEvents.get(rowIndex);

        Object value = null;

        switch(columnIndex)
        {
            case COLUMN_DATE_TIME:
            {
                value = event.getEventTime();
                break;
            }
            case COLUMN_EVENT_NAME:
            {
                value = event.getEventName();
                break;
            }
            case COLUMN_USERNAME:
            {
                value = "";
                break;
            }
            case COLUMN_EVENT_SOURCE:
            {
                value = event.getSourceIPAddress();
                break;
            }
            case COLUMN_REGION:
            {
                value = event.getAwsRegion();
                break;
            }
            case COLUMN_USER_AGENT:
            {
                value = event.getUserAgent();
                break;
            }
        }

        return value;
    } 
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    
    private final static String[] columnNames = new String[] {
         "Date/Time", "Event Name", "Username", "Event Source", "Region", "User Agent"
    };
}
