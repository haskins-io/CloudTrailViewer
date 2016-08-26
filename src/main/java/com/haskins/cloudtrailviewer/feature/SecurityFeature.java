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
 * Feature that shows Events that might be considered to be security risks
 * 
 * @author mark.haskins
 */
public class SecurityFeature extends BaseFeature {
    
    private static final String NAME = "Security Feature";
    private static final long serialVersionUID = -8036727410192669423L;

    private final List<String> securityEvents = new ArrayList<>();

    public SecurityFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                new OverviewContainer(),
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Security Feature", "security")
        );

        loadSecurityEvents();
    }
           
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getIcon() {
        return "Warning-48.png";
    }

    @Override
    public String getTooltip() {
        return "Security Overview";
    }
    
    @Override
    public String getName() {
        return SecurityFeature.NAME;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        if (securityEvents.contains(event.getEventName())) {
            container.addEvent(event, "EventName");
        }
    }
    
    @Override
    public void finishedLoading() {
        container.finishedLoading();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    void buildUI() {

        super.buildUI();
    }
    
    private void loadSecurityEvents() {
        
        String query = "SELECT api_call FROM aws_security";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            
            String aws_name = (String)row.get("api_call");
            securityEvents.add(aws_name);
        }
    }
}