/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventLoaderListener;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.MenuDefinition;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark
 */
public class ScanChartPanel extends JInternalFrame implements ActionListener, EventLoaderListener {

    private final EventLoader eventLoader = new EventLoader();

    private final MenuDefinition menuDefinition;

    private final Map<String, Integer> eventMap = new HashMap<>();
    private List<Map.Entry<String, Integer>> chartEvents;

    private final String chartType = "Pie";
    private int chartTop = 5;
    private ChartPanel chartPanel = null;

    private final JTabbedPane tabs = new JTabbedPane();
    private final JTextArea dataTextArea = new JTextArea();

    private final String newline = "\n";

    public ScanChartPanel(MenuDefinition menuDef) {

        super(menuDef.getName(), true, true, false, true);

        menuDefinition = menuDef;

        eventLoader.addListener(this);

        Object[] options = {"Local Files", "S3 Files"};
        int i = JOptionPane.showOptionDialog(
            jCloudTrailViewer.DESKTOP,
            "Do you want to scan local files or remote files",
            "Choose File location",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (i == 0) {
            eventLoader.showFileBrowser();
            buildUI();
        }
        else if (i == 1) {
            eventLoader.showS3Browser();
            buildUI();
        }
        else {
            this.dispose();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {

        for (Event event : events) {

            // get required property value and store in map with count
            String value = EventUtils.getEventProperty(menuDefinition.getProperty(), event);
            if (value != null) {

                int count = 1;
                if (eventMap.containsKey(value)) {
                    count = eventMap.get(value) + 1;
                }

                eventMap.put(value, count);
            }
        }
    }

    @Override
    public void finishedLoading() {

        chartEvents = EventUtils.entriesSortedByValues(eventMap);

        createChart();
        addTabs();

        this.validate();
    }

    @Override
    public void newMessage(String message) {
    }

    ////////////////////////////////////////////////////////////////////////////
    // ChartMouseListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {

        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case "Top5":
                changeTop(5);
                reloadData();
                break;
            case "Top10":
                changeTop(10);
                reloadData();
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {

        this.setLayout(new BorderLayout());

        this.setTitle(menuDefinition.getName());
        this.setSize(500, 280);

        JMenuItem mnuTop5 = new JMenuItem("Top 5");
        mnuTop5.setActionCommand("Top5");
        mnuTop5.addActionListener(this);

        JMenuItem mnuTop10 = new JMenuItem("Top 10");
        mnuTop10.setActionCommand("Top10");
        mnuTop10.addActionListener(this);

        JMenuItem mnuIgnoreRoot = new JMenuItem("Ignore Root");
        mnuIgnoreRoot.setActionCommand("IgnoreRoot");
        mnuIgnoreRoot.addActionListener(this);

        JMenu menuDisplay = new JMenu("Display");
        menuDisplay.add(mnuTop5);
        menuDisplay.add(mnuTop10);
        menuDisplay.addSeparator();
        menuDisplay.add(mnuIgnoreRoot);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuDisplay);

        this.setJMenuBar(menuBar);
        
        StatusBarPanel statusBarPanel = new StatusBarPanel();
        eventLoader.addListener(statusBarPanel);

        this.add(statusBarPanel, BorderLayout.SOUTH);
    }

    private void addTabs() {

        if (chartEvents != null && !chartEvents.isEmpty()) {

            createChart();
            if (chartPanel != null) {
                tabs.addTab("Chart", chartPanel);
            }

            dataTextArea.setPreferredSize(new Dimension(600, 440));
            dataTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
            reloadData();
            tabs.addTab("Data", dataTextArea);

            this.add(tabs, BorderLayout.CENTER);
        }
        else {
            this.add(new JLabel("No Data"), BorderLayout.CENTER);
        }
    }

    private void createChart() {

        if (chartType.equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createTopPieChart(chartTop, chartEvents, 480, 260);

        }
        else if (chartType.equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                chartEvents,
                480, 260,
                menuDefinition.getName(), "Count",
                PlotOrientation.VERTICAL);
        }
    }

    private void reloadData() {

        if (chartEvents != null) {

            int count = 0;

            StringBuilder dataString = new StringBuilder();
            for (Map.Entry entry : chartEvents) {

                if (count >= chartTop) {
                    break;
                }

                dataString.append(entry.getKey()).append(" : ").append(entry.getValue()).append(newline);
                count++;
            }

            dataTextArea.setText(dataString.toString());
        }
    }

    private void changeTop(int newTop) {

        chartTop = newTop;
        chartEvents = EventUtils.entriesSortedByValues(eventMap);
        createChart();
        tabs.remove(0);
        tabs.insertTab("Chart", null, chartPanel, "", 0);
        tabs.setSelectedIndex(0);
    }
}
