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

package com.haskins.jcloudtrailerviewer;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.panel.MenuPanel;
import com.haskins.jcloudtrailerviewer.panel.StatusBarPanel;
import com.haskins.jcloudtrailerviewer.panel.ToolBarPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class jCloudTrailViewer extends JFrame {

    public final static JDesktopPane DESKTOP = new JDesktopPane();
    
    public final static String VERSION = "1.0";
        
    private final EventLoader eventLoader;
    public EventsDatabase eventsDatabase;
    
    public jCloudTrailViewer() {
        
        super("jCloudTrailViewer");
        
        eventsDatabase = new EventsDatabase();
        
        eventLoader = new EventLoader();
        eventLoader.addListener(eventsDatabase);
        
        PropertiesSingleton.getInstance();
        
        buildUI();
    }

    private void buildUI() {
        
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width / 2,
                screenSize.height / 2);

        this.setTitle("CloudTrail viewer and analysis");
        
        StatusBarPanel.getInstance();
        JPanel statusBarPanel = StatusBarPanel.getInstance().getStatusBar();
                
        JPanel layout = new JPanel();
        layout.setLayout(new BorderLayout());
        layout.add(new ToolBarPanel(eventsDatabase), BorderLayout.NORTH);
        layout.add(DESKTOP, BorderLayout.CENTER);
        layout.add(statusBarPanel, BorderLayout.SOUTH);
        
        setContentPane(layout);
        setJMenuBar(new MenuPanel(eventLoader, eventsDatabase));
    }
      
    private static void createAndShowGUI() {
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        jCloudTrailViewer frame = new jCloudTrailViewer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
    
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
