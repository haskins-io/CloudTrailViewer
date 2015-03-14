/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventLoaderListener;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import com.haskins.jcloudtrailerviewer.util.ConstantsActions;
import com.haskins.jcloudtrailerviewer.util.EventTimestampComparator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * @author mark
 */
public class SecurityPanel extends JInternalFrame implements EventLoaderListener {

    private final EventUtils eventUtils = new EventUtils();
    
    private final EventLoader eventLoader = new EventLoader();
    
    private final EventsTableModel errorTableModel;
    private final EventsTableModel iamTableModel;
    private final EventsTableModel securityTableModel;
    
    private final EventDetailTableModel detailTableModel = new EventDetailTableModel();

    private final JTextArea rawJsonPanel = new JTextArea();
    
    private final List<String> actions_iam = new ArrayList<>();
    private final List<String> actions_network = new ArrayList<>();
    
    private final JTabbedPane tabs = new JTabbedPane();
    
    public SecurityPanel() {
        
        super("Security", true, true, false, true);
        
        eventLoader.addListener(this);
        
        errorTableModel = new EventsTableModel();
        iamTableModel = new EventsTableModel();
        securityTableModel = new EventsTableModel();
        
        actions_iam.addAll(Arrays.asList(ConstantsActions.ACTIONS_IAM));
        actions_network.addAll(Arrays.asList(ConstantsActions.ACTIONS_NETWORK));
        
        buildUI();
        
        Object[] options = {"Load Files", "S3 Files"};
        int i = JOptionPane.showOptionDialog(
            jCloudTrailViewer.DESKTOP, 
            "Do you want to checks local files or remote files", 
            "Choose File Location", 
            JOptionPane.YES_NO_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);
        
        if (i == 0) {
            eventLoader.showFileBrowser();
        } else {
            eventLoader.showS3Browser();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {
        
        for (Event event : events) {
            
            // Errors
            if (event.getErrorCode().length() > 1) {
                if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
                EventUtils.addTimestamp(event);
                errorTableModel.addEvent(event);
                tabs.setTitleAt(0, "Errors (" + errorTableModel.size() + ")");
            }
            
            // IAM
            if (event.getEventSource().equalsIgnoreCase("iam.amazonaws.com") && actions_iam.contains(event.getEventName())) {
                if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
                EventUtils.addTimestamp(event);
                iamTableModel.addEvent(event);
                tabs.setTitleAt(1, "Iam (" + iamTableModel.size() + ")");
            }
            
            // Security
            if (event.getEventSource().equalsIgnoreCase("ec2.amazonaws.com") && actions_network.contains(event.getEventName())) {
                if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
                EventUtils.addTimestamp(event);
                securityTableModel.addEvent(event);
                tabs.setTitleAt(2, "Network (" + securityTableModel.size() + ")");
            }
        }
    }
    
    @Override
    public void finishedLoading() {
        
        // sort data in table models
        List<Event> tmpErrorList = errorTableModel.getEvents();
        Collections.sort(tmpErrorList, new EventTimestampComparator());
        errorTableModel.setData(tmpErrorList);
        
        List<Event> tmpIamList = iamTableModel.getEvents();
        Collections.sort(tmpIamList, new EventTimestampComparator());
        iamTableModel.setData(tmpIamList);
        
        List<Event> tmpSecurityList = securityTableModel.getEvents();
        Collections.sort(tmpSecurityList, new EventTimestampComparator());
        securityTableModel.setData(tmpSecurityList);
        
        // reload all TableModels
        errorTableModel.reloadTableModel();
        iamTableModel.reloadTableModel();
        securityTableModel.reloadTableModel();
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
                
                List<Event> events = null;
                
                int tabId = tabs.getSelectedIndex();
                switch(tabId) {
                    case 0:
                        events = errorTableModel.getEvents();
                        break;
                    case 1:
                        events = iamTableModel.getEvents();
                        break;
                    case 2:
                        events = securityTableModel.getEvents();
                        break;
                }
                
                if (events != null) {
                    
                    ChartData chartData = ChartDialog.showDialog(jCloudTrailViewer.DESKTOP);

                    if (chartData != null) {

                        List<Map.Entry<String, Integer>> chartMetrics = eventUtils.getRequiredEvents(events, chartData);

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
        
        
        // need three tables
        final EventsTable errorTable = new EventsTable(errorTableModel);
        errorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = errorTableModel.getEventAt(errorTable.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });
        errorTable.setVisible(true);
        JScrollPane errorScrollPane = new JScrollPane(errorTable);
        
        final EventsTable iamTable = new EventsTable(iamTableModel);
        iamTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = iamTableModel.getEventAt(iamTable.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });
        iamTable.setVisible(true);
        JScrollPane iamScrollPane = new JScrollPane(iamTable);
        
        final EventsTable securityTable = new EventsTable(securityTableModel);
        securityTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = securityTableModel.getEventAt(securityTable.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });
        securityTable.setVisible(true);
        JScrollPane securityScrollPane = new JScrollPane(securityTable);
        

        // Detail area
        EventDetailTable detailTable = new EventDetailTable(detailTableModel);
        JScrollPane eventDetailScrollPane = new JScrollPane(detailTable);
        JScrollPane rawJsonScrollPane = new JScrollPane(rawJsonPanel);

        JTabbedPane detailPanel = new JTabbedPane();
        detailPanel.add("Table View", eventDetailScrollPane);
        detailPanel.add("Raw View", rawJsonScrollPane);
        
        rawJsonPanel.setFont(new Font("Verdana", Font.PLAIN, 12));

        
        // need a tabbed pane for the tables
        tabs.add("Errors", errorScrollPane);
        tabs.add("IAM", iamScrollPane);
        tabs.add("Network", securityScrollPane);
        
        // need a split pane to put everything together
        JSplitPane split = new JSplitPane();
        split.add(tabs, JSplitPane.LEFT);
        split.add(detailPanel, JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(2);
        split.setAutoscrolls(false);
        split.setDividerLocation(400);
        
        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }
    
    private void showEventDetail(Event event) {

        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }
}
