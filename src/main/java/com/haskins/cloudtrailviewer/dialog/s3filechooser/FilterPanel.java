/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2016  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.dialog.s3filechooser;

import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.model.filter.CompositeFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class FilterPanel extends JPanel implements ActionListener {
    
    private static final DefaultComboBoxModel FILTER_LIST = new DefaultComboBoxModel();
    
    private final CompositeFilter filters = new CompositeFilter();
    
    private final JPanel activeFilterPanel = new JPanel();
    
    public FilterPanel() {
        
        addAvailableFilters();
        
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////
    public CompositeFilter getFilters() {
        return filters;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
    }
 
    ////////////////////////////////////////////////////////////////////////////
    // User Interface methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        
        buildFiterChoicePanel();
        builtActiveFilterContainer();
    }
    
    private void addAvailableFilters() {
     
        FILTER_LIST.addElement(new FilterWrapper("All Filter", AllFilter.class));
        
        // will needs a custom render to show name of filter in the Combo
    }
    
    private void buildFiterChoicePanel() {
    
        JPanel panel = new JPanel(new BorderLayout());
        
        JComboBox filterCombo = new JComboBox(FILTER_LIST);
        // need action on change
        
        JButton addButton = new JButton("Add");
        addButton.setActionCommand("Add Filter");
        addButton.addActionListener(this);
        
        panel.add(new JLabel("Filter"), BorderLayout.WEST);
        panel.add(filterCombo, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.EAST);
        
        this.add(panel, BorderLayout.PAGE_START);
    }
    
    private void builtActiveFilterContainer() {
        
        activeFilterPanel.setLayout(new BoxLayout(activeFilterPanel, BoxLayout.PAGE_AXIS));
        
        this.add(activeFilterPanel, BorderLayout.CENTER);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Logic methods
    ////////////////////////////////////////////////////////////////////////////
    private void addFilter() {
        
        // get selected item in combo and do something
    }

}

class FilterWrapper {

    private final String filterName;
    private final Class classType;
    
    public FilterWrapper(String name, Class classname) {
    
        this.filterName = name;
        this.classType = classname;
    }

    public String getFilterName() {
        return this.filterName;
    }
    
    public Class getClassType() {
        return this.classType;
    }
}
