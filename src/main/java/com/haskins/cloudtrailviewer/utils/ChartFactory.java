/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;

/**
 *
 * @author mark
 */
public class ChartFactory {
    
    public static ChartPanel createChart(String type, List<Entry<String,Integer>> events, int width, int height, PlotOrientation orientation) {
        
        if (type.contains("Pie")) {
            return ChartFactory.createPieChart(type, events, width, height);
        } else {
            return ChartFactory.createBarChart(type, events, width, height, orientation);
        } 
    }
    
    private static ChartPanel createPieChart(String type, List<Entry<String,Integer>> events, int width, int height) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (Map.Entry<String,Integer> event : events) {
            dataset.setValue(event.getKey(), event.getValue());
        }
        
        JFreeChart jFChart;
        if (type.contains("3d")) {
            jFChart = org.jfree.chart.ChartFactory.createPieChart3D(
                "", 
                dataset, 
                false, 
                true, 
                false
            );
        } else {
            jFChart = org.jfree.chart.ChartFactory.createPieChart(
                "", 
                dataset, 
                false, 
                true, 
                false
            );
        }
        
        PiePlot plot = (PiePlot) jFChart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.01);
        plot.setOutlineVisible(false);
        plot.setLabelGenerator(null);

        // use gradients and white borders for the section colours
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));
        
        TextTitle t = jFChart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        
        final ChartPanel jChartPanel =  new ChartPanel(jFChart, width, height, width, height, width, height, false, false, true, true, false, true);  
        jChartPanel.setMinimumDrawWidth(0);
        jChartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        jChartPanel.setMinimumDrawHeight(0);
        jChartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        
        return jChartPanel;
    }
    
    /**
     * Returns a Bar chart
     * @param events events to include on chart
     * @return 
     */
    private static ChartPanel createBarChart(String type, List<Entry<String,Integer>> events, int width, int height, PlotOrientation orientation) {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();        

        for (Entry<String,Integer> event : events) {
            dataset.addValue(event.getValue().intValue(), event.getKey(), "");
        }
        
        JFreeChart jFChart;
        if (type.contains("3d")) {
            jFChart = org.jfree.chart.ChartFactory.createBarChart3D(
                "", 
                "", 
                "", 
                dataset, 
                orientation, 
                false, 
                true, 
                false
            );
        } else {
            jFChart = org.jfree.chart.ChartFactory.createBarChart(
                "", 
                "", 
                "", 
                dataset, 
                orientation,
                false, 
                true, 
                false
            );
        }
                            
        final ChartPanel jChartPanel =  new ChartPanel(jFChart, width, height, width, height, width, height, false, false, true, true, false, true);  
        jChartPanel.setMinimumDrawWidth(0);
        jChartPanel.setMaximumDrawWidth(Integer.MAX_VALUE);
        jChartPanel.setMinimumDrawHeight(0);
        jChartPanel.setMaximumDrawHeight(Integer.MAX_VALUE);
        
        return jChartPanel;
    }
}
