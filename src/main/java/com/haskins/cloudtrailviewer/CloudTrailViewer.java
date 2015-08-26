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

package com.haskins.cloudtrailviewer;

import com.haskins.cloudtrailviewer.application.CloudTrailViewerApplication;
import com.haskins.cloudtrailviewer.application.Menu;
import com.haskins.cloudtrailviewer.core.AwsService;
import com.haskins.cloudtrailviewer.core.DbManager;
import com.haskins.cloudtrailviewer.core.PropertiesController;
import javax.swing.JFrame;

/**
 * Launcher class for the application.
 * 
 * @author mark
 */
public class CloudTrailViewer extends JFrame {

    public static CloudTrailViewerApplication frame;
    
    private static void createAndShowGUI() {
                
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        Menu menu = new Menu();    
        frame.setJMenuBar(menu);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } 
    /**
     * Main Class used to run the application
     * @param args Array of arguments
     */
    public static void main(String[] args) {

        DbManager.getInstance().sync();
        PropertiesController.getInstance();
        AwsService.getInstance();
        
        frame = new CloudTrailViewerApplication();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
