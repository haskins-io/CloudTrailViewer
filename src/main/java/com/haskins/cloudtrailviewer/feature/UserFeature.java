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
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.servicespanel.ServiceOverviewContainer;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.NameValueModel;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author mark
 */
public class UserFeature extends JPanel implements Feature, EventDatabaseListener {

    public static final String NAME = "User Feature";
    
    private final Help help = new Help("User Feature", "user");

    private final Map<String, NameValueModel> userMap = new HashMap<>();
    private final DefaultTableModel userTableModel = new DefaultTableModel();

    private final Map<String, NameValueModel> roleMap = new HashMap<>();
    private final DefaultTableModel roleTableModel = new DefaultTableModel();

    private final ServiceOverviewContainer servicesContainer;
    private final EventTablePanel eventTable = new EventTablePanel();
    
    private final HelpToolBar helpBar;

    public UserFeature(HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        
        servicesContainer = new ServiceOverviewContainer(this);
        
        buildUI();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        populateTable(userMap, userTableModel);
        populateTable(roleMap, roleTableModel);
    }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "User-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "User Overview";
    }

    @Override
    public String getName() {
        return UserFeature.NAME;
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

        eventTable.clearEvents();
        eventTable.setEvents(events);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {

        String type = event.getUserIdentity().getType();
        if (type.equalsIgnoreCase("IAMUser") || type.equalsIgnoreCase("AssumedRole")) {

            if (type.equalsIgnoreCase("IAMUser")) {
                addUser(event);
            }
            else {
                addRole(event);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void addUser(Event event) {

        String username = event.getUserIdentity().getUserName();
        if (username == null) {
            username = event.getUserIdentity().getPrincipalId();
        }

        if (!userMap.containsKey(username)) {

            NameValueModel model = new NameValueModel(username, event);
            userMap.put(username, model);

        }
        else {

            NameValueModel model = userMap.get(username);
            model.addEvent(event);
        }
    }

    private void addRole(Event event) {

        boolean was_role = true;

        if (event.getEventName().equalsIgnoreCase("ConsoleLogin")) {
            was_role = false;
            addUser(event);
        }

        String role;
        if (event.getUserIdentity().getSessionContext() != null) {
            role = event.getUserIdentity().getSessionContext().getSessionIssuer().getUserName();

        }
        else {
            role = event.getUserIdentity().getPrincipalId();
        }

        if (was_role) {

            if (!roleMap.containsKey(role)) {

                NameValueModel model = new NameValueModel(role, event);
                roleMap.put(role, model);

            }
            else {

                NameValueModel model = roleMap.get(role);
                model.addEvent(event);
            }
        }
    }

    private void buildUI() {

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.add("Users", createTable(userTableModel, userMap));
        tabbedPane.add("Roles", createTable(roleTableModel, roleMap));

        eventTable.setVisible(true);

        servicesContainer.setMinimumSize(new Dimension(300, 300));
        servicesContainer.setPreferredSize(new Dimension(300, 300));

        JSplitPane jsp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, servicesContainer, eventTable);
        jsp1.setBackground(Color.WHITE);
        jsp1.setDividerSize(3);
        jsp1.setDividerLocation(0.5);

        JSplitPane jsp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, tabbedPane, jsp1);
        jsp2.setBackground(Color.WHITE);
        jsp2.setDividerSize(3);

        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        this.add(jsp2, BorderLayout.CENTER);
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
        column.setMinWidth(50);
        column.setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(300, 400));
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        return scrollPane;
    }
    
    private void showEvents(List<Event> events) {

        eventTable.clearEvents();
        eventTable.setEvents(events);
        servicesContainer.setEvents(events);
    }
}
