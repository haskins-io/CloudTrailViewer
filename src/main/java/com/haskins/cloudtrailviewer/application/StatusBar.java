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

package com.haskins.cloudtrailviewer.application;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class that provides the Status Bar component
 * 
 * @author mark
 */
public class StatusBar extends JPanel {
    
    private final JLabel loadedEvents = new JLabel();
    private final JLabel statusMessage = new JLabel();
    
    /**
     * Default Constructor
     */
    public StatusBar() {
        
        buildStatusBar();
    }
    
    /**
     * Sets a message in the center section of the  bar
     * @param message message to display
     */
    public void setStatusMessage(String message) {
        
        statusMessage.setText(message);
    }
    
    /**
     * Indicates the number of Loaded events in the right section of the bar
     * @param eventCount 
     */
    public void setLoadedEvents(int eventCount) {
        
        loadedEvents.setText("Events Loaded : " + eventCount);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildStatusBar() {
        
        JPanel leftSection = new JPanel();
        leftSection.add(loadedEvents);
        
        JPanel middleSection = new JPanel();
        middleSection.add(statusMessage);
        
        JPanel rightSection = new JPanel();
        rightSection.add(loadedEvents);
        
        this.setLayout(new GridLayout(0,3));
        
        this.add(leftSection);
        this.add(middleSection);
        this.add(rightSection);
        
        this.setVisible(true);
    }
}
