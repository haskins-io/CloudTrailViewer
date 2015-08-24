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

package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.NameValueModel;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark.haskins
 */
public class ErrorFeature extends JPanel implements Feature, EventDatabaseListener {
        
    public static final String NAME = "Error Feature";
    
    private final Help help = new Help("Error Feature", "error");
    
    private final DefaultTableModel errorsTableModel = new DefaultTableModel();
    private final Map<String, NameValueModel> errorsMap = new HashMap<>();
    private final EventTablePanel eventTable = new EventTablePanel(EventTablePanel.CHART_EVENT);
        
    private final StatusBar statusBar;
    private final HelpToolBar helpBar;
    
    public ErrorFeature(StatusBar sb, HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        this.statusBar = sb;
        
        buildUI();
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { 
        populateTable(errorsMap, errorsTableModel);
    }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Error-48.png";
    }

    @Override
    public String getTooltip() {
        return "Error Overview";
    }
    
    @Override
    public String getName() {
        return ErrorFeature.NAME;
    }
    
    @Override
    public void will_hide() {
        helpBar.setHelp(null);
    }
    
    @Override
    public void will_appear() {
        helpBar.setHelp(help);
    }
    
    @Override
    public void showEventsTable(List<Event> events) {
                
        statusBar.setVisibleEvents(events.size());
        eventTable.clearEvents();
        eventTable.setEvents(events);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
                 
        String errorName = event.getErrorCode();
        if (errorName.trim().length() > 0) {
            
            if (!errorsMap.containsKey(errorName)) {

                NameValueModel errorModel = new NameValueModel(errorName, event);
                errorsMap.put(errorName, errorModel);
            } else {
                
                NameValueModel errorModel = errorsMap.get(errorName);
                errorModel.addEvent(event);
            }
        }
    }
    
    @Override
    public void finishedLoading() {
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {
        
        eventTable.setVisible(true);

        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0)); 
        this.setLayout(new BorderLayout());
        this.add(createTable(errorsTableModel, errorsMap), BorderLayout.WEST);
        this.add(eventTable, BorderLayout.CENTER);
    }
    
    private void populateTable(Map<String, NameValueModel> map, DefaultTableModel model) {

        Set<String> keys = map.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            NameValueModel nameValue = map.get(key);
            model.addRow(new Object[]{key, nameValue.getNumberOfEvents()});
        }
    }

    private JScrollPane createTable(final DefaultTableModel model, final Map<String, NameValueModel> map) {

        model.addColumn("Property");
        model.addColumn("Value");

        final JTable table = new JTable(model);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getFirstIndex() >= 0) {

                    String user = (String) model.getValueAt(table.getSelectedRow(), 0);
                    NameValueModel model = map.get(user);
                    showEvents(model.getEvents());
                }
            }
        });
        
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setMinWidth(75);
        column.setPreferredWidth(75);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(300, 400));
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        return scrollPane;
    }
    
    private void showEvents(List<Event> events) {

        eventTable.clearEvents();
        eventTable.setEvents(events);
    }
}