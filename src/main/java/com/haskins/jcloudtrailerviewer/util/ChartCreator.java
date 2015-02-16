package com.haskins.jcloudtrailerviewer.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import org.jfree.chart.ChartFactory;
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
 * @author mark.haskins
 */
public class ChartCreator {
    
    public static ChartPanel createPieChart(DefaultPieDataset dataset, int width, int height) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "",
            dataset,
            false,
            true,
            false
        );
        
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);
        plot.setInteriorGap(0.04);
        plot.setOutlineVisible(false);

        // use gradients and white borders for the section colours
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(2.0f));

        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        
        ChartPanel sourcePanel = new ChartPanel(chart);
        sourcePanel.setPreferredSize(new Dimension(width, height));
        
        return sourcePanel;
    }
    
    public static ChartPanel createLineChart(DefaultCategoryDataset dataset, int width, int height) {
        
        JFreeChart chart = ChartFactory.createLineChart(
            "",
            "",
            "",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
                
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
               
        return chartPanel;
    }
    
    public static ChartPanel createBarChart(DefaultCategoryDataset dataset, int width, int height, PlotOrientation orientation) {
        
        JFreeChart chart = ChartFactory.createBarChart(
            "", 
            "Drink", 
            "Share", 
            dataset,
            orientation, 
            false, 
            true, 
            false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
                        
        return chartPanel;
    }
}
