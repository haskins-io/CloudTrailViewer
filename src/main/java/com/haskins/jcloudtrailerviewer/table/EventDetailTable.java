package com.haskins.jcloudtrailerviewer.table;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark.haskins
 */
public class EventDetailTable extends JTable {
       
    public EventDetailTable(EventDetailTableModel model) {
        
        super(model);
        super.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        setupTable();
    }
    
    private void setupTable() {
        
        setRowSelectionAllowed(true);
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        for (int i = 0; i<2; i++) {
            
            TableColumn column = this.getColumnModel().getColumn(i);
            
            if (i == 0) {
                column.setPreferredWidth(150);
            } else {
                column.setPreferredWidth(250);
            }
        }
    }
}
