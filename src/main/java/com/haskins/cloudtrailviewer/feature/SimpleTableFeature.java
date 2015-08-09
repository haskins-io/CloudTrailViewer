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
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;

/**
 * Feature that provides a Table view of the loaded events
 *  
 * @author mark
 */
public class SimpleTableFeature extends JPanel implements Feature {
    
    public static final String NAME = "Table Feature";
    
    private final Help help = new Help("Table Feature", "table");
        
    private final EventTablePanel tablePanel;
    
    private final HelpToolBar helpBar;
    
    /**
     * Default constructor
     * @param eventsDatabase reference to an Event Database 
     */
    public SimpleTableFeature(FilteredEventDatabase eventsDatabase, HelpToolBar helpBar) {
              
        this.helpBar = helpBar;
        
        tablePanel = new EventTablePanel(eventsDatabase);
        
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
        tablePanel.eventLoadingComplete();
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
    public String getName() {
        return SimpleTableFeature.NAME;
    }
    
    @Override
    public void will_hide() {
        helpBar.setHelp(null);
    }
    
    @Override
    public void will_appear() {
        helpBar.setHelp(help);
        tablePanel.will_appear();
        this.revalidate();
    }
    
    @Override
    public void showEventsTable(List<Event> events) {}

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        this.setLayout(new BorderLayout());
        
        add(tablePanel, BorderLayout.CENTER);
    }
}
