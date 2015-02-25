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

import java.awt.Font;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
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
        
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        
        TableModel model = getModel();
        
        return (String)model.getValueAt(rowIndex, 1);
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
