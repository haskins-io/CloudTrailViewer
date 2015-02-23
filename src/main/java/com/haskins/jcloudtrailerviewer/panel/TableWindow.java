package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author mark.haskins
 */
public class TableWindow extends JInternalFrame {

    private final EventsTableModel tableModel;
    private final EventDetailTableModel detailTableModel = new EventDetailTableModel();

    private final JTextArea rawJsonPanel = new JTextArea();

    private final List<Event> masterEvents;

    private final EventUtils eventFilter = new EventUtils();
    
    

    public TableWindow(String title, List<Event> events) {

        super(title, true, true, false, true);

        masterEvents = events;

        tableModel = new EventsTableModel();
        tableModel.setData(masterEvents);

        buildUI();
    }

    private void buildUI() {

        this.setLayout(new BorderLayout());

        this.setSize(640, 480);

        EventsTable table = new EventsTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                Event event = tableModel.getEventAt(e.getFirstIndex());
                showEventDetail(event);
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

        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    private void showEventDetail(Event event) {

        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }

    private void createAndShowChart() {

        ChartData chartData = ChartDialog.showDialog(jCloudTrailViewer.DESKTOP);

        if (chartData != null) {

            List<Map.Entry<String, Integer>> events = eventFilter.getRequiredEvents(masterEvents, chartData);

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
}
