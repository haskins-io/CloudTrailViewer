package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 *
 * @author mark.haskins
 */
public class ChartCreator {
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    
    public static ChartPanel createPieChart(
            List<Entry<String,Integer>> events, 
            int width, int height) {
        
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        int count = 5;
        if (events.size() < 5) {
            count = events.size();
        }
        
        for (int i=0; i<count; i++) {
            Entry<String,Integer> obj = events.get(i);
            dataset.setValue(obj.getKey(), obj.getValue());
        }
        
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
    
    public static ChartPanel createLineChart(
            List<Entry<String,Integer>> events, 
            int width, int height) {
        
        JFreeChart chart = ChartFactory.createLineChart(
            "",
            "",
            "",
            null,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
                
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
               
        return chartPanel;
    }
    
    public static ChartPanel createTimeSeriesChart(String title, Map<String, Map<String, Integer>> data) {
        
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
                
                try {
                    // create a date object
                    RegularTimePeriod t = new FixedMillisecond(sdf.parse(dateTime).getTime());                   
                    series.add(t, tps);
                    
                } catch (ParseException ex) {
                    Logger.getLogger(EventsDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title, "Time", "Value", dataset, true, true, false
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
        chartPanel.setPreferredSize(new Dimension(320, 240));
                        
        return chartPanel;
    }
    
    public static ChartPanel createBarChart(
            List<Entry<String,Integer>> events, 
            int width, int height, 
            String xLabel, String yLabel,
            PlotOrientation orientation) {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        int count = 5;
        if (events.size() < 5) {
            count = events.size();
        }
        
        for (int i=0; i<count; i++) {
            Entry<String,Integer> obj = events.get(i);
            dataset.addValue(obj.getValue().intValue(), obj.getKey(), "");
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "", 
            xLabel, 
            yLabel, 
            dataset,
            orientation, 
            true, 
            true, 
            false
        );
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(width, height));
                        
        return chartPanel;
    }
}
