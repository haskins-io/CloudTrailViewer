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

package com.haskins.cloudtrailviewer.features;

import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.sidebar.EventTree;
import com.haskins.cloudtrailviewer.sidebar.EventJson;
import com.haskins.cloudtrailviewer.sidebar.EventsStats;
import com.haskins.cloudtrailviewer.sidebar.SideBarPanel;
import com.haskins.cloudtrailviewer.table.EventsTable;
import com.haskins.cloudtrailviewer.table.EventsTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Feature that provides a Table view of the loaded events
 *  
 * @author mark
 */
public class SimpleTable extends JPanel implements Feature, ActionListener {
    
    public static final String NAME = "Simple Table";
    
    private final FilteredEventDatabase eventDb;
    private final EventsTableModel tableModel;
    private final EventsTable table;
    
    private final JPopupMenu popupMenu = new JPopupMenu();
    
    private final SideBarPanel sideBar = new SideBarPanel();
    private JSplitPane jsp;
    
    private final JTextField filterTextField = new JTextField();
    
    /**
     * Default constructor
     * @param eventsDatabase reference to an Event Database 
     */
    public SimpleTable(FilteredEventDatabase eventsDatabase) {
        
        eventDb = eventsDatabase;
        tableModel = new EventsTableModel(eventsDatabase);
        table = new EventsTable(tableModel);
        
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        toggleColumnVisibleState(e.getActionCommand());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        tableModel.orderTimeStamps();
        sideBar.eventLoadingComplete();
    }
    
    @Override
    public boolean providesSideBar() {
        return true;
    }
    
    @Override
    public boolean showOnToolBar() {
        return true;
    }
        
    @Override
    public String getIcon() {
        return "Table-48.png";
    }

    @Override
    public String getTooltip() {
        return "Select Table view";
    }
    
    @Override
    public void toggleSideBar() {
        
        sideBar.setVisible(!sideBar.isVisible());
        
        if (sideBar.isVisible()) {
            jsp.setDividerLocation(0.8);
            jsp.setDividerSize(3);
        } else {
            jsp.setDividerLocation(1);
            jsp.setDividerSize(0);
        }
    } 
    
    @Override
    public String getName() {
        return SimpleTable.NAME;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        this.setLayout(new BorderLayout());
        
        for (String columnName : tableModel.columnNames) {
            JMenuItem menuItem = new JCheckBoxMenuItem(columnName, true);
            menuItem.addActionListener(this);
            popupMenu.add(menuItem);
        }
        
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
             
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    showPopupMenu(mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {
                    Event event = tableModel.getEventAt(table.getSelectedRow());
                    sideBar.currentEvent(event);
                    
                    if (!sideBar.isVisible()) {
                        toggleSideBar();
                    }
                }
            }
        });

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
        
        
        filterTextField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                filterUpdate();
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                filterUpdate();
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                filterUpdate();
            }
        });
        
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.add(new JLabel("Filter"), BorderLayout.WEST);
        filterPanel.add(filterTextField, BorderLayout.CENTER);
                
        add(filterPanel, BorderLayout.PAGE_START);
        add(jsp, BorderLayout.CENTER);
    }
    
    private void showPopupMenu(int x, int y) {
        
        for (MenuElement element : popupMenu.getSubElements()) {
            
            JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)element;
            String name = menuItem.getText();
            int position = tableModel.findColumn(name);
            
            if (table.getColumnModel().getColumn(position).getWidth() == 0) {
                menuItem.setState(false);
            } else {
                menuItem.setState(true);
            }
        }
        
        popupMenu.show(table.getTableHeader(), x, y);
    }
    
    private void toggleColumnVisibleState(String columnName) {
        
        int position = tableModel.findColumn(columnName);

        if (table.getColumnModel().getColumn(position).getWidth() == 0) {
            table.getColumnModel().getColumn(position).setMaxWidth(100);
            table.getColumnModel().getColumn(position).setMinWidth(100);
            table.getColumnModel().getColumn(position).setPreferredWidth(100);
            table.getColumnModel().getColumn(position).setResizable(true);
        } else {
            table.getColumnModel().getColumn(position).setMaxWidth(0);
            table.getColumnModel().getColumn(position).setMinWidth(0);
            table.getColumnModel().getColumn(position).setPreferredWidth(0);
            table.getColumnModel().getColumn(position).setResizable(false);
        }
    }
    
    private void filterUpdate()
    {
        String text = filterTextField.getText();
        this.eventDb.getFilter().setNeedle(text);
    }
}
