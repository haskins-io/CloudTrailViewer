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
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
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
public class ChartWindow extends JInternalFrame implements ChartMouseListener {
    
    private final List<Entry<String,Integer>> events;
    private final Map<String, Map<String, Integer>> mapData;
    
    private final ChartData chartData;
    
    private String chartSelect = null;
    
    private final Filters filters = new Filters();
    private final FreeformFilter freeFormFilter = new FreeformFilter();
    
    public ChartWindow(ChartData chartData, List<Entry<String,Integer>> data) {
        
        super(chartData.getChartSource(), true, true, false, true);
        
        this.chartData = chartData;
        
        events = data;
        mapData = null;
        
        filters.addEventFilter(freeFormFilter);
        
        buildGui();
    }
    
    public ChartWindow(ChartData chartData, Map<String, Map<String, Integer>> data) {
        
        super("", true, true, false, true);
        
        this.chartData = chartData;
        
        events = null;
        mapData = data;
        
        buildGui();
    }
    
    private void buildGui() {
        
        this.setLayout(new BorderLayout());
        
        this.setTitle(chartData.getChartSource());
        this.setSize(500, 280);
        
        if ((events != null && !events.isEmpty()) || (mapData != null && !mapData.isEmpty())) {
            
            JTabbedPane tabs = new JTabbedPane();
            
            addChart(tabs);
            addTable(tabs);
            addData(tabs);
            
            this.add(tabs, BorderLayout.CENTER);
        }
        else {
            this.add(new JLabel("No Data"), BorderLayout.CENTER);
        }
    }
    
    private void addChart(JTabbedPane panel) {
                
        ChartPanel chartPanel = null;
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createPieChart(events, 480, 260);

        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                    events, 
                    480, 260,
                    chartData.getChartSource(), "Count",
                    PlotOrientation.VERTICAL);
            
        } else if (chartData.getChartStyle().equalsIgnoreCase("TimeSeries")) {

            chartPanel = ChartCreator.createTimeSeriesChart("Transactions Per Second", mapData);
        }
        
        if (chartPanel != null) {

            panel.addTab("Chart", chartPanel);
        }
    }
    
    private void addTable(JTabbedPane panel) {
        
        if (events != null) {
            
            DefaultTableModel tableModel = new DefaultTableModel();

            JTable table = new JTable(tableModel);
            table.setPreferredSize(new Dimension(480, 260));

            tableModel.addColumn("Property");
            tableModel.addColumn("Value");

            for (Entry entry : events) {
                tableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
            }

            panel.addTab("Table", table); 
        }
    }
    
    private void addData(JTabbedPane panel) {
        
        if (events != null) {
        
            String newline = "\n";

            JTextArea dataTextArea = new JTextArea();
            dataTextArea.setPreferredSize(new Dimension(600, 440));
            dataTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));

            StringBuilder dataString = new StringBuilder();
            for (Entry entry : events) {

                dataString.append(entry.getKey()).append(" : ").append(entry.getValue()).append(newline);
            }

            dataTextArea.setText(dataString.toString());

            panel.addTab("Data", dataTextArea);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {

        Object object = cme.getEntity(); 
        ChartEntity cie = (ChartEntity)object; 

        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            PieSectionEntity categoryItemEntity = (PieSectionEntity)cie;
            chartSelect = categoryItemEntity.getSectionKey().toString();

        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            CategoryItemEntity categoryItemEntity = (CategoryItemEntity)cie;
            chartSelect = categoryItemEntity.getCategory().toString(); 
        }
        
//        if (chartSelect != null) {
//            
//            freeFormFilter.setValue(chartSelect);
//            List<Event> filteredEvents = filters.filterEvents(eventsDatabase.getEvents());
//
//            TableWindow window = new TableWindow("Filtered by : " + chartSelect, filteredEvents);
//            window.setVisible(true);
//
//            jCloudTrailViewer.DESKTOP.add(window);
//
//            try {
//                window.setSelected(true);
//            }
//            catch (java.beans.PropertyVetoException pve) {
//            }
//        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }
}
