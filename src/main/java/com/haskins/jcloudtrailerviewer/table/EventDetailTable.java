package com.haskins.jcloudtrailerviewer.table;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

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
    
    @Override
    public String getToolTipText(MouseEvent e) {
        
        String tip = "";
        
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        
        TableModel model = getModel();
        
        
        
        tip = (String)model.getValueAt(rowIndex, 1);
        
        return tip;
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
