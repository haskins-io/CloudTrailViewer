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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark.haskins
 */
public class TriDataPanel extends JPanel implements ActionListener {
    
    private List<Event> events = new LinkedList<>();
    private List<Map.Entry<String, Integer>> chartEvents = new ArrayList<>();
    
    private ChartPanel chartPanel = null;
    private final ChartData chartData = new ChartData();
    
    private final DefaultTableModel defaultTableModel = new DefaultTableModel();   
    
    private final JTextArea tabbedTextArea = new JTextArea();
    private final JTabbedPane tabs = new JTabbedPane();
    
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
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private void reloadTable() {
        
        for (int i = defaultTableModel.getRowCount() -1; i>=0; i--) {
            defaultTableModel.removeRow(i);
        }
        
        if (chartEvents != null && chartEvents.size() > 0) {
            
            for (Map.Entry entry : chartEvents) {
                defaultTableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
            }         
        } 
    } 
    
    private void updateChart() {
        
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
    
    private void generateInitialChartData() {
        chartEvents = EventUtils.getRequiredEvents(events, chartData);
    }
    
    private void createChart(int width, int height) {
        
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
        
//        if (chartPanel != null) {
//            chartPanel.addChartMouseListener(this);
//        }
    }
}
