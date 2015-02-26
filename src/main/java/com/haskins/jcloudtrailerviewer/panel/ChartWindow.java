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

import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark.haskins
 */
public class ChartWindow extends JInternalFrame implements ActionListener, ChartMouseListener {
    
    private final Filters filters = new Filters();
    
    private final List<Event> events;
    private List<Map.Entry<String, Integer>> chartEvents;
    
    private final ChartData chartData;
    private ChartPanel chartPanel = null;
    
    private final DefaultTableModel tableModel = new DefaultTableModel();   
    
    private final JTabbedPane tabs = new JTabbedPane();
    private final JTextArea dataTextArea = new JTextArea();
    
    private String chartSelect = null;
        
    public ChartWindow(ChartData chartData, List data) {
        
        super(chartData.getChartSource(), true, true, false, true);
        
        this.chartData = chartData;
        
        Object firstDataElement = data.get(0);
        if (firstDataElement instanceof Event) {
            
            events = data;
            chartEvents = null;
            
            generateInitialChartData();
        } else {
            
            events = null;
            chartEvents = data;
        }
        
        filters.addEventFilter(new FreeformFilter());
        
        buildGui();
        addTabs();
    }
       
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {

        if (cme.getTrigger().getClickCount() == 2) {
            
            Object object = cme.getEntity(); 
            ChartEntity cie = (ChartEntity)object; 

            if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

                PieSectionEntity categoryItemEntity = (PieSectionEntity)cie;
                chartSelect = categoryItemEntity.getSectionKey().toString();

            } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

                CategoryItemEntity categoryItemEntity = (CategoryItemEntity)cie;
                chartSelect = categoryItemEntity.getCategory().toString(); 
            }

            if (chartSelect != null) {

                filters.setFilterCriteria(chartSelect);
                List<Event> filteredEvents = filters.filterEvents(events);

                TableWindow window = new TableWindow("Filtered by : " + chartSelect, filteredEvents);
                window.setVisible(true);

                jCloudTrailViewer.DESKTOP.add(window);

                try {
                    window.setSelected(true);
                }
                catch (java.beans.PropertyVetoException pve) {
                }
            } 
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }

    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "Top5":
                changeTop(5);
                reloadTable();
                reloadData();
                break;
            case "Top10":
                changeTop(10);
                reloadTable();
                reloadData();
                break;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private Methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildGui() {
        
        tableModel.addColumn("Property");
        tableModel.addColumn("Value");
        
        this.setLayout(new BorderLayout());
        
        this.setTitle(chartData.getChartSource());
        this.setSize(500, 280);
        
        JMenuItem mnuTop5 = new JMenuItem("Top 5");
        mnuTop5.setActionCommand("Top5");
        mnuTop5.addActionListener(this);
        
        JMenuItem mnuTop10 = new JMenuItem("Top 10");
        mnuTop10.setActionCommand("Top10");
        mnuTop10.addActionListener(this);
        
        JMenuItem mnuIgnoreRoot = new JMenuItem("Ignore Root");
        mnuIgnoreRoot.setActionCommand("IgnoreRoot");
        mnuIgnoreRoot.addActionListener(this);
        
        JMenu menuDisplay = new JMenu("Display");
        menuDisplay.add(mnuTop5);
        menuDisplay.add(mnuTop10);
        menuDisplay.addSeparator();
        menuDisplay.add(mnuIgnoreRoot);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuDisplay);
        
        this.setJMenuBar(menuBar);
    }
    
    private void addTabs() {
        
        if ( (events != null && !events.isEmpty() ) || ( chartEvents != null && !chartEvents.isEmpty() )) {
            
            createChart();
            if (chartPanel != null) {
                tabs.addTab("Chart", chartPanel);
            }
            
            
            JTable table = new JTable(tableModel);
            table.setPreferredSize(new Dimension(480, 260));
            reloadTable();
            tabs.addTab("Table", table); 
            
            
            dataTextArea.setPreferredSize(new Dimension(600, 440));
            dataTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
            reloadData();
            tabs.addTab("Data", dataTextArea); 
            
            this.add(tabs, BorderLayout.CENTER);
        }
        else {
            this.add(new JLabel("No Data"), BorderLayout.CENTER);
        }
    }
    
    private void generateInitialChartData() {
        
        chartEvents = EventUtils.getRequiredEvents(events, chartData);
    }
    
    private void createChart() {
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createTopPieChart(chartData, chartEvents, 480, 260);
           
        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                    chartEvents, 
                    480, 260,
                    chartData.getChartSource(), "Count",
                    PlotOrientation.VERTICAL);
        } 
        
        if (chartPanel != null) {
            chartPanel.addChartMouseListener(this);
        }
    }
     
    private void reloadTable() {
        
        if (events != null) {
            
            // first clear down tablemodel
            for (int i = tableModel.getRowCount() -1; i>=0; i--) {
                tableModel.removeRow(i);
            }

            for (Entry entry : chartEvents) {
                tableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
            }            
        }
    }
    
    private void reloadData() {
        
        if (events != null) {
         
            String newline = "\n";

            StringBuilder dataString = new StringBuilder();
            for (Entry entry : chartEvents) {

                dataString.append(entry.getKey()).append(" : ").append(entry.getValue()).append(newline);
            }

            dataTextArea.setText(dataString.toString());
        }
    }
    
    private void changeTop(int newTop) {
        
        chartData.setTop(newTop);
        generateInitialChartData();
        createChart();
        tabs.remove(0);
        tabs.insertTab("Chart", null, chartPanel, "", 0);
        tabs.setSelectedIndex(0);
    }
}
