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
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *
 * @author mark.haskins
 */
public class ToolBarPanel extends JToolBar {

    private final EventUtils eventFilter = new EventUtils();

    private final EventsDatabase eventsDatabase;
    private final JDesktopPane desktop;

    private final Filters filters = new Filters();
    private final FreeformFilter freeFormFilter = new FreeformFilter();

    public ToolBarPanel(EventsDatabase database, JDesktopPane jdesktop) {

        eventsDatabase = database;
        desktop = jdesktop;

        filters.addEventFilter(freeFormFilter);

        buildToolBar();
    }

    private void buildToolBar() {

        this.setLayout(new BorderLayout());

        JButton btnNewChart = new JButton("New Chart");
        btnNewChart.setActionCommand("NewChart");
        btnNewChart.setToolTipText("Add new Chart");

        try {
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/chart-pie.png");
            btnNewChart.setIcon(new ImageIcon(imageUrl));
        }
        catch (Exception e) {
        }

        btnNewChart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!eventsDatabase.getEvents().isEmpty()) {

                    createAndShowChart();

                }
                else {

                    JOptionPane.showMessageDialog(
                        desktop,
                        "No Events Loaded!",
                        "Data Error",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton btnEvents = new JButton("Events");
        btnEvents.setActionCommand("Events");
        btnEvents.setToolTipText("Show All Events");

        try {
            URL imageUrl = jCloudTrailViewer.class.getResource("../../../icons/table.gif");
            btnEvents.setIcon(new ImageIcon(imageUrl));
        }
        catch (Exception e) {

        }

        btnEvents.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (!eventsDatabase.getEvents().isEmpty()) {

                    TableWindow window = new TableWindow("All Events", eventsDatabase.getEvents());
                    window.setVisible(true);

                    desktop.add(window);

                    try {
                        window.setSelected(true);
                    }
                    catch (java.beans.PropertyVetoException pve) {
                    }

                }
                else {

                    JOptionPane.showMessageDialog(
                        desktop,
                        "No Events Loaded!",
                        "Data Error",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.add(btnNewChart);
        buttonsPanel.add(btnEvents);

        this.add(buttonsPanel, BorderLayout.WEST);

        // Spacer panel so Search Field can be on the Right
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

        final JTextField searchBox = new JTextField();
        searchBox.setSize(220, 20);
        searchBox.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    String searchCriteria = searchBox.getText().trim();

                    if (searchCriteria.length() > 0) {
                        freeFormFilter.setValue(searchCriteria);
                        List<Event> events = filters.filterEvents(eventsDatabase.getEvents());

                        TableWindow window = new TableWindow("Filtered by : " + searchCriteria, events);
                        window.setVisible(true);

                        desktop.add(window);

                        try {
                            window.setSelected(true);
                        }
                        catch (java.beans.PropertyVetoException pve) {
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        searchPanel.add(searchBox, BorderLayout.CENTER);

        this.add(searchPanel, BorderLayout.EAST);
    }

    private void createAndShowChart() {

        ChartData chartData = ChartDialog.showDialog(desktop);

        if (chartData != null) {

            List<Map.Entry<String, Integer>> events = eventFilter.getRequiredEvents(eventsDatabase.getEvents(), chartData);

            ChartWindow chart = new ChartWindow(chartData, events);
            chart.setVisible(true);

            desktop.add(chart);

            try {
                chart.setSelected(true);
            }
            catch (java.beans.PropertyVetoException e) {
            }
        }
    }
}
