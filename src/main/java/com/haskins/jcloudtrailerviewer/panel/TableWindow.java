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

import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import com.haskins.jcloudtrailerviewer.util.EventTimestampComparator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author mark.haskins
 */
public class TableWindow extends JInternalFrame
{

    private final EventsTableModel tableModel;
    private final EventDetailTableModel detailTableModel = new EventDetailTableModel();

    private final JTextArea rawJsonPanel = new JTextArea();
    private final JTextField searchBox = new JTextField();
    
    private final Filters filters = new Filters();
    private final FreeformFilter freeFormFilter = new FreeformFilter();

    private final EventUtils eventFilter = new EventUtils();
    
    private final List<Event> events = new LinkedList<>();
    
    public TableWindow(String title, List<Event> masterEvents) {

        super(title, true, true, false, true);
        
        events.addAll(masterEvents);
        
        Collections.sort(events, new EventTimestampComparator());
        
        tableModel = new EventsTableModel();
        tableModel.setData(events);
        
        filters.addEventFilter(freeFormFilter);

        buildUI();
    }

    private void buildUI() {

        this.setLayout(new BorderLayout());

        this.setSize(640, 480);

        final EventsTable table = new EventsTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = tableModel.getEventAt(table.getSelectedRow());
                    showEventDetail(event);
                }
            }
        });

        table.setVisible(true);

        JButton btnShowChart = new JButton();
        btnShowChart.setActionCommand("NewChart");
        btnShowChart.setToolTipText("Show Chart");

        try {
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/chart-pie.png");
            btnShowChart.setIcon(new ImageIcon(imageUrl));
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
        
        
        EventDetailTable detailTable = new EventDetailTable(detailTableModel);
        JScrollPane eventDetailScrollPane = new JScrollPane(detailTable);
        JScrollPane rawJsonScrollPane = new JScrollPane(rawJsonPanel);

        JTabbedPane detailPanel = new JTabbedPane();
        detailPanel.add("Table View", eventDetailScrollPane);
        detailPanel.add("Raw View", rawJsonScrollPane);

        JScrollPane eventsScrollPane = new JScrollPane(table);
        JSplitPane split = new JSplitPane();
        split.add(eventsScrollPane, JSplitPane.LEFT);
        split.add(detailPanel, JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(2);
        split.setAutoscrolls(false);
        split.setDividerLocation(400);

        rawJsonPanel.setFont(new Font("Verdana", Font.PLAIN, 12));

        JPanel statusBar = new JPanel();
        statusBar.add(new JLabel("Events : " + events.size()));
        
        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void showEventDetail(Event event) {

        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }

    private void createAndShowChart() {

        ChartData chartData = ChartDialog.showDialog(jCloudTrailViewer.DESKTOP);

        if (chartData != null) {

            List<Map.Entry<String, Integer>> filteredEvents = eventFilter.getRequiredEvents(events, chartData);

            ChartWindow chart = new ChartWindow(chartData, filteredEvents);
            chart.setVisible(true);

            jCloudTrailViewer.DESKTOP.add(chart);

            try {
                chart.setSelected(true);
            }
            catch (java.beans.PropertyVetoException e) {
            }
        }
    }
}
