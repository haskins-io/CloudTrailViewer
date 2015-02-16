package com.haskins.jcloudtrailerviewer.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mark
 */
public class TopFiveTableModel extends DefaultTableModel {
        
    private List<Entry<String,Integer>> topFiveData = new ArrayList<>();
    
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
