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

import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JLabel;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;

/**
 *
 * @author mark.haskins
 */
public class ChartWindow extends AbstractInternalFrame {
    
    private String chartSelect = null;
        
    public ChartWindow(ChartData chartData, List data) {
        
        super(chartData.getChartSource());
        
        this.chartData = chartData;
        
        Object firstDataElement = data.get(0);
        if (firstDataElement instanceof Event) {
            
            events = data;
            chartEvents = null;
            generateInitialChartData();
            
        } else {
            
            events = null;
            chartEvents = data;
        }
        
        filters.addEventFilter(new FreeformFilter());
        
        buildGui();
        
        if ( (events != null && !events.isEmpty() ) || ( chartEvents != null && !chartEvents.isEmpty() )) {
            addTabbedChartDetail(480, 160);
}
        else {
            this.add(new JLabel("No Data"), BorderLayout.CENTER);
        }
    }
       
    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void chartMouseClicked(ChartMouseEvent cme) {

        if (cme.getTrigger().getClickCount() == 2) {
            
            Object object = cme.getEntity(); 
            ChartEntity cie = (ChartEntity)object; 

            if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

                PieSectionEntity categoryItemEntity = (PieSectionEntity)cie;
                chartSelect = categoryItemEntity.getSectionKey().toString();

            } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

                CategoryItemEntity categoryItemEntity = (CategoryItemEntity)cie;
                chartSelect = categoryItemEntity.getCategory().toString(); 
            }

            if (chartSelect != null && events != null) {

                filters.setFilterCriteria(chartSelect);
                List<Event> filteredEvents = filters.filterEvents(events);

                TableWindow window = new TableWindow("Filtered by : " + chartSelect, filteredEvents);
                window.setVisible(true);

                jCloudTrailViewer.DESKTOP.add(window);

                try {
                    window.setSelected(true);
                }
                catch (java.beans.PropertyVetoException pve) {
                }
            } 
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent cme) { }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private Methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildGui() {
        
        this.setSize(500, 280);
        
        addTopXmenu();
        
        this.add(tabs, BorderLayout.CENTER);
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
