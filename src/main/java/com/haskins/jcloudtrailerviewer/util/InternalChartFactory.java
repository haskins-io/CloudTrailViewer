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
