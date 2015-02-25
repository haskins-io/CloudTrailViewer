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

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark
 */
public class TopFiveTableModel extends DefaultTableModel {
        
    private List<Entry<String,Integer>> topFiveData = new LinkedList<>();
    
    public void update(List data) {
        
        topFiveData.clear();
        topFiveData = data;
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

        int count = 0;
        
        if (topFiveData != null) {
            count = topFiveData.size();
        }
        
        return count;
    }
    
   @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {        
        Entry<String,Integer> value = topFiveData.get(rowIndex);
        
        String retVal;
        if (columnIndex == 0) {
            retVal = value.getKey();
        } else {
            retVal = String.valueOf(value.getValue());
        }

        return retVal;
    } 
    
    private final static String[] columnNames = new String[] {
         "Property", "Count"
    };

}
