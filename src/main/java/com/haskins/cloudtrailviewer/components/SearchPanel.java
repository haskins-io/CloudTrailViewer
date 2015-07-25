/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.model.filter.AllFilter;
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
 *
 * @author mark
 */
public class SearchPanel extends JPanel {
    
    private final JComboBox searchOptions = new JComboBox();
    private final JTextField searchField = new JTextField();
        
    private final List<SearchPanelListener> listeners = new ArrayList<>();
    
    public SearchPanel() {
        buildUI();
    }
    
    public void addListener(SearchPanelListener l) {
        listeners.add(l);
    }
    
    public Filter getFilter() {
        
        Filter f = null;
        
        if (searchField.getText().trim().length() > 0) {
            f = (Filter)this.searchOptions.getSelectedItem();
            f.setNeedle(searchField.getText());
        }
       
        return f;
    }
    
    private void buildUI() {

        this.setLayout(new BorderLayout());
        
        JPanel components = new JPanel();
        components.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        components.setLayout(new BoxLayout(components, BoxLayout.LINE_AXIS));
        
        // dropdown
        searchOptions.addItem(new AllFilter());
        
        components.add(searchOptions);
        
        // search field
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
