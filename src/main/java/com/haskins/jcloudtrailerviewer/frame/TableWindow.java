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

package com.haskins.jcloudtrailerviewer.frame;

import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.components.ChartDialog;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.util.EventTimestampComparator;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Class that represents a Internal Frame Windows that shows a table.
 * 
 * @author mark.haskins
 */
public class TableWindow extends AbstractInternalFrame {
            
    /**
     * Default Constructor
     * @param title String to be used as title for Window
     * @param masterEvents collection of events to be shown in table.
     */
    public TableWindow(String title, List<Event> masterEvents) {

        super(title);
        
        events.addAll(masterEvents);
        
        Collections.sort(events, new EventTimestampComparator());
        
        eventsTableModel.setData(events);
        
        buildUI();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        this.setSize(640, 480);

        final EventsTable table = new EventsTable(eventsTableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = eventsTableModel.getEventAt(table.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });

        table.setVisible(true);

        JButton btnShowChart = new JButton();
        btnShowChart.setActionCommand("NewChart");
        btnShowChart.setToolTipText("Show Chart");

        try {
            ClassLoader cl = this.getClass().getClassLoader();
            btnShowChart.setIcon(new ImageIcon(cl.getResource("icons/chart-pie.png")));
        }
        catch (Exception e) {
            btnShowChart.setText("Show Chart");
        }

        btnShowChart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createAndShowChart();
            }
        });

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(btnShowChart);
        
        JScrollPane eventsScrollPane = new JScrollPane(table);
        JSplitPane split = new JSplitPane();
        split.add(eventsScrollPane, JSplitPane.LEFT);
        split.add(getEventDetailPanel(), JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(10);
        split.setAutoscrolls(false);
        split.setDividerLocation(this.getWidth() - 300);
        split.setResizeWeight(1.0);
        
        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void createAndShowChart() {

        chartData = ChartDialog.showDialog(jCloudTrailViewer.DESKTOP);

        if (chartData != null) {

            ChartWindow chart = new ChartWindow(chartData, events);
            chart.setVisible(true);

            jCloudTrailViewer.DESKTOP.add(chart);

            try {
                chart.setSelected(true);
            }
            catch (java.beans.PropertyVetoException e) {
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) { }
    
    @Override
    public void finishedLoading() { }
    
    @Override
    public void newMessage(String message) { }
}
