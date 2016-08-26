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
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;

/**
 * Feature that displays API Errors as it's top level presentation
 * 
 * @author mark.haskins
 */
public class ErrorFeature extends BaseFeature {
        
    private static final String NAME = "Error Feature";
    private static final long serialVersionUID = -9102410065515704792L;

    public ErrorFeature(StatusBar sb, HelpToolBar helpBar) {

        super(
                sb,
                helpBar,
                new OverviewContainer(),
                null,
                new EventTablePanel(EventTablePanel.CHART_EVENT),
                new Help("Error Feature", "error")
        );
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        String errorName = event.getErrorCode();
        if (errorName.trim().length() > 0) {
            pContainer.addEvent(event, "ErrorCode");
        }
    }
}