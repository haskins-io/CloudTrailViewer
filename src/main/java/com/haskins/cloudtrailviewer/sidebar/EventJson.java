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

package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Panel that shows the original JSON string for a Event as taken from the log file.
 * 
 * @author mark
 */
public class EventJson extends JPanel implements SideBar {
    
    public static final String NAME = "EventJson";
    
    private final JTextArea rawEvent = new JTextArea();
    
    public EventJson() {
        
        this.setLayout(new BorderLayout());
        
        rawEvent.setFont(new Font("Verdana", Font.PLAIN, 10));
        
        JScrollPane rawJsonScrollPane = new JScrollPane(rawEvent);
        rawJsonScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        this.add(rawJsonScrollPane, BorderLayout.CENTER);
    }
            
    ////////////////////////////////////////////////////////////////////////////
    ///// SideBar implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public String getName() {
        return EventJson.NAME;
    }

    @Override
    public void eventLoadingComplete() { }

    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Braces-32.png";
    }

    @Override
    public String getTooltip() {
        return "Show Event JSON";
    }

    @Override
    public void setCurrentEvent(Event event) {
        
        if (event.getRawJSON() == null ) { EventUtils.addRawJson(event); }
        rawEvent.setText(event.getRawJSON());
    } 
}
