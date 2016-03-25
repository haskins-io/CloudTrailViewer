/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.model.filter.CompositeFilter;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Reusable panel that provides search functionality
 * 
 * @author mark
 */
public class SearchPanel extends JPanel {

    private static final long serialVersionUID = -1107795006666536735L;
    
    private final JComboBox searchOptions = new JComboBox();
    private final JTextField searchField = new JTextField();

    private final List<SearchPanelListener> listeners = new ArrayList<>();

    /**
     * Default Constructor
     */
    public SearchPanel() {
        buildUI();
    }

    /**
     * adds a listener to the component
     * @param l reference to a listener
     */
    public void addListener(SearchPanelListener l) {
        listeners.add(l);
    }

    /**
     * Returns the filter to be used as part of the search
     * @return CompositeFilter object containing current selected filters
     */
    public CompositeFilter getFilter() {
        
        CompositeFilter filters = new CompositeFilter();
                
        if (searchField.getText().trim().length() > 0) {
            Filter f = (Filter)this.searchOptions.getSelectedItem();
            f.setNeedle(searchField.getText());
            
            filters.addFilter(f);
        }
       
        
        return filters;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {

        this.setLayout(new BorderLayout());
        
        JPanel components = new JPanel();
        components.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        components.setLayout(new BoxLayout(components, BoxLayout.LINE_AXIS));
        
        searchOptions.addItem(new AllFilter());
        
        components.add(searchOptions);
        
        searchField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               
                for (SearchPanelListener listener : listeners) {
                    listener.enterPressed();
                }
            }
        });
        
        components.add(searchField);
        
        this.add(components, BorderLayout.CENTER);
    }
}
