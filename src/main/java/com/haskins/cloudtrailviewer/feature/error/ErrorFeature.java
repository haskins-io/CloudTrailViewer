
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
package com.haskins.cloudtrailviewer.feature.error;

import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.thirdparty.SortedListModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author mark.haskins
 */
public class ErrorFeature extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "Error Feature";
    
    private final Map<String, List<Event>> errorsMap = new HashMap<>();
    private final SortedListModel errorListModel = new SortedListModel<>();    
    private final EventTablePanel eventTable = new EventTablePanel();
        
    public ErrorFeature(FilteredEventDatabase eventsDatabase) {
        
        eventsDatabase.addListener(this);
        
        buildUI();
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { }

    @Override
    public boolean providesSideBar() {
        return false;
    }

    @Override
    public void toggleSideBar() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Service-Overview-48.png";
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
    public void will_hide() { }
    
    @Override
    public void will_appear() { }
    
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
                 
        String errorName = event.getErrorCode();
        if (errorName.trim().length() > 0) {
            
            List<Event> errorEvents;

            if (!errorsMap.containsKey(errorName)) {

                errorEvents = new ArrayList();
                errorsMap.put(errorName, errorEvents);
                errorListModel.add(errorName);
            
            } else {
                errorEvents = errorsMap.get(errorName);
            }

            errorEvents.add(event);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        final JList errorList = new JList(errorListModel);
        errorList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                                          
                String selected = (String)errorList.getSelectedValue();
                
                eventTable.clearEvents();
                eventTable.setEvents(errorsMap.get(selected));
            }
        });
        
        errorList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        errorList.setLayoutOrientation(JList.VERTICAL);
        errorList.setVisibleRowCount(-1);

        JScrollPane sPane = new JScrollPane(errorList);
        sPane.setMinimumSize(new Dimension(300, 400));
        sPane.setPreferredSize(new Dimension(300, 400));
        sPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        eventTable.setVisible(true);

        this.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0)); 
        this.setLayout(new BorderLayout());
        this.add(sPane, BorderLayout.WEST);
        this.add(eventTable, BorderLayout.CENTER);
    }
}