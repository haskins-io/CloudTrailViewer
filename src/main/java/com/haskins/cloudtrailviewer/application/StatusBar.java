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

import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
    private final static JLabel memory = new JLabel();
    
    /**
     * Default Constructor
     */
    public StatusBar() {
        
        buildStatusBar();
        
        Thread t = new Thread(new MemoryCheck());
        t.start();
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
        
        JPanel visibleEventsLabel = new JPanel();
        visibleEventsLabel.add(loadedEvents);
        
        JPanel messageLabel = new JPanel();
        messageLabel.add(statusMessage);
        
        JPanel loadedEventsLabel = new JPanel();
        loadedEventsLabel.add(loadedEvents);
        
        JPanel memoryLabel = new JPanel();
        memoryLabel.add(memory);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        
        panel.add(visibleEventsLabel);
        panel.add(messageLabel);
        panel.add(loadedEventsLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(memoryLabel);
        
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }
    
    private static class MemoryCheck implements Runnable {

        private final Runtime runtime = Runtime.getRuntime();
        
        @Override
        public void run() {
            
            while (true) {
                
                try {
                    long total = runtime.totalMemory() / 1024 / 1024;
                    long free = runtime.freeMemory() / 1024 / 1024;
                    long max = runtime.maxMemory() / 1024 / 1024;
                    long used = total - free;

                    memory.setText(String.format("Memory Used : %sMb | Memory Free %dMb | Max Available : %dMb", used, free, max));
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    
                }
            }
        }
        
    }
}
