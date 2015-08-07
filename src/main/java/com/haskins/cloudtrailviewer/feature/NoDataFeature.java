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

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Feature that provides a "No Events Loaded" panel
 * @author mark
 */
public class NoDataFeature extends JPanel implements Feature {
        
    public static final String NAME = "No Data";
    
    private static final String noEvents = "No Events Loaded.";
    private static final String events = "";
    
    JLabel label = new JLabel(noEvents);
    
    /**
     * Default Constructor
     */
    public NoDataFeature() {
        buildPanel();
    }
    
    public void showEventsAvailable() {
        label.setText(events);
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
    public void will_hide() { }
    
    @Override
    public void will_appear() { }
    
    @Override
    public void showEventsTable(List<Event> events) {}
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildPanel() {
                
        this.setLayout(new GridBagLayout());
        this.add(label);
    }

}
