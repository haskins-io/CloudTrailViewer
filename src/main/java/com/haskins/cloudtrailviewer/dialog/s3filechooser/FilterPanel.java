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
import com.haskins.cloudtrailviewer.model.filter.DateFilter;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author mark.haskins
 */
public class FilterPanel extends JPanel implements ActionListener {
    
    private static final String ACTION_ADD = "ActionAdd";
    
    private final DefaultComboBoxModel filter_list = new DefaultComboBoxModel();
    private final JComboBox filterCombo = new JComboBox(filter_list);

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
        
        if (actionCommand.equalsIgnoreCase(ACTION_ADD)) {
            addFilter();
            
        } 
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
     
        filter_list.addElement(new FilterWrapper("All Filter", AllFilter.class));
        filter_list.addElement(new FilterWrapper("Date Filter", DateFilter.class));
    }
    
    private void buildFiterChoicePanel() {
    
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        filterCombo.setRenderer(new FilterComboBoxRenderer());
        
        JButton addButton = new JButton("Add");
        addButton.setActionCommand(ACTION_ADD);
        addButton.addActionListener(this);
        
        panel.add(new JLabel("Filter"), BorderLayout.WEST);
        panel.add(filterCombo, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.EAST);
        
        this.add(panel, BorderLayout.PAGE_START);
    }
    
    private void builtActiveFilterContainer() {
        
        activeFilterPanel.setLayout(new BoxLayout(activeFilterPanel, BoxLayout.Y_AXIS));
        
        JPanel filtersPanel = new JPanel(new FlowLayout());
        filtersPanel.add(activeFilterPanel);
        
        this.add(filtersPanel, BorderLayout.CENTER);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Logic methods
    ////////////////////////////////////////////////////////////////////////////
    private void addFilter() {
        
        try {
            
            FilterWrapper wrapper = (FilterWrapper)filterCombo.getSelectedItem();
            Class filterClass = wrapper.getClassType();
            
            final Filter filter = (Filter)filterClass.newInstance();
            
            filters.addFilter(filter);
            
            final JPanel filterPanel = new JPanel(new BorderLayout());
            
            JPanel panel = filter.getFilterPanel();
            panel.setMinimumSize(new Dimension(375,30));
            panel.setPreferredSize(new Dimension(375,30));
            panel.setMaximumSize(new Dimension(375,30));
            filterPanel.add(panel, BorderLayout.CENTER);
            
            JButton removeFilter = new JButton("Remove");
            removeFilter.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    filters.removeFilter(filter);
                    
                    activeFilterPanel.remove(filterPanel);
                    activeFilterPanel.revalidate();
                }
            });
            
            filterPanel.add(removeFilter, BorderLayout.LINE_END);

            activeFilterPanel.add(filterPanel);
            activeFilterPanel.revalidate();
            
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FilterPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
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

class FilterComboBoxRenderer extends JLabel implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        FilterWrapper wrapper = (FilterWrapper)value;
        
        setText(wrapper.getFilterName());
        
        return this;
    }
}
