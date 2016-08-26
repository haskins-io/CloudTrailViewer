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
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;

/**
 * Feature that shows API events broken down into Users and Roles
 * 
 * @author mark.haskins
 */
public class InvokersFeature extends BaseFeature {
    
    private static final String NAME = "Invoker Feature";
    private static final long serialVersionUID = 6059342047082169382L;

    public InvokersFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                new InvokersContainer(),
                null,
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Invoker Feature", "user")
        );
    }
           
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        ((InvokersContainer)pContainer).addEvent(event);
    }
}