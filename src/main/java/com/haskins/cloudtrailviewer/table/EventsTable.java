

package com.haskins.cloudtrailviewer.table;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark.haskins
 */
public class EventsTable extends JTable {
        
    /**
     * Default Constructor.
     * 
     * Create a new JTable with a EventTableModel as its TableModel
     * @param model 
     */
    public EventsTable(EventsTableModel model) {
        
        super(model);
        super.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                
        setupTable();
    }
    
    private void setupTable() {
        
        setRowSelectionAllowed(true);
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        for (int i = 0; i<this.dataModel.getColumnCount(); i++) {
            
            TableColumn column = this.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
    }
}
