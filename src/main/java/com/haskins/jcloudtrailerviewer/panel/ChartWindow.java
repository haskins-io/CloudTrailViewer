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

import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author mark.haskins
 */
public class ChartWindow extends AbstractInternalFrame {
        
    private final TriDataPanel triPanel;
        
    public ChartWindow(ChartData chartData, List data) {
        
        super(chartData.getChartSource());
        
        triPanel = new TriDataPanel(chartData, false);
        
        events = data;
        
        buildGui();
        
        if (events != null && !events.isEmpty()) {
            triPanel.setEvents(events);
}
        else {
            this.add(new JLabel("No Data"), BorderLayout.CENTER);
        }
    }
        
    ////////////////////////////////////////////////////////////////////////////
    // Private Methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildGui() {
        
        this.setSize(500, 280);
        
        this.setJMenuBar(triPanel.getChartMenu());
        this.add(triPanel, BorderLayout.CENTER);
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> newEvents) { }
    
    @Override
    public void finishedLoading() { }
    
    @Override
    public void newMessage(String message) { }
}
