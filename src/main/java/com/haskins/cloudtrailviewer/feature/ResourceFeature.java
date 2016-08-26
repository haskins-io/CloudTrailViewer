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
import java.util.ArrayList;
import java.util.List;

/**
 * Feature that shows API events to do with Creating or deletion of resources
 * 
 * @author mark.haskins
 */
public class ResourceFeature extends BaseFeature {
    
    private static final String NAME = "Resources Feature";
    private static final long serialVersionUID = 4393936519169431431L;

    private final List<String> resourceEvents = new ArrayList<>();

    public ResourceFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                new OverviewContainer(),
                null,
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Resource Feature", "resources")
        );

        loadSecurityEvents();
    }
           
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        if (resourceEvents.contains(event.getEventName())) {
            pContainer.addEvent(event, "EventName");
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void loadSecurityEvents() {
        
        String query = "SELECT api_call FROM aws_resources";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            
            String aws_name = (String)row.get("api_call");
            resourceEvents.add(aws_name);
        }
    }
}