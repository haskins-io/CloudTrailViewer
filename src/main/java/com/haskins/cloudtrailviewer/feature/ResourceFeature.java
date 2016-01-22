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
import com.haskins.cloudtrailviewer.components.OverviewContainer;
import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * Feature that shows API events to do with Creating or deletion of resources
 * 
 * @author mark.haskins
 */
public class ResourceFeature extends JPanel implements Feature {
    
    public static final String NAME = "Resources Feature";
    private static final long serialVersionUID = 4393936519169431431L;
    
    private final Help help = new Help("Resource Feature", "resources");
    
    private final List<String> resourceEvents = new ArrayList<>();
    
    private final OverviewContainer resourcesContainer;
    private final EventTablePanel eventTable = new EventTablePanel(EventTablePanel.CHART_RESOURCE);
    
    private final HelpToolBar helpBar;
    private final StatusBar statusBar;
    private JSplitPane jsp;
    
    public ResourceFeature(StatusBar sb, HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        this.statusBar = sb;
        
        resourcesContainer = new OverviewContainer(this);
        
        loadSecurityEvents();
        buildUI();
    }
           
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Server-48.png";
    }

    @Override
    public String getTooltip() {
        return "Resources Overview";
    }
    
    @Override
    public String getName() {
        return ResourceFeature.NAME;
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
        
        if (!eventTable.isVisible()) {
            
            jsp.setDividerLocation(0.5);
            jsp.setDividerSize(3);
            eventTable.setVisible(true);
        }
        
        statusBar.setEvents(events);
        eventTable.clearEvents();
        eventTable.setEvents(events);
    }
        
    @Override
    public void reset() {
        
        resourcesContainer.reset();
        resourcesContainer.revalidate();
        this.revalidate();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        if (resourceEvents.contains(event.getEventName())) {
            resourcesContainer.addEvent(event, "EventName");
        }
    }
    
    @Override
    public void finishedLoading() {
        resourcesContainer.finishedLoading();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        resourcesContainer.setBackground(Color.white);
        JScrollPane sPane = new JScrollPane(resourcesContainer);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        eventTable.setVisible(false);
        
        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sPane, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
    
    private void loadSecurityEvents() {
        
        String query = "SELECT api_call FROM aws_resources";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            
            String aws_name = (String)row.get("api_call");
            resourceEvents.add(aws_name);
        }
    }
}