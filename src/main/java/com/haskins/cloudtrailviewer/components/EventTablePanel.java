/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.sidebar.EventJson;
import com.haskins.cloudtrailviewer.sidebar.EventTree;
import com.haskins.cloudtrailviewer.sidebar.EventsStats;
import com.haskins.cloudtrailviewer.sidebar.SideBarPanel;
import com.haskins.cloudtrailviewer.table.EventsTable;
import com.haskins.cloudtrailviewer.table.EventsTableModel;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author mark
 */
public class EventTablePanel extends JPanel{
    
    public static final String NAME = "Simple Table";
    
    private final FilteredEventDatabase eventDb;
    private final EventsTableModel tableModel;
    private final EventsTable table;
        
    private final SideBarPanel sideBar = new SideBarPanel();
    private JSplitPane jsp;
    
    private final JTextField filterTextField = new JTextField();
    
    /**
     * Default constructor
     * @param eventsDatabase reference to an Event Database 
     */
    public EventTablePanel(FilteredEventDatabase eventsDatabase) {
        
        eventDb = eventsDatabase;
        tableModel = new EventsTableModel(eventsDatabase);
        table = new EventsTable(tableModel);
        
        buildUI();
    }
    
    public void toggleSideBar() {
        
        sideBar.setVisible(!sideBar.isVisible());
        
        if (sideBar.isVisible()) {
            jsp.setDividerLocation(0.8);
            jsp.setDividerSize(3);
        } else {
            jsp.setDividerLocation(1);
            jsp.setDividerSize(0);
        }
    }
    
    public void eventLoadingComplete() {
        tableModel.orderTimeStamps();
        sideBar.eventLoadingComplete();
    
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        this.setLayout(new BorderLayout());
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    
                    Event event = tableModel.getEventAt(table.getSelectedRow());
                    if (event != null) {
                        
                        sideBar.currentEvent(event);

                        if (!sideBar.isVisible()) {
                            toggleSideBar();
                        }
                    }
                }
            }
        });

        JScrollPane eventsScrollPane = new JScrollPane(table);
        eventsScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        sideBar.addSideBar(new EventJson());
        sideBar.addSideBar(new EventTree());
        sideBar.addSideBar(new EventsStats(eventDb));
        
        sideBar.setVisible(false);
        
        jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, eventsScrollPane, sideBar);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().width
                             - jsp.getInsets().right
                             - jsp.getDividerSize());
        
        
        filterTextField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                filterUpdate();
            }

        });
        
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(new JLabel("Filter"), BorderLayout.WEST);
        filterPanel.add(filterTextField, BorderLayout.CENTER);
                
        add(filterPanel, BorderLayout.PAGE_START);
        add(jsp, BorderLayout.CENTER);
    }
    
    private void toggleColumnVisibleState(String columnName) {
        
        int position = tableModel.findColumn(columnName);

        if (table.getColumnModel().getColumn(position).getWidth() == 0) {
            table.getColumnModel().getColumn(position).setMaxWidth(100);
            table.getColumnModel().getColumn(position).setMinWidth(100);
            table.getColumnModel().getColumn(position).setPreferredWidth(100);
            table.getColumnModel().getColumn(position).setResizable(true);
        } else {
            table.getColumnModel().getColumn(position).setMaxWidth(0);
            table.getColumnModel().getColumn(position).setMinWidth(0);
            table.getColumnModel().getColumn(position).setPreferredWidth(0);
            table.getColumnModel().getColumn(position).setResizable(false);
        }
    }
    
    private void filterUpdate()
    {
        String text = filterTextField.getText();
        this.eventDb.getFilter().setNeedle(text);
    }
}
