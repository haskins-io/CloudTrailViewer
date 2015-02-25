package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.filter.FreeformFilter;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *
 * @author mark.haskins
 */
public class ToolBarPanel extends JToolBar implements ActionListener, KeyListener {

    private final EventUtils eventUtils = new EventUtils();

    private final EventsDatabase eventsDatabase;

    private final Filters filters = new Filters();
    private final FreeformFilter freeFormFilter = new FreeformFilter();
    
    private final JTextField searchBox = new JTextField();

    public ToolBarPanel(EventsDatabase database) {

        eventsDatabase = database;

        filters.addEventFilter(freeFormFilter);

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
                    
                    freeFormFilter.setValue(searchCriteria);
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

    private void buildToolBar() {

        this.setFloatable(false);
        this.setLayout(new BorderLayout());

        // New Chart Button
        JButton btnNewChart = new JButton();
        btnNewChart.setActionCommand("NewChart");
        btnNewChart.setToolTipText("Add new Chart");
        btnNewChart.addActionListener(this);

        try {
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/chart-pie.png");
            btnNewChart.setIcon(new ImageIcon(imageUrl));
        }
        catch (Exception e) {
            btnNewChart.setText("New Chart");
        }

        // All Events Table
        JButton btnEvents = new JButton();
        btnEvents.setActionCommand("Events");
        btnEvents.setToolTipText("Show All Events");
        btnEvents.addActionListener(this);

        try {
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/table.gif");
            btnEvents.setIcon(new ImageIcon(imageUrl));
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
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/lock.png");
            btnSecurityScan.setIcon(new ImageIcon(imageUrl));
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
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/Search.png");
            JLabel searchIcon = new JLabel();
            searchIcon.setIcon(new ImageIcon(imageUrl));
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

                List<Map.Entry<String, Integer>> events = eventUtils.getRequiredEvents(eventsDatabase.getEvents(), chartData);

                ChartWindow chart = new ChartWindow(chartData, events);
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

            TableWindow window = new TableWindow("All Events", eventsDatabase.getEvents());
            window.setVisible(true);

            jCloudTrailViewer.DESKTOP.add(window);
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
        
        SecurityPanel securityPanel = new SecurityPanel();
        securityPanel.setVisible(true);

        jCloudTrailViewer.DESKTOP.add(securityPanel);
    }
}
