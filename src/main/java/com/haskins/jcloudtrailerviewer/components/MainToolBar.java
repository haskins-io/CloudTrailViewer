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


package com.haskins.jcloudtrailerviewer.components;

import com.haskins.jcloudtrailerviewer.frame.SecurityWindow;
import com.haskins.jcloudtrailerviewer.frame.TableWindow;
import com.haskins.jcloudtrailerviewer.frame.CombinedWindow;
import com.haskins.jcloudtrailerviewer.frame.ChartWindow;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *  Provides the toolbar that is attached to the JDesktop.
 * 
 * @author mark.haskins
 */
public class MainToolBar extends JToolBar implements ActionListener, KeyListener {

    private final EventsDatabase eventsDatabase;

    private final Filters filters = new Filters();
    
    private final JTextField searchBox = new JTextField();

    /**
     * Default Constructor.
     * @param database an Events Database instance.
     */
    public MainToolBar(EventsDatabase database) {

        eventsDatabase = database;

        addRequiredFilters();
        buildToolBar();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // KeyListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!eventsDatabase.getEvents().isEmpty()) {

                String searchCriteria = searchBox.getText().trim();
                if (searchCriteria.length() > 0) {
                    
                    filters.setFilterCriteria(searchCriteria);
                    List<Event> events = filters.filterEvents(eventsDatabase.getEvents());

                    TableWindow window = new TableWindow("Filtered by : " + searchCriteria, events);
                    window.setVisible(true);

                    jCloudTrailViewer.DESKTOP.add(window);

                    try {
                        window.setSelected(true);
                    }
                    catch (java.beans.PropertyVetoException pve) {
                    }
                }
            }
            else {

                JOptionPane.showMessageDialog(
                    jCloudTrailViewer.DESKTOP,
                    "No Events Loaded!",
                    "Data Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }


    @Override
    public void keyReleased(KeyEvent e) { }
    
    @Override
    public void keyTyped(KeyEvent e) { }

    ////////////////////////////////////////////////////////////////////////////
    // KeyListener
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "NewChart":
                createChart();
                break;
            case "Events":
                createAllEventsTable();
                break;
            case "SecurityScan":
                securityScan();
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void addRequiredFilters() {
        filters.addEventFilter(new FreeformFilter());
    }
    
    private void buildToolBar() {

        this.setFloatable(false);
        this.setLayout(new BorderLayout());

        // New Chart Button
        JButton btnNewChart = new JButton();
        btnNewChart.setActionCommand("NewChart");
        btnNewChart.setToolTipText("Add new Chart");
        btnNewChart.addActionListener(this);

        ClassLoader cl = this.getClass().getClassLoader();
        
        try {
            
            btnNewChart.setIcon(new ImageIcon(cl.getResource("icons/chart-pie.png")));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            btnNewChart.setText("New Chart");
        }

        // All Events Table
        JButton btnEvents = new JButton();
        btnEvents.setActionCommand("Events");
        btnEvents.setToolTipText("Show All Events");
        btnEvents.addActionListener(this);

        try {
            btnEvents.setIcon(new ImageIcon(cl.getResource("icons/table.gif")));
        }
        catch (Exception e) {
            btnEvents.setText("Events");
        }

        // Security Scan
        JButton btnSecurityScan = new JButton();
        btnSecurityScan.setActionCommand("SecurityScan");
        btnSecurityScan.setToolTipText("Security Scan");
        btnSecurityScan.addActionListener(this);

        try {
            btnSecurityScan.setIcon(new ImageIcon(cl.getResource("icons/lock.png")));
        }
        catch (Exception e) {
            btnSecurityScan.setText("Security Scan");
        }
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(btnNewChart);
        buttonsPanel.add(btnEvents);
        buttonsPanel.add(btnSecurityScan);

        this.add(buttonsPanel, BorderLayout.WEST);
        this.add(new JPanel(), BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setPreferredSize(new Dimension(250, 20));

        try {
            JLabel searchIcon = new JLabel();
            searchIcon.setIcon(new ImageIcon(cl.getResource("icons/Search.png")));
            searchPanel.add(searchIcon, BorderLayout.WEST);
        }
        catch (Exception e) {
        }
        
        searchBox.setSize(220, 20);
        searchBox.addKeyListener(this);

        searchPanel.add(searchBox, BorderLayout.CENTER);

        this.add(searchPanel, BorderLayout.EAST);
    }

    private void createChart() {
        
        if (!eventsDatabase.getEvents().isEmpty()) {

            ChartData chartData = ChartDialog.showDialog(jCloudTrailViewer.DESKTOP);

            if (chartData != null) {

                ChartWindow chart = new ChartWindow(chartData, eventsDatabase.getEvents());
                chart.setVisible(true);

                jCloudTrailViewer.DESKTOP.add(chart);
            }
        }
        else {

            JOptionPane.showMessageDialog(
                jCloudTrailViewer.DESKTOP,
                "No Events Loaded!",
                "Data Error",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void createAllEventsTable() {
        
        if (!eventsDatabase.getEvents().isEmpty()) {

            CombinedWindow window = new CombinedWindow("All Events", eventsDatabase.getEvents(), null);
            window.setVisible(true);

            jCloudTrailViewer.DESKTOP.add(window);
            
            try {
                window.setSelected(true);
            }
            catch (java.beans.PropertyVetoException pve) {
            }
        }
        else {

            JOptionPane.showMessageDialog(
                jCloudTrailViewer.DESKTOP,
                "No Events Loaded!",
                "Data Error",
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void securityScan() {
        
        SecurityWindow securityPanel = new SecurityWindow();
        securityPanel.setVisible(true);

        jCloudTrailViewer.DESKTOP.add(securityPanel);
        
        try {
            securityPanel.setSelected(true);
        }
        catch (java.beans.PropertyVetoException pve) {
        }
    }
}
