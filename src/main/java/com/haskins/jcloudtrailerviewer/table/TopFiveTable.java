package com.haskins.jcloudtrailerviewer.table;

import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark
 */
public class TopFiveTable extends JTable {
    
    public TopFiveTable(TopFiveTableModel model) {
        
        super(model);
        super.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                
        setupTable();
    }
    
    private void setupTable() {
        
        setFont(new Font("Arial", Font.PLAIN, 12));
        
        for (int i = 0; i<2; i++) {
            
            TableColumn column = this.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
    }
}