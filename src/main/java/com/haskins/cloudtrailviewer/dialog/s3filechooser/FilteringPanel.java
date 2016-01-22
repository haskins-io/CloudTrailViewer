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
import com.haskins.cloudtrailviewer.model.filter.EventFieldFilter;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 *
 * @author mark.haskins
 */
public class FilteringPanel extends JPanel implements ActionListener {
    
    private static final String ACTION_ADD = "ActionAdd";
    
    private static final String ACTION_MODE_AND = "ActionModeAnd";
    private static final String ACTION_MODE_OR = "ActionModeOr";
    private static final long serialVersionUID = -3267460281771883973L;
    
    private final DefaultComboBoxModel filter_list = new DefaultComboBoxModel();
    private final JComboBox filterCombo = new JComboBox(filter_list);

    private final CompositeFilter filters = new CompositeFilter();
    
    private LinkedList<FilterPanel> panelsList = new LinkedList<>();
    private final JPanel activeFilterPanel = new JPanel();
    
    /**
     * Default constructor
     */
    public FilteringPanel() {
        
        addAvailableFilters();
        
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns all the filters
     * @return 
     */
    public CompositeFilter getFilters() {
        return filters;
    }
    
    /**
     * Removes the passed filter panel
     * 
     * @param filterToRemove 
     */
    public void removeFilter(FilterPanel filterToRemove) {

        filters.removeFilter(filterToRemove.getFilter());

        activeFilterPanel.remove(filterToRemove);
        activeFilterPanel.revalidate();
        
        panelsList.remove(filterToRemove);
        if (!panelsList.isEmpty()) {
            ((FilterPanel)panelsList.getFirst()).hideAndPanel();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        if (actionCommand.equalsIgnoreCase(ACTION_ADD)) {
            addFilter();
        } else if (actionCommand.equalsIgnoreCase(ACTION_MODE_AND)) {
            changeMode(CompositeFilter.BITWISE_AND);
        } else if (actionCommand.equalsIgnoreCase(ACTION_MODE_OR)) {
            changeMode(CompositeFilter.BITWISE_OR);
        }
    }
 
    ////////////////////////////////////////////////////////////////////////////
    // User Interface methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        buildFiterChoicePanel();
        buildActiveFilterContainer();
        buildFilterModePanel();
    }
    
    private void addAvailableFilters() {
     
        filter_list.addElement(new FilterWrapper("Text Filter", AllFilter.class));
        filter_list.addElement(new FilterWrapper("Date Filter", DateFilter.class));
        
        // event
        filter_list.addElement(new FilterWrapper("Event Name", EventFieldFilter.class, "eventName"));
        filter_list.addElement(new FilterWrapper("AWS Region", EventFieldFilter.class, "awsRegion"));
        filter_list.addElement(new FilterWrapper("Source IP Address", EventFieldFilter.class, "sourceIPAddress"));
        filter_list.addElement(new FilterWrapper("User Agent", EventFieldFilter.class, "userAgent"));
        filter_list.addElement(new FilterWrapper("Event Source", EventFieldFilter.class, "eventSource"));
        filter_list.addElement(new FilterWrapper("Error Code", EventFieldFilter.class, "errorCode"));
        filter_list.addElement(new FilterWrapper("Error Message", EventFieldFilter.class, "errorMessage"));
        filter_list.addElement(new FilterWrapper("Recipient Account Id", EventFieldFilter.class, "recipientAccountId"));
        
        // user identity
        filter_list.addElement(new FilterWrapper("User Identity : Type", EventFieldFilter.class, "userIdentity.type"));
        filter_list.addElement(new FilterWrapper("User Identity : Principal Id", EventFieldFilter.class, "userIdentity.principalId"));
        filter_list.addElement(new FilterWrapper("User Identity : Arn", EventFieldFilter.class, "userIdentity.arn"));
        filter_list.addElement(new FilterWrapper("User Identity : Account Id", EventFieldFilter.class, "userIdentity.accountId"));
        filter_list.addElement(new FilterWrapper("User Identity : Access Key Id", EventFieldFilter.class, "userIdentity.accessKeyId"));
        filter_list.addElement(new FilterWrapper("User Identity : User name", EventFieldFilter.class, "userIdentity.userName"));
        filter_list.addElement(new FilterWrapper("User Identity : Invoked By", EventFieldFilter.class, "userIdentity.invokedBy"));
        filter_list.addElement(new FilterWrapper("User Identity : Web Id Federation Data", EventFieldFilter.class, "userIdentity.webIdFederationData"));
        
        // Sessoin Context
        filter_list.addElement(new FilterWrapper("Session Issuer : Type", EventFieldFilter.class, "userIdentity.sessionContext.sessionIssuer.type"));
        filter_list.addElement(new FilterWrapper("Session Issuer : Principal Id", EventFieldFilter.class, "userIdentity.sessionContext.sessionIssuer.principalId"));
        filter_list.addElement(new FilterWrapper("Session Issuer : Arn", EventFieldFilter.class, "userIdentity.sessionContext.sessionIssuer.arn"));
        filter_list.addElement(new FilterWrapper("Session Issuer : Account Id", EventFieldFilter.class, "userIdentity.sessionContext.sessionIssuer.accountId"));
        filter_list.addElement(new FilterWrapper("Session Issuer : User name", EventFieldFilter.class, "userIdentity.sessionContext.sessionIssuer.userName"));
    }
    
    private void buildFiterChoicePanel() {
    
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        filterCombo.setRenderer(new FilterComboBoxRenderer());
        filterCombo.setMaximumRowCount(10);
        
        JButton addButton = new JButton("Add");
        addButton.setActionCommand(ACTION_ADD);
        addButton.addActionListener(this);
        
        panel.add(new JLabel("Filter"), BorderLayout.WEST);
        panel.add(filterCombo, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.EAST);
        
        this.add(panel, BorderLayout.PAGE_START);
    }
    
    private void buildActiveFilterContainer() {
        
        activeFilterPanel.setLayout(new BoxLayout(activeFilterPanel, BoxLayout.Y_AXIS));
        
        JPanel filtersPanel = new JPanel(new FlowLayout());
        filtersPanel.add(activeFilterPanel);
        
        JScrollPane filtersScroller = new JScrollPane(filtersPanel);
        filtersScroller.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        filtersScroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        this.add(filtersScroller, BorderLayout.CENTER);
    }
    
    private void buildFilterModePanel() {
        
        JPanel modePanel = new JPanel(new FlowLayout());
        modePanel.add(new JLabel("Filtering Mode : "));
        
        JRadioButton btnAnd = new JRadioButton("AND");
        btnAnd.setActionCommand(ACTION_MODE_AND);
        btnAnd.setSelected(true);
        btnAnd.addActionListener(this);
        
        JRadioButton btnOr = new JRadioButton("OR");
        btnOr.setActionCommand(ACTION_MODE_OR);
        btnOr.setSelected(false);
        btnOr.addActionListener(this);
        
        ButtonGroup group = new ButtonGroup();
        group.add(btnAnd);
        group.add(btnOr);
        
        modePanel.add(btnAnd);
        modePanel.add(btnOr);
        
        this.add(modePanel, BorderLayout.PAGE_END);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Logic methods
    ////////////////////////////////////////////////////////////////////////////
    private void addFilter() {
        
        FilterWrapper wrapper = (FilterWrapper)filterCombo.getSelectedItem();

        final Filter filter = getFilter(wrapper);
        if (filter != null) {

            boolean isFirst = true;
            if (filters.size() != 0) {
                isFirst = false;
            }

            FilterPanel filterPanel = 
                    new FilterPanel(wrapper.getFilterName(), filter, isFirst, this, filters.getModeString());
            
            panelsList.add(filterPanel);
            filters.addFilter(filter);

            activeFilterPanel.add(filterPanel);
            activeFilterPanel.revalidate();
        }
    }
    
    private Filter getFilter(FilterWrapper wrapper) {
        
        Filter filter = null;
        
        try {
            Class filterClass = wrapper.getClassType();

            filter = (Filter)filterClass.newInstance();
            if (filter instanceof EventFieldFilter) {
                ((EventFieldFilter)filter).setOption(wrapper.getOption());
            }
         
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FilteringPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return filter;
    }
    
    private void changeMode(int mode) {
        
        filters.setMode(mode);
        for (FilterPanel panel : panelsList) {
            panel.setMode(filters.getOperators()[mode]);
        }
    }
}

class FilterWrapper {

    private final String filterName;
    private final Class classType;
    private final String optionalField;
    
    public FilterWrapper(String name, Class classname) {
        this(name, classname, null);
    }
    
    public FilterWrapper(String name, Class classname, String option) {
        
        this.filterName = name;
        this.classType = classname;
        this.optionalField = option;
    }

    public String getFilterName() {
        return this.filterName;
    }
    
    public Class getClassType() {
        return this.classType;
    }
    
    public String getOption() {
        return this.optionalField;
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
