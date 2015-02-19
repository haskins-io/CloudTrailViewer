package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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
    
    public TableWindow(String title, List<Event> events) {
        
        super(title, true, true, false, true);
        
        tableModel = new EventsTableModel(events);
        
        buildUI();
    }
    
    private void buildUI() {
                
        this.setLayout(new BorderLayout());
        
        this.setSize(640, 480);
        
        EventsTable table = new EventsTable(tableModel);
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
                
            @Override
            public void valueChanged(ListSelectionEvent e) {

                Event event = tableModel.getEventAt(e.getFirstIndex());
                showEventDetail(event);
            }
        });
        
        table.setVisible(true);

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
                
        add(split, BorderLayout.CENTER);
    }
    
    private void showEventDetail(Event event) {
                
        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }

}
