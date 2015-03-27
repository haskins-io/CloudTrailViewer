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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark.haskins
 */
public class TriDataPanel extends JPanel implements ActionListener {
    
    private final List<Event> masterEvents = new LinkedList<>();
    private List<Map.Entry<String, Integer>> chartEvents = new ArrayList<>();
    
    private ChartPanel chartPanel = null;
    private final ChartData chartData;
    
    private final DefaultTableModel defaultTableModel = new DefaultTableModel();   
    
    private final JTextArea tabbedTextArea = new JTextArea();
    private final JTabbedPane tabs = new JTabbedPane();
    
    public TriDataPanel(ChartData data) {
        chartData = data;
        buildDisplay();
    }
    
    public void setEvents(List<Event> events) {
        
        masterEvents.clear();
        masterEvents.addAll(events);
        
        updatePanel(5);
    }
    
    public JMenuBar getChartMenu() {
        
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
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildDisplay() {
        
        this.setLayout(new BorderLayout());
        
        createChart();
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
        chartData.setTop(top);
        updateChartEvents();
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
    
    private void updateChart() {
        
        createChart();
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
    
    private void generateInitialChartData() {
        chartEvents = EventUtils.getRequiredEvents(masterEvents, chartData);
    }
    
    private void createChart() {
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createTopPieChart(chartData.getTop(), chartEvents);
           
        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                chartData.getTop(),
                chartEvents, 
                chartData.getChartSource(), "Count",
                PlotOrientation.VERTICAL);
        } 
        
//        if (chartPanel != null) {
//            chartPanel.addChartMouseListener(this);
//        }
    }
}
