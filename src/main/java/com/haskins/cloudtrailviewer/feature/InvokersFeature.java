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
import com.haskins.cloudtrailviewer.components.InvokersContainer;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author mark.haskins
 */
public class InvokersFeature extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "Invoker Feature";
    
    private final Help help = new Help("Invoker Feature", "user");
        
    private final InvokersContainer resourcesContainer;
    private final EventTablePanel eventTable = new EventTablePanel(EventTablePanel.CHART_RESOURCE);
    
    private final HelpToolBar helpBar;
    private final StatusBar statusBar;
    private JSplitPane jsp;
    
    public InvokersFeature(StatusBar sb, HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        this.statusBar = sb;
        
        resourcesContainer = new InvokersContainer(this);
        
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
        return "User-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "Invokers Overview";
    }
    
    @Override
    public String getName() {
        return InvokersFeature.NAME;
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
        
        statusBar.setVisibleEvents(events.size());
        eventTable.clearEvents();
        eventTable.setEvents(events);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        resourcesContainer.addEvent(event);
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
    
}