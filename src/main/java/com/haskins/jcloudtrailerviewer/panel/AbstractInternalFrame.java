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
import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;

/**
 * Abstract class that all JinternalFrame windows should implement. It provides
 * common logic and functionality.
 * 
 * @author mark.haskins
 */
public abstract class AbstractInternalFrame extends JInternalFrame implements ChartMouseListener, EventLoaderListener, ActionListener {
    
    protected abstract void updateTextArea();
    protected abstract void updateChartEvents(int newTop);
    
    protected final static String NEWLINE = "\n";
    
    // Filtering
    protected final Filters filters = new Filters();
    
    // Events
    protected final EventLoader eventLoader = new EventLoader();
    protected List<Event> events = new LinkedList<>();
    protected final EventUtils eventUtils = new EventUtils();
        
    // Table
    protected final DefaultTableModel defaultTableModel = new DefaultTableModel();   
    
    protected final EventsTableModel eventsTableModel = new EventsTableModel();
    protected final EventDetailTableModel eventsDetailTableModel = new EventDetailTableModel();
    
    // Chart
    protected ChartPanel chartPanel = null;
    protected ChartData chartData = new ChartData();
    protected List<Map.Entry<String, Integer>> chartEvents = new ArrayList<>();
    
    // GUI Components
    protected final JTextArea eventDetailTextArea = new JTextArea();
    
    public AbstractInternalFrame(String title) {
        
        super(title, true, true, false, true);
        
        // setup some gui stuff
        this.setLayout(new BorderLayout());
        
        eventDetailTextArea.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        // set some default values
        chartData.setChartStyle("Pie");
        chartData.setTop(5);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "Top5":
                updateChartEvents(5);
                reloadTable();
                updateTextArea();
                break;
            case "Top10":
                updateChartEvents(10);
                reloadTable();
                updateTextArea();
                break;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Protected Methods
    ////////////////////////////////////////////////////////////////////////////
    protected void generateInitialChartData() {
        chartEvents = EventUtils.getRequiredEvents(events, chartData);
    }
    
    protected void createChart(int width, int height) {
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createTopPieChart(chartData.getTop(), chartEvents, width, height);
           
        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                chartEvents, 
                width, height,
                chartData.getChartSource(), "Count",
                PlotOrientation.VERTICAL);
        } 
        
        if (chartPanel != null) {
            chartPanel.addChartMouseListener(this);
        }
    }
    
    protected void showEventDetail(Event event) {

        eventsDetailTableModel.showDetail(event);
        
        if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
        eventDetailTextArea.setText(event.getRawJSON());
    }
    
    protected JTabbedPane getEventDetailPanel() {
        
        EventDetailTable detailTable = new EventDetailTable(eventsDetailTableModel);
        JScrollPane eventDetailScrollPane = new JScrollPane(detailTable);
        JScrollPane rawJsonScrollPane = new JScrollPane(eventDetailTextArea);

        JTabbedPane detailPanel = new JTabbedPane();
        detailPanel.add("Table View", eventDetailScrollPane);
        detailPanel.add("Raw View", rawJsonScrollPane);
        
        return detailPanel;
    }
    
    protected int showScanDialog() {
        
        Object[] options = {"Local Files", "S3 Files"};
        return JOptionPane.showOptionDialog(
            jCloudTrailViewer.DESKTOP,
            "Do you want to scan local files or remote files",
            "Choose File location",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);   
    }
    
    protected void addStatusBar() {
        
        StatusBarPanel statusBarPanel = new StatusBarPanel();
        eventLoader.addListener(statusBarPanel);

        this.add(statusBarPanel, BorderLayout.SOUTH);
    }
    
    protected void addTabbedChartDetail(JTabbedPane tabs, int width, int height) {
        
        createChart(width, height);
        if (chartPanel != null) {
            tabs.addTab("Chart", chartPanel);
        }

        JTable table = new JTable(defaultTableModel);
        table.setPreferredSize(new Dimension(480, 260));
        reloadTable();
        tabs.addTab("Table", table); 

        updateTextArea();
        tabs.addTab("Data", eventDetailTextArea); 
    }
        
    protected void reloadTable() {
        
        if (events != null) {
            
            for (int i = defaultTableModel.getRowCount() -1; i>=0; i--) {
                defaultTableModel.removeRow(i);
            }

            for (Entry entry : chartEvents) {
                defaultTableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
            }            
        }
    } 
    
    protected void updateChart(JTabbedPane tabs, int newTop) {
        
        chartData.setTop(newTop);
        createChart(480, 160);
        tabs.remove(0);
        tabs.insertTab("Chart", null, chartPanel, "", 0);
        tabs.setSelectedIndex(0);
    }
}
