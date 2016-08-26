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
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Feature that provides a "No Events Loaded" panel
 * 
 * @author mark
 */
public class NoDataFeature extends JPanel implements Feature {
        
    public static final String NAME = "No Data";
    private static final long serialVersionUID = -2472899470035275634L;
    
    private final Help help = new Help("CloudTrail Viewer", "default");
    
    private static final String NO_EVENTS = "No Events Loaded.";
    
    private final JLabel label = new JLabel(NO_EVENTS);
    
    private final HelpToolBar helpBar;
    
    /**
     * Default Constructor
     * @param helpBar
     */
    public NoDataFeature(HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        
        buildPanel();
    }
    
    public void showEventsAvailable() {
        label.setText("");
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Card implementation
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    public void eventLoadingComplete() { }
        
    @Override
    public boolean showOnToolBar() {
        return false;
    }
    
    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getTooltip() {
        return null;
    }
    
    @Override
    public String getName() {
        return NoDataFeature.NAME;
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
    public void showPrimaryData(List<Event> events) {}
        
    @Override
    public void reset() {
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) { }

    @Override
    public void finishedLoading() { }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildPanel() {
                
        this.setLayout(new GridBagLayout());
        this.add(label);
    }

}
