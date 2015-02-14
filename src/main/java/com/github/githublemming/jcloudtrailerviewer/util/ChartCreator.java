package com.github.githublemming.jcloudtrailerviewer.util;

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
    
    public static ChartPanel createPieChart(String title, DefaultPieDataset dataset, int width, int height) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            title,
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
        
        plot.setLabelGenerator(null);

        TextTitle t = chart.getTitle();
        t.setHorizontalAlignment(HorizontalAlignment.LEFT);
        t.setFont(new Font("Arial", Font.BOLD, 16));
        
        ChartPanel sourcePanel = new ChartPanel(chart);
        sourcePanel.setPreferredSize(new Dimension(width, height));
        
        return sourcePanel;
    }
    
    public static ChartPanel createLineChart(String title, DefaultCategoryDataset dataset, int width, int height) {
        
        JFreeChart chart = ChartFactory.createLineChart(
            title,
            "","",
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
    
    public static void updatePieChartDataset(String source, DefaultPieDataset dataset) {

        if (source != null) {
            int count = 1;

            try {
                Number existingCount = dataset.getValue(source);
                if (existingCount != null) {
                    count = existingCount.intValue() + 1;
                } 
            } catch (Exception e) {
                count = 1;
            }

            dataset.setValue(source, count);
        }
    }
    
    public static void updateLineChartDataset(String row, String source, DefaultCategoryDataset dataset) {
                
        if (source != null) {
            
            int count = 1;

            try {
                Number existingCount = dataset.getValue(row, source);
                if (existingCount != null) {
                    count = existingCount.intValue() + 1;
                } 
            } catch (Exception e) {
                count = 1;
            }

            dataset.setValue(count, row, source);
        }
    }
}
