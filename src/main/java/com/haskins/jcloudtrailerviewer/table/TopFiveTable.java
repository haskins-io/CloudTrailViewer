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