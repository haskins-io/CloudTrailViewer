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
package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.filter.EventFilter;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.util.EventTimestampComparator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.ChartMouseEvent;

/**
 *
 * @author mark.haskins
 */
public class ScanTablePanel extends AbstractInternalFrame  {
        
    private List<String> scanActions;
    private String scanNeedle;
    
    public ScanTablePanel(MenuDefinition menuDefinition) {
        
        super(menuDefinition.getName());
        
        if (menuDefinition.getActions() != null) {
            scanActions = menuDefinition.getActions();
        }
        
        if (menuDefinition.getContains() != null) {
            
            EventFilter filter = null;
            
            if (menuDefinition.getContains().contains(":")) {
                
                String[] parts = menuDefinition.getContains().split(":");
                
                String filterName = parts[0];
                try {
                    Class c = Class.forName("com.haskins.jcloudtrailerviewer.filter." + filterName);
                    filter = (EventFilter)c.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(ScanTablePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                scanNeedle = parts[1];
                
            } else {
                filter = new FreeformFilter();
                scanNeedle = menuDefinition.getContains();
            }
            
            if (filter != null) {
                filters.addEventFilter(filter);
            }
        }
               
        eventLoader.addListener(this);
        
        int scanDialogResult = showScanDialog();
        if (scanDialogResult == 0) {
            eventLoader.showFileBrowser();
            buildUI();
        } else if (scanDialogResult == 1) {
            eventLoader.showS3Browser();
            buildUI();
        } else {
            this.dispose();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {
               
        for (Event event : events) {
            
            if (scanActions != null && scanActions.size() > 0 && scanActions.contains(event.getEventName()) ) {
                addEvent(event);
            } else  {
                
                filters.setFilterCriteria(scanNeedle);
                if (filters.passesFilter(event)) {
                    addEvent(event);
                } 
            }
        }
    }
    
    @Override
    public void finishedLoading() {

        List<Event> tmpErrorList = eventsTableModel.getEvents();
        Collections.sort(tmpErrorList, new EventTimestampComparator());
        eventsTableModel.setData(tmpErrorList);
    }
    
    @Override
    public void newMessage(String message) {}
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {

        this.setSize(640, 480);
        
        // chart button
        JButton btnShowChart = new JButton();
        btnShowChart.setActionCommand("NewChart");
        btnShowChart.setToolTipText("Show Chart");

        try {
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/chart-pie.png");
            btnShowChart.setIcon(new ImageIcon(imageUrl));
        }
        catch (Exception e) {
            btnShowChart.setText("Show Chart");
        }

        btnShowChart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                List<Event> events = eventsTableModel.getEvents();
                                
                if (events != null) {
                    
                    ChartData chartData = ChartDialog.showDialog(jCloudTrailViewer.DESKTOP);

                    if (chartData != null) {

                        List<Map.Entry<String, Integer>> chartMetrics = EventUtils.getRequiredEvents(events, chartData);

                        ChartWindow chart = new ChartWindow(chartData, chartMetrics);
                        chart.setVisible(true);

                        jCloudTrailViewer.DESKTOP.add(chart);
                        
                        try {
                            chart.setSelected(true);
                        }
                        catch (java.beans.PropertyVetoException pve) {
                        }
                    }
                }
            }
        });
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(btnShowChart);
        
        final EventsTable actionsTable = new EventsTable(eventsTableModel);
        actionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = eventsTableModel.getEventAt(actionsTable.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });
        actionsTable.setVisible(true);
        JScrollPane actionsScrollPane = new JScrollPane(actionsTable);
                
        
        // need a split pane to put everything together
        JSplitPane split = new JSplitPane();
        split.add(actionsScrollPane, JSplitPane.LEFT);
        split.add(getEventDetailPanel(), JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(2);
        split.setAutoscrolls(false);
        split.setDividerLocation(400);
        
        addStatusBar();
        
        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }
    
    private void addEvent(Event event) {
        eventsTableModel.addEvent(event); 
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) { }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }
}
