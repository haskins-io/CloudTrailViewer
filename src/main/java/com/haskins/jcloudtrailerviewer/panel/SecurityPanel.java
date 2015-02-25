package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventLoaderListener;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
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
 * @author mark
 */
public class SecurityPanel extends JInternalFrame implements EventLoaderListener {

    private final EventLoader eventLoader = new EventLoader();
    
    private final EventsTableModel errorTableModel;
    private final EventsTableModel iamTableModel;
    private final EventsTableModel securityTableModel;
    
    private final EventDetailTableModel detailTableModel = new EventDetailTableModel();

    private final JTextArea rawJsonPanel = new JTextArea();
    
    public SecurityPanel() {
        
        super("Security", true, true, false, true);
        
        eventLoader.addListener(this);
        
        errorTableModel = new EventsTableModel();
        iamTableModel = new EventsTableModel();
        securityTableModel = new EventsTableModel();
        
        buildUI();
        
        // open load files browser
        eventLoader.showFileBrowser();
    }
    
    @Override
    public void newEvents(List<Event> events) {
        
        for (Event event : events) {
            
            // Errors
            if (event.getErrorCode().length() > 1) {
                EventUtils.addRawJson(event);
                errorTableModel.addEvent(event);
            }
            
            // IAM
            
            // Security
            
        }
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());

        this.setSize(640, 480);
        
        // chart button
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
                
            }
        });
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(btnShowChart);
        
        
        // need three tables
        EventsTable errorTable = new EventsTable(errorTableModel);
        errorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                Event event = errorTableModel.getEventAt(e.getFirstIndex());
                showEventDetail(event);
            }
        });
        errorTable.setVisible(true);
        JScrollPane errorScrollPane = new JScrollPane(errorTable);
        
        EventsTable iamTable = new EventsTable(iamTableModel);
        iamTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                Event event = iamTableModel.getEventAt(e.getFirstIndex());
                showEventDetail(event);
            }
        });
        iamTable.setVisible(true);
        JScrollPane iamScrollPane = new JScrollPane(iamTable);
        
        EventsTable securityTable = new EventsTable(securityTableModel);
        securityTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                Event event = securityTableModel.getEventAt(e.getFirstIndex());
                showEventDetail(event);
            }
        });
        securityTable.setVisible(true);
        JScrollPane securityScrollPane = new JScrollPane(securityTable);
        

        // Detail area
        EventDetailTable detailTable = new EventDetailTable(detailTableModel);
        JScrollPane eventDetailScrollPane = new JScrollPane(detailTable);
        JScrollPane rawJsonScrollPane = new JScrollPane(rawJsonPanel);

        JTabbedPane detailPanel = new JTabbedPane();
        detailPanel.add("Table View", eventDetailScrollPane);
        detailPanel.add("Raw View", rawJsonScrollPane);
        
        rawJsonPanel.setFont(new Font("Verdana", Font.PLAIN, 12));

        
        // need a tabbed pane for the tables
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Errors", errorScrollPane);
        tabs.add("IAM", iamScrollPane);
        tabs.add("Network", securityScrollPane);
        
        // need a split pane to put everything together
        JSplitPane split = new JSplitPane();
        split.add(tabs, JSplitPane.LEFT);
        split.add(detailPanel, JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerSize(2);
        split.setAutoscrolls(false);
        split.setDividerLocation(400);
        
        add(toolbar, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }
    
    private void showEventDetail(Event event) {

        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }
}
