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

package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.model.ChartData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

/**
 * Factory class that creates JFreeCharts based on provided parameters.
 * 
 * @author mark.haskins
 */
public class ChartCreator {
    
    public static ChartPanel createChart(List<Entry<String,Integer>> events, ChartData chartData) {
        
        if (chartData.getChartStyle().contains("Pie")) {
            return ChartCreator.createPieChart(events, chartData);
        } else {
            return ChartCreator.createBarChart(events, chartData);
        } 
    }
    
    /**
     * Returns a Pie chart
     * @param events events to process
     * @return 
     */
    private static ChartPanel createPieChart(List<Entry<String,Integer>> events, ChartData chartData) {
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (Entry<String,Integer> event : events) {
            dataset.setValue(event.getKey(), event.getValue());
        }
        
        JFreeChart chart;
        if (chartData.getChartStyle().contains("3d")) {
            chart = ChartFactory.createPieChart3D(
                "", 
                dataset, 
                false, 
                true, 
                false
            );
        } else {
            chart = ChartFactory.createPieChart(
                "", 
                dataset, 
                false, 
                true, 
                false
            );
        }
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.10);
        plot.setOutlineVisible(false);

        // use gradients and white borders for the section colours
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));
        
        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        
        ChartPanel sourcePanel = new ChartPanel(chart);
        
        return sourcePanel;
    }
    
    /**
     * Returns a Bar chart
     * @param events events to include on chart
     * @return 
     */
    private static ChartPanel createBarChart(List<Entry<String,Integer>> events, ChartData chartData) {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();        

        for (Entry<String,Integer> event : events) {
            dataset.addValue(event.getValue().intValue(), event.getKey(), "");
        }
        
        JFreeChart chart;
        if (chartData.getChartStyle().contains("3d")) {
            chart = ChartFactory.createBarChart3D(
                "", 
                chartData.getChartSource(), 
                "", 
                dataset, 
                chartData.getOrientation(), 
                true, 
                true, 
                false
            );
        } else {
            chart = ChartFactory.createBarChart(
                "", 
                chartData.getChartSource(), 
                "", 
                dataset, 
                chartData.getOrientation(),
                true, 
                true, 
                false
            );
        }
                    
        return new ChartPanel(chart);
    }
    
    /**
     * returns a TimeSeries chart
     * @param data data to include on chart
     * @param width width of chart
     * @param height height of chart
     * @return 
     */
    public static ChartPanel createTimeSeriesChart(
        Map<String, 
        Map<String, Integer>> data, 
        int width, 
        int height) {
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        
        Set<String> dataKeys = data.keySet();
        Iterator<String> dataIterator = dataKeys.iterator();
        while(dataIterator.hasNext()) {
            
            String serviceKey = dataIterator.next();
            TimeSeries series = new TimeSeries(serviceKey);
            
            Map<String, Integer> serviceData = data.get(serviceKey);
            Set<String> serviceKeys = serviceData.keySet();
            Iterator<String> serviceIterator = serviceKeys.iterator();
            while(serviceIterator.hasNext()) {
                
                String dateTime = serviceIterator.next();
                int tps = serviceData.get(dateTime);
               
                RegularTimePeriod t = new FixedMillisecond(EventUtils.getTimestamp(dateTime));                   
                series.add(t, tps);     
            }
            
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "", "Time", "Value", dataset, true, true, false
        );
        chart.setBackgroundPaint(Color.white);
        
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);

        final NumberAxis rangeAxis2 = new NumberAxis("Range Axis 2");
        rangeAxis2.setAutoRangeIncludesZero(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
                        
        return chartPanel;
    }
}
