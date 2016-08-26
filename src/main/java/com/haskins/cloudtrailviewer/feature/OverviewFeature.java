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
import com.haskins.cloudtrailviewer.components.servicespanel.ServiceOverviewContainer;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;

/**
 * Feature that shows loaded events broken down into AWS Services
 * 
 * @author mark
 */
public class OverviewFeature extends BaseFeature {
    
    public static final String NAME = "Overview Feature";
    private static final long serialVersionUID = -2287861024079990428L;

    public OverviewFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                new ServiceOverviewContainer(),
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Overview", "overview")
        );
    }
           
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getIcon() {
        return "Service-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "Service API Overview";
    }
    
    @Override
    public String getName() {
        return OverviewFeature.NAME;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        ((ServiceOverviewContainer)container).addEvent(event);
    }
    
    @Override
    public void finishedLoading() { }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    void buildUI() {
        super.buildUI();
    }
   
}