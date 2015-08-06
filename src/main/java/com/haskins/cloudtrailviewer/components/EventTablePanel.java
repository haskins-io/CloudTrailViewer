/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.sidebar.EventJson;
import com.haskins.cloudtrailviewer.sidebar.EventTree;
import com.haskins.cloudtrailviewer.sidebar.EventsStats;
import com.haskins.cloudtrailviewer.sidebar.SideBarPanel;
import com.haskins.cloudtrailviewer.table.EventsTable;
import com.haskins.cloudtrailviewer.table.EventsTableModel;
import com.haskins.cloudtrailviewer.thirdparty.XTableColumnModel;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark
 */
public class EventTablePanel extends JPanel implements ActionListener {
    
    private final FilteredEventDatabase eventDb;
    private final EventsTableModel tableModel;
    private final EventsTable table;

    private final SideBarPanel sideBar = new SideBarPanel();
    private JSplitPane jsp;

    private final JTextField filterTextField = new JTextField();

    private final XTableColumnModel customColumnModel = new XTableColumnModel();
    private final JPopupMenu columnPopupMenu = new JPopupMenu();

    /**
     * This Constructor creates it's own EventsDatabase.
     */
    public EventTablePanel() {
        this(new FilteredEventDatabase(new AllFilter(), null));
    }

    /**
     * This Constructor tables an existing EventsDatabase
     *
     * @param eventsDatabase reference to an Event Database
     */
    public EventTablePanel(FilteredEventDatabase eventsDatabase) {

        eventDb = eventsDatabase;
        tableModel = new EventsTableModel(eventsDatabase);
        table = new EventsTable(tableModel);

        table.setColumnModel(customColumnModel);
        table.createDefaultColumnsFromModel();
        
        buildUI();
    }

    /**
     * When called with show or hide the sid bar
     */
    public void toggleSideBar() {

        sideBar.setVisible(!sideBar.isVisible());

        if (sideBar.isVisible()) {
            jsp.setDividerLocation(0.8);
            jsp.setDividerSize(3);
        }
        else {
            jsp.setDividerLocation(1);
            jsp.setDividerSize(0);
        }
    }
    
    /**
     * Should be called when the table is going to be made visible so it can reload
     * the table.
     */
    public void will_appear() {
        tableModel.reloadTableModel();
    }
    
    /**
     * Should be called when the table is going to be hidden to that the table 
     * can be emptied to free up some memory
     */
    public void will_hide() { }

    /**
     * Empty the database of all events.
     */
    public void clearEvents() {
        eventDb.clear();
    }

    /**
     * Sets the Events that the table will display. Calling this will reload the
     * table model and display the events in the table.
     * @param events 
     */
    public void setEvents(List<Event> events) {
        eventDb.addEvents(events);
        sideBar.eventLoadingComplete();
        tableModel.reloadTableModel();
    }

    /**
     * 
     */
    public void eventLoadingComplete() {
        sideBar.eventLoadingComplete();
        tableModel.reloadTableModel();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        createPopupMenu();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {

                    Event event = tableModel.getEventAt(table.getSelectedRow());
                    if (event != null) {

                        sideBar.currentEvent(event);

                        if (!sideBar.isVisible()) {
                            toggleSideBar();
                        }
                    }
                }
            }
        });

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                if (e.getButton() == MouseEvent.BUTTON3) {
                    columnPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            };
        });
        table.setAutoCreateRowSorter(true);
        
        JScrollPane eventsScrollPane = new JScrollPane(table);
        eventsScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        sideBar.addSideBar(new EventJson());
        sideBar.addSideBar(new EventTree());
        sideBar.addSideBar(new EventsStats(eventDb));

        sideBar.setVisible(false);

        jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, eventsScrollPane, sideBar);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().width
            - jsp.getInsets().right
            - jsp.getDividerSize());

        filterTextField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                filterUpdate();
            }
        
        });
        
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(Color.white);
        filterPanel.add(new JLabel("Filter"), BorderLayout.WEST);
        filterPanel.add(filterTextField, BorderLayout.CENTER);

        JButton export = new JButton();
        export.setActionCommand("");
        export.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GeneralUtils.saveTableAsCsv(table);
            }

        });
        ToolBarUtils.addImageToButton(export, "CSV-Export-32.png", "Export", "Save to CSV");

        filterPanel.add(export, BorderLayout.EAST);

        add(filterPanel, BorderLayout.PAGE_START);
        add(jsp, BorderLayout.CENTER);
    }

    private void filterUpdate() {

        String text = filterTextField.getText();
        this.eventDb.getFilter().setNeedle(text);
    }
    
    private void createPopupMenu() {
        
        for (int i=0; i < EventsTableModel.COLUMN_NAMES.length; i++) {
            addMenuItem(EventsTableModel.COLUMN_NAMES[i], i, true);
        }
    }

    private void addMenuItem(String name, int position, boolean visible) {
         
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
        menuItem.setActionCommand(String.valueOf(position));
        menuItem.addActionListener(this);
        menuItem.setSelected(visible);
        columnPopupMenu.add(menuItem);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
                
        String action = e.getActionCommand();
        int columnId = Integer.parseInt(action);
        
        TableColumn column = customColumnModel.getColumnByModelIndex(columnId);
        boolean visible = customColumnModel.isColumnVisible(column);
        
        JCheckBoxMenuItem selected = (JCheckBoxMenuItem)e.getSource();
        selected.setSelected(!visible);
        customColumnModel.setColumnVisible(column, !visible);
    }
}
