
package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.filter.Filters;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventDetailTableModel;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.table.EventsTableModel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
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
public class EventsPanel extends JPanel implements MouseListener {
            
    private final Filters filters;
    
    private final EventsDatabase eventsDatabase;
    
    private EventsTable table;
    private final EventsTableModel tableModel;
    
    private EventDetailTable detailTable;
    private final EventDetailTableModel detailTableModel;
    
    private final JTextArea rawJsonPanel = new JTextArea();
    
    
    public EventsPanel(EventsDatabase database, Filters filters) {
        
        this.filters = filters;
        eventsDatabase = database;
        
        tableModel = new EventsTableModel(eventsDatabase);
        detailTableModel = new EventDetailTableModel();
       
        buildUI();
    }
        
    ////////////////////////////////////////////////////////////////////////////
    // MouseListener Methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e)
    {
        int rowIndex = table.getSelectedRow();
        if (rowIndex >= 0 && e.getButton() == e.BUTTON3)  {

        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////    
    private void buildUI() {
        
        // Create the filter panel
        FilterPanel filterPanel = new FilterPanel(filters);
        eventsDatabase.addListeners(filterPanel);
        
        // Create the primary Events table
        table = new EventsTable(tableModel, eventsDatabase);
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
                
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    
                    Event event = eventsDatabase.getRecordByIndex(e.getFirstIndex());
                    showEventDetail(event);
                }
            });

        table.addMouseListener(this);

        JScrollPane eventsScrollPane = new JScrollPane(table);
        
        // Create the Detail Panel
        JTabbedPane detailPanel = new JTabbedPane();
        
        detailTable = new EventDetailTable(detailTableModel);
        JScrollPane eventDetailScrollPane = new JScrollPane(detailTable);
        
        JScrollPane rawJsonScrollPane = new JScrollPane(rawJsonPanel);
        
        detailPanel.add("Table View", eventDetailScrollPane);
        detailPanel.add("Raw View", rawJsonScrollPane);
        
        
        // Put them all together
        JSplitPane split = new JSplitPane();
        split.add(eventsScrollPane, JSplitPane.LEFT);
        split.add(detailPanel, JSplitPane.RIGHT);
        split.setOneTouchExpandable(true);
        split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(900);
        split.setDividerSize(2);
        split.setAutoscrolls(false);
        
        setLayout(new BorderLayout());
        
        add(filterPanel, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }
    
    private void showEventDetail(Event event) {
                
        detailTableModel.showDetail(event);
        rawJsonPanel.setText(event.getRawJSON());
    }
}

