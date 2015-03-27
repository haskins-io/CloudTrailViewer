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

import com.haskins.jcloudtrailerviewer.event.EventLoaderListener;
import com.haskins.jcloudtrailerviewer.filter.EventFilter;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author mark.haskins
 */
public class CombinedPanel extends AbstractInternalFrame implements EventLoaderListener {
         
    private final Map<String, Map<String, Integer>> tpsMap = new HashMap<>();
    private ChartPanel tpsPanel = null;
    
    private List<String> scanActions;
    private String scanNeedle;
    
    public CombinedPanel(String title, List<Event> logEvents, MenuDefinition menuDefinition) {
        
        super(title);
        
        if (logEvents != null) {
            events = logEvents;
            generateData(logEvents);
            buildUI();
            
        } else {
            
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
            } else if (scanDialogResult == 1) {
                eventLoader.showS3Browser();
            } else {
                this.dispose();
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> eventLoaderEvent) {
               
        for (Event event : eventLoaderEvent) { 
        
            if (scanActions != null && scanActions.size() > 0 && scanActions.contains(event.getEventName()) ) {
                
                events.add(event);
                
            } else {

                filters.setFilterCriteria(scanNeedle);
                if (filters.passesFilter(event)) {
                   events.add(event);
                } 
            }
        }
    }
    
    @Override
    public void finishedLoading() { 
    
        generateData(events);
        buildUI();
        this.revalidate();
    }
    
    @Override
    public void newMessage(String message) { }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setSize(
                jCloudTrailViewer.DESKTOP.getWidth() - 50,
                jCloudTrailViewer.DESKTOP.getHeight() - 50);
        
        addTopXmenu();
        
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel,BoxLayout.Y_AXIS));
        
        // create PIE Chart
        sidePanel.add(tabs);
        addTabbedChartDetail(480, 160);
        
        // Detail area      
        sidePanel.add(getEventDetailPanel());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // create TPS Chart
        createTpsChart();
        mainPanel.add(tpsPanel, BorderLayout.NORTH);
        
        // Table
        final EventsTable table = new EventsTable(eventsTableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = eventsTableModel.getEventAt(table.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });

        table.setSize(new Dimension(400,205));
        table.setVisible(true);
        JScrollPane eventsScrollPane = new JScrollPane(table);
        
        mainPanel.add(eventsScrollPane, BorderLayout.CENTER);
        
        JSplitPane split = new JSplitPane();
        split.add(mainPanel, JSplitPane.LEFT);
        split.add(sidePanel, JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(10);
        split.setAutoscrolls(false);
        split.setDividerLocation(this.getWidth() - 300);
        split.setResizeWeight(1.0);
        
        this.add(split, BorderLayout.CENTER);
        
        this.validate();
    }
    
    private void generateData(List<Event> eventsData) {
        
        // generate Chart data
        chartData.setChartSource("EventName");
        chartData.setTop(5);
        chartData.setChartStyle("Pie");
        chartData.setChartType("Top");
        
        generateInitialChartData();
        
        // generate TPS data
        tpsPerService(eventsData);
        
        // Populate Table Model
        eventsTableModel.setData(eventsData);
    }
        
    private void createTpsChart() {
        
        tpsPanel = ChartCreator.createTimeSeriesChart("Transactions Per Sections", tpsMap, 400, 250);
    }
    
    private void tpsPerService(List<Event> eventsData) {
        
        for (Event event : eventsData) {
            
            String eventName = event.getEventName();
            if (inTopX(event)) {
                int tpsCount = 1;
                String dateTime = event.getEventTime();
                if (tpsMap.containsKey(eventName)) {

                    Map<String, Integer> serviceTps = tpsMap.get(eventName);
                    if (serviceTps.containsKey(dateTime)) {

                        tpsCount = serviceTps.get(dateTime);
                        tpsCount++;

                        serviceTps.put(dateTime, tpsCount);
                        tpsMap.put(eventName, serviceTps);
                    }
                    else {

                        serviceTps.put(dateTime, tpsCount);  
                        tpsMap.put(eventName, serviceTps);
                    }
                }
                else {

                     Map<String, Integer> serviceTps = new HashMap<>();
                     serviceTps.put(dateTime, tpsCount);
                     tpsMap.put(eventName, serviceTps);
                } 
            }
        }
    }
    
    private boolean inTopX(Event event) {
        
        boolean inTopX = false;
        
        for (Map.Entry topEvent : chartEvents) {
            
            if (topEvent.getKey().equals(event.getEventName())) {
                inTopX = true;
                break;
            }
        }
        
        return inTopX;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) { }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }
}
