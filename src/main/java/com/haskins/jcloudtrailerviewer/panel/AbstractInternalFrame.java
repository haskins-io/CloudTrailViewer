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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    
    protected final static String NEWLINE = "\n";
    
    // Filtering
    protected final Filters filters = new Filters();
    
    // Events
    protected final EventLoader eventLoader = new EventLoader();
    protected List<Event> events = new LinkedList<>();
    protected List<Event> topXEvents = new LinkedList<>();
    protected final EventUtils eventUtils = new EventUtils();
        
    // Table
    protected final DefaultTableModel defaultTableModel = new DefaultTableModel();   
    
    protected final EventsTableModel eventsTableModel = new EventsTableModel();
    protected final EventDetailTableModel eventsDetailTableModel = new EventDetailTableModel();
    
    // Chart
    protected ChartPanel chartPanel = null;
    protected ChartData chartData = new ChartData();
    protected List<Map.Entry<String, Integer>> chartEvents = new ArrayList<>();
    protected final Map<String, Integer> eventMap = new HashMap<>();
    
    // GUI Components
    protected final JTextArea eventDetailTextArea = new JTextArea();
    protected final JTextArea tabbedTextArea = new JTextArea();
    protected final JTabbedPane tabs = new JTabbedPane();
    
    public AbstractInternalFrame(String title) {
        
        super(title, true, true, false, true);
        
        // setup some gui stuff
        this.setLayout(new BorderLayout());
        
        eventDetailTextArea.setFont(new Font("Verdana", Font.PLAIN, 10));
        tabbedTextArea.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        // set some default values
        chartData.setChartStyle("Pie");
        chartData.setTop(5);
        
        defaultTableModel.addColumn("Property");
        defaultTableModel.addColumn("Value");
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "Top5":
                chartData.setTop(5);
                updateChartEvents();
                reloadTable();
                updateTextArea();
                break;
            case "Top10":
                chartData.setTop(10);
                updateChartEvents();
                reloadTable();
                updateTextArea();
                break;
            case "Pie":
                chartData.setChartStyle("Pie");
                updateChartEvents();
                break;
            case "Bar":
                chartData.setChartStyle("Bar");
                updateChartEvents();
                break;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // GUI Methods
    ////////////////////////////////////////////////////////////////////////////
    protected void createChart(int width, int height) {
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createTopPieChart(chartData.getTop(), chartEvents, width, height);
           
        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                chartData.getTop(),
                chartEvents, 
                width, height,
                chartData.getChartSource(), "Count",
                PlotOrientation.VERTICAL);
        } 
        
        if (chartPanel != null) {
            chartPanel.addChartMouseListener(this);
        }
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
    
    protected void addTabbedChartDetail(int width, int height) {
        
        createChart(width, height);
        if (chartPanel != null) {
            tabs.addTab("Chart", chartPanel);
        }

        JTable table = new JTable(defaultTableModel);
        JScrollPane tablecrollPane = new JScrollPane(table);
        reloadTable();
        tabs.addTab("Table", tablecrollPane); 

        updateTextArea();
        JScrollPane tabbedDataScrollPane = new JScrollPane(tabbedTextArea);
        tabs.addTab("Data", tabbedDataScrollPane); 
    }
    
    protected void addTopXmenu() {
        
        JMenuItem mnuTop5 = new JMenuItem("Top 5");
        mnuTop5.setActionCommand("Top5");
        mnuTop5.addActionListener(this);
        
        JMenuItem mnuTop10 = new JMenuItem("Top 10");
        mnuTop10.setActionCommand("Top10");
        mnuTop10.addActionListener(this);
                
        JMenu menuTop = new JMenu("Top");
        menuTop.add(mnuTop5);
        menuTop.add(mnuTop10);
        
        
        JMenuItem mnuPie = new JMenuItem("Pie");
        mnuPie.setActionCommand("Pie");
        mnuPie.addActionListener(this);
        
        JMenuItem mnuBar = new JMenuItem("Bar");
        mnuBar.setActionCommand("Bar");
        mnuBar.addActionListener(this);
                
        JMenu menuStyle = new JMenu("Style");
        menuStyle.add(mnuPie);
        menuStyle.add(mnuBar);
        
        JMenu menuChart = new JMenu("Chart");
        menuChart.add(menuTop);
        menuChart.add(menuStyle);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuChart);
        
        this.setJMenuBar(menuBar);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Data Methods
    ////////////////////////////////////////////////////////////////////////////
    protected void generateInitialChartData() {
        chartEvents = EventUtils.getRequiredEvents(events, chartData);
    }

    protected void showEventDetail(Event event) {

        eventsDetailTableModel.showDetail(event);
        
        if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
        eventDetailTextArea.setText(event.getRawJSON());
    }
       
    protected void reloadTable() {
        
        for (int i = defaultTableModel.getRowCount() -1; i>=0; i--) {
            defaultTableModel.removeRow(i);
        }
        
        if (chartEvents != null && chartEvents.size() > 0) {
            
            for (Entry entry : chartEvents) {
                defaultTableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
            }         
        } 
    } 
    
    protected void updateChart() {
        
        createChart(480, 160);
        tabs.remove(0);
        tabs.insertTab("Chart", null, chartPanel, "", 0);
        tabs.setSelectedIndex(0);
    }
    
    private void updateTextArea() {

        if (chartEvents != null) {

            int count = 0;

            StringBuilder dataString = new StringBuilder();
            for (Map.Entry entry : chartEvents) {

                if (count >= chartData.getTop()) {
                    break;
                }

                dataString.append(entry.getKey()).append(" : ").append(entry.getValue()).append(NEWLINE);
                count++;
            }

            tabbedTextArea.setText(dataString.toString());
        }
    }
    
    private void updateChartEvents() {
        
        generateInitialChartData();
        updateChart();
    }
}
