package com.haskins.jcloudtrailerviewer.util;

import com.haskins.jcloudtrailerviewer.model.ChartData;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 *
 * @author mark.haskins
 */
public class InternalChartFactory {

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    
    public static JInternalFrame createChart(ChartData chartData, List<Entry<String,Integer>> events) {
        
        JInternalFrame frame = new JInternalFrame(
                chartData.getChartSource(),
                true, //resizable
                true, //closable
                false, //maximizable
                true
        );

        frame.setTitle(chartData.getChartSource());
        frame.setSize(640, 480);
        frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);

        if (!events.isEmpty()) {
            
            addChart(chartData, events, frame);
            
        } else {
            
            frame.add(new JLabel("No Data"));
            
        }
        return frame;
    }
    
    private static void addChart(ChartData chartData, List<Entry<String,Integer>> events, JInternalFrame frame) {
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {
            frame.add(ChartCreator.createPieChart(events, 620, 460));
        } 
    }
}
