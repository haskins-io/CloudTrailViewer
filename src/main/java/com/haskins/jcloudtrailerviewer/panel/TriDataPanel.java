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
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
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
    
    JRadioButtonMenuItem mnuTop5 = new JRadioButtonMenuItem("Top 5");
    JRadioButtonMenuItem mnuTop10 = new JRadioButtonMenuItem("Top 10");
    
    JRadioButtonMenuItem mnuPie = new JRadioButtonMenuItem("Pie");
    JRadioButtonMenuItem mnuPie3d = new JRadioButtonMenuItem("Pie 3D");
    JRadioButtonMenuItem mnuBar = new JRadioButtonMenuItem("Bar");
    JRadioButtonMenuItem mnuBar3d = new JRadioButtonMenuItem("Bar 3d");
    
    private final JTextArea tabbedTextArea = new JTextArea();
    private final JTabbedPane tabs = new JTabbedPane();
    
    public TriDataPanel(ChartData data, boolean showMenu) {
        
        chartData = data;
        filters.addEventFilter(new FreeformFilter());
        
        buildDisplay(showMenu);
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
                
        ButtonGroup topGroup = new ButtonGroup();
        topGroup.add(mnuTop5);
        topGroup.add(mnuTop10);
        

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
        
        ButtonGroup styleGroup = new ButtonGroup();
        styleGroup.add(mnuPie);
        styleGroup.add(mnuPie3d);
        styleGroup.add(mnuBar);
        styleGroup.add(mnuBar3d);
                
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
                updatePanel(5);
                break;
            case "Top10":
                updatePanel(10);
                break;
            case "Pie":
                updateChartEvents("Pie", false);
                break;
            case "Bar":;
                updateChartEvents("Bar", false);
                break;
            case "Pie3d":
                updateChartEvents("Pie", true);
                break;
            case "Bar3d":
                updateChartEvents("Bar", true);
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
    private void buildDisplay(boolean showMenu) {
        
        this.setLayout(new BorderLayout());
        
        selectInitialMenus();
        
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
        
        if (showMenu) {
            this.add(getChartMenu(), BorderLayout.NORTH);
        }
        
        this.add(tabs, BorderLayout.CENTER);
    }
    
    private void selectInitialMenus() {
        
        // TOP
        if (chartData.getTop() == 5) {
            mnuTop5.setSelected(true);
        } else if (chartData.getTop() == 10) {
            mnuTop10.setSelected(true);
        }
        
        // STYLE
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {
            mnuPie.setSelected(true);
        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {
            mnuBar.setSelected(true);
        } 
        
        // PROPERTY
        
    }
        
    private void updatePanel(int top) {
        
        boolean chart3d = false;
        if (mnuPie3d.isSelected() || mnuBar3d.isSelected()) {
            chart3d = true;
        }
        
        chartData.setTop(top);
        updateChartEvents(chartData.getChartStyle(), chart3d);
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
    
    private void updateChartEvents(String style, boolean chart3d) {
        
        chartData.setChartStyle(style);
        
        generateInitialChartData();
        updateChart(chart3d);
    }
    
    private void generateInitialChartData() {
        chartEvents = EventUtils.getRequiredEvents(masterEvents, chartData);
    }
    
    private void createChart(boolean chart3d) {
        
        if (mnuPie.isSelected() || mnuPie3d.isSelected()) {

            chartPanel = ChartCreator.createTopPieChart(
                chartData.getTop(), 
                chartEvents, 
                chart3d);
           
        } else if (mnuBar.isSelected() || mnuBar3d.isSelected()) {

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
