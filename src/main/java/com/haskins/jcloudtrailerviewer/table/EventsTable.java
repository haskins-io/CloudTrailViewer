package com.haskins.jcloudtrailerviewer.table;

import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark.haskins
 */
public class EventsTable extends JTable {
    
    private EventsDatabase database = null;
    
    public EventsTable(EventsTableModel model, EventsDatabase database) {
        
        super(model);
        super.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        this.database = database;
                
        setupTable();
    }
    
    private void setupTable() {
        
        setRowSelectionAllowed(true);
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        for (int i = 0; i<6; i++) {
            
            TableColumn column = this.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
            
        }
    }
}
