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
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class MetricsFeature extends JPanel implements Feature {

    public static final String NAME = "Metrics Feature";

    private final Help help = new Help("Metrics Feature", "metrics");
    
    private final StatusBar statusBar;
    private final HelpToolBar helpBar;
    

    public MetricsFeature(StatusBar sb, HelpToolBar helpBar) {

        this.helpBar = helpBar;
        this.statusBar = sb;
    }
    

    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() {
    }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Geo-Data-48.png";
    }

    @Override
    public String getTooltip() {
        return "Metrics Overview";
    }

    @Override
    public String getName() {
        return MetricsFeature.NAME;
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

    }

    @Override
    public void reset() {

    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {

    }

    @Override
    public void finishedLoading() {
       
    }
}
