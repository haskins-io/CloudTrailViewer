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

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventLoaderListener;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import com.haskins.jcloudtrailerviewer.util.EventTimestampComparator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author mark.haskins
 */
public class ScanTablePanel extends JInternalFrame implements EventLoaderListener {
    
    private final EventLoader eventLoader = new EventLoader();
    
    private final EventsTableModel tableModel = new EventsTableModel();
    private final EventDetailTableModel detailTableModel = new EventDetailTableModel();
    
    private final List<String> scanActions;
    
    private final JTextArea rawJsonPanel = new JTextArea();
    
    public ScanTablePanel(MenuDefinition menuDefinition) {
        
        super(menuDefinition.getName(), true, true, false, true);
        
        scanActions = menuDefinition.getActions();
                
        eventLoader.addListener(this);
        
        Object[] options = {"Local Files", "S3 Files"};
        int i = JOptionPane.showOptionDialog(
            jCloudTrailViewer.DESKTOP, 
            "Do you want to scan local files or remote files", 
            "Choose File location", 
            JOptionPane.YES_NO_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);
        
        if (i == 0) {
            eventLoader.showFileBrowser();
            buildUI();
        } else if (i == 1) {
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
            
            if (scanActions.contains(event.getEventName())) {
                if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
//                EventUtils.addTimestamp(event);
                tableModel.addEvent(event);
            }
        }
    }
    
    @Override
    public void finishedLoading() {

        List<Event> tmpErrorList = tableModel.getEvents();
        Collections.sort(tmpErrorList, new EventTimestampComparator());
        tableModel.setData(tmpErrorList);
    }
    
    @Override
    public void newMessage(String message) {}
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setLayout(new BorderLayout());

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
                
                List<Event> events = tableModel.getEvents();
                                
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
        
        final EventsTable actionsTable = new EventsTable(tableModel);
        actionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = tableModel.getEventAt(actionsTable.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });
        actionsTable.setVisible(true);
        JScrollPane actionsScrollPane = new JScrollPane(actionsTable);
                

        // Detail area
        EventDetailTable detailTable = new EventDetailTable(detailTableModel);
        JScrollPane eventDetailScrollPane = new JScrollPane(detailTable);
        JScrollPane rawJsonScrollPane = new JScrollPane(rawJsonPanel);

        JTabbedPane detailPanel = new JTabbedPane();
        detailPanel.add("Table View", eventDetailScrollPane);
        detailPanel.add("Raw View", rawJsonScrollPane);
        
        rawJsonPanel.setFont(new Font("Verdana", Font.PLAIN, 12));

        
        // need a split pane to put everything together
        JSplitPane split = new JSplitPane();
        split.add(actionsScrollPane, JSplitPane.LEFT);
        split.add(detailPanel, JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(2);
        split.setAutoscrolls(false);
        split.setDividerLocation(400);
        
        StatusBarPanel statusBarPanel = new StatusBarPanel();
        eventLoader.addListener(statusBarPanel);
        
        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(statusBarPanel, BorderLayout.SOUTH);
        
    }
    
    private void showEventDetail(Event event) {

        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }
}
