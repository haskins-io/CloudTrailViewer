/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.panel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class StatusBarPanel {
        
    private static StatusBarPanel instance = null;
    
    private final JPanel ui = new JPanel();
        
    private final JLabel message = new JLabel("Load some CloudTrail events");
    private final JLabel eventsLoaded = new JLabel("0");
                    
    private StatusBarPanel() {
        buildUI();
    }
    
    public static StatusBarPanel getInstance() {
     
        if (instance == null) {
            instance = new StatusBarPanel();
        }
        
        return instance;
    }
        
    public JPanel getStatusBar() {
        return this.ui;
    }
    
    public void setEventsLoaded(int events) {
        this.eventsLoaded.setText(String.valueOf(events));
    }
    public void incrementEventsLoaded(int events) {
        
        String currentNumEvents = this.eventsLoaded.getText();
        int count = Integer.valueOf(currentNumEvents);
        count = count + events;
        
        this.eventsLoaded.setText(String.valueOf(count));
    }
    
    public void setMessage(String message) {
        
        this.message.setText(message);
    }

    private void buildUI() {
  
        JPanel rightSection = new JPanel();
        rightSection.add(new JLabel("Events Loaded :"));
        rightSection.add(eventsLoaded);
        
        this.ui.setLayout(new BorderLayout());
        this.ui.add(message, BorderLayout.CENTER);
        this.ui.add(rightSection, BorderLayout.EAST);
        
        this.ui.setVisible(true);
    }
}
