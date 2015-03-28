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

import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import static com.haskins.jcloudtrailerviewer.panel.AbstractInternalFrame.NEWLINE;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
public class TriDataPanel extends JPanel implements ActionListener, ChartMouseListener {
    
    protected final Filters filters = new Filters();
    
    private final List<Event> masterEvents = new LinkedList<>();
    private List<Map.Entry<String, Integer>> chartEvents = new ArrayList<>();
    
    private ChartPanel chartPanel = null;
    private final ChartData chartData;
    private String chartSelect = null;
    
    private final DefaultTableModel defaultTableModel = new DefaultTableModel();   
    
    JCheckBoxMenuItem mnuTop5 = new JCheckBoxMenuItem("Top 5");
    JCheckBoxMenuItem mnuTop10 = new JCheckBoxMenuItem("Top 10");
    
    JCheckBoxMenuItem mnuPie = new JCheckBoxMenuItem("Pie");
    JCheckBoxMenuItem mnuPie3d = new JCheckBoxMenuItem("Pie 3D");
    JCheckBoxMenuItem mnuBar = new JCheckBoxMenuItem("Bar");
    JCheckBoxMenuItem mnuBar3d = new JCheckBoxMenuItem("Bar 3d");
    
    private final JTextArea tabbedTextArea = new JTextArea();
    private final JTabbedPane tabs = new JTabbedPane();
    
    public TriDataPanel(ChartData data) {
        
        chartData = data;
        filters.addEventFilter(new FreeformFilter());
        
        buildDisplay();
    }
    
    public void setEvents(List<Event> events) {
        
        masterEvents.clear();
        masterEvents.addAll(events);
        
        updatePanel(5);
    }
    
    public JMenuBar getChartMenu() {
        
        mnuTop5.setActionCommand("Top5");
        mnuTop5.addActionListener(this);
        
        mnuTop10.setActionCommand("Top10");
        mnuTop10.addActionListener(this);
                
        JMenu menuTop = new JMenu("Top");
        menuTop.add(mnuTop5);
        menuTop.add(mnuTop10);
        
        
        mnuPie.setActionCommand("Pie");
        mnuPie.addActionListener(this);
        
        mnuPie3d.setActionCommand("Pie3d");
        mnuPie3d.addActionListener(this);
        
        mnuBar.setActionCommand("Bar");
        mnuBar.addActionListener(this);
        
        mnuBar3d.setActionCommand("Bar3d");
        mnuBar3d.addActionListener(this);
                
        JMenu menuStyle = new JMenu("Style");
        menuStyle.add(mnuPie);
        menuStyle.add(mnuPie3d);
        menuStyle.add(mnuBar);
        menuStyle.add(mnuBar3d);
        
        JMenu menuChart = new JMenu("Chart");
        menuChart.add(menuTop);
        menuChart.add(menuStyle);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuChart);
        
        return menuBar;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "Top5":
                mnuTop5.setState(true);
                mnuTop10.setState(false);
                updatePanel(5);
                break;
            case "Top10":
                mnuTop5.setState(false);
                mnuTop10.setState(true);
                updatePanel(10);
                break;
            case "Pie":
                mnuPie.setState(true);
                mnuBar.setState(false);
                mnuPie3d.setState(false);
                mnuBar3d.setState(false);
                chartData.setChartStyle("Pie");
                updateChartEvents(false);
                break;
            case "Bar":
                mnuPie.setState(false);
                mnuBar.setState(true);
                mnuPie3d.setState(false);
                mnuBar3d.setState(false);
                chartData.setChartStyle("Bar");
                updateChartEvents(false);
                break;
            case "Pie3d":
                mnuPie.setState(false);
                mnuBar.setState(false);
                mnuPie3d.setState(true);
                mnuBar3d.setState(false);
                chartData.setChartStyle("Pie");
                updateChartEvents(true);
                break;
            case "Bar3d":
                mnuPie.setState(false);
                mnuBar.setState(false);
                mnuPie3d.setState(false);
                mnuBar3d.setState(true);
                chartData.setChartStyle("Bar");
                updateChartEvents(true);
                break;
        }
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
                List<Event> filteredEvents = filters.filterEvents(masterEvents);

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
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildDisplay() {
        
        this.setLayout(new BorderLayout());
        
        if (chartData.getTop() == 5) {
            mnuTop5.setState(true);
        } else if (chartData.getTop() == 10) {
            mnuTop10.setState(true);
        }
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {
            mnuPie.setState(true);
        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {
            mnuBar.setState(true);
        }
        
        createChart(false);
        if (chartPanel != null) {
            tabs.addTab("Chart", chartPanel);
        }
                
        defaultTableModel.addColumn("Property");
        defaultTableModel.addColumn("Value");
        
        JTable table = new JTable(defaultTableModel);
        JScrollPane tablecrollPane = new JScrollPane(table);
        reloadTable();
        tabs.addTab("Table", tablecrollPane); 

        updateTextArea();
        JScrollPane tabbedDataScrollPane = new JScrollPane(tabbedTextArea);
        tabs.addTab("Data", tabbedDataScrollPane); 
        
        this.add(tabs, BorderLayout.CENTER);
    }
        
    private void updatePanel(int top) {
        
        boolean chart3d = false;
        if (mnuPie3d.getState() || mnuBar3d.getState()) {
            chart3d = true;
        }
        
        chartData.setTop(top);
        updateChartEvents(chart3d);
        reloadTable();
        updateTextArea();
    }
    
    private void reloadTable() {
        
        for (int i = defaultTableModel.getRowCount() -1; i>=0; i--) {
            defaultTableModel.removeRow(i);
        }
        
        if (chartEvents != null && chartEvents.size() > 0) {
            
            int count = 0;
            for (Map.Entry entry : chartEvents) {
                defaultTableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
                count++;
                
                if (count >= chartData.getTop()) {
                    break;
                }
            }         
        } 
    } 
    
    private void updateChart(boolean chart3d) {
        
        createChart(chart3d);
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
    
    private void updateChartEvents(boolean chart3d) {
        
        generateInitialChartData();
        updateChart(chart3d);
    }
    
    private void generateInitialChartData() {
        chartEvents = EventUtils.getRequiredEvents(masterEvents, chartData);
    }
    
    private void createChart(boolean chart3d) {
        
        if (mnuPie.getState() || mnuPie3d.getState()) {

            chartPanel = ChartCreator.createTopPieChart(
                chartData.getTop(), 
                chartEvents, 
                chart3d);
           
        } else if (mnuBar.getState() || mnuBar3d.getState()) {

            chartPanel = ChartCreator.createBarChart(
                chartData.getTop(),
                chartEvents, 
                chartData.getChartSource(), "Count",
                PlotOrientation.VERTICAL,
                chart3d);
        } 
        
        if (chartPanel != null) {
            chartPanel.addChartMouseListener(this);
        }
    }
}
