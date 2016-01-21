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

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.dao.DbManager;
import com.haskins.cloudtrailviewer.core.PropertiesController;
import com.haskins.cloudtrailviewer.dialog.preferences.PreferencesDialog;
import com.haskins.cloudtrailviewer.dialog.s3filechooser.EnhancedS3FileChooser;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;

/**
 *
 * Provides the Application Menu
 * 
 * @author mark
 */
public class Menu extends JMenuBar implements ApplicationListener {

    private final Application application;
    private final CloudTrailViewerApplication ctApplication;
    
    /**
     * Default Constructor
     * @param ctApp reference to main Application class
     */
    public Menu(CloudTrailViewerApplication ctApp) {
        
        ctApplication = ctApp;
        
        this.application = new DefaultApplication();
        application.addApplicationListener(this);
        application.addPreferencesMenuItem();
        application.setEnabledAboutMenu(true);
        application.setEnabledPreferencesMenu(true);
        
        buildMenu();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ApplicationListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void handleAbout(ApplicationEvent event) {
        showAboutDialog();
        event.setHandled(true);
    }
   
    @Override
    public void handlePreferences(ApplicationEvent event) {
        showPreferencesDialog();
    }
   
    @Override
    public void handleQuit(ApplicationEvent event) {
        handleCloseApplication();
    }
    
    @Override
    public void handleReOpenApplication(ApplicationEvent event) {}
    @Override
    public void handlePrintFile(ApplicationEvent event) {}
    @Override
    public void handleOpenApplication(ApplicationEvent event) {}
    @Override
    public void handleOpenFile(ApplicationEvent event) {}
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildMenu() {
        
        if (!application.isMac()) {
            
            // -- Menu : File
            JMenu menuFile = new JMenu("File");
            
            JMenuItem preferences = new JMenuItem(new AbstractAction("Preferences") {

                @Override
                public void actionPerformed(ActionEvent t) {
                    showPreferencesDialog();
                }
            });
            
            JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {

                @Override
                public void actionPerformed(ActionEvent t) {
                    handleCloseApplication();
                }
            });
            
            JMenuItem about = new JMenuItem(new AbstractAction("About") {

                @Override
                public void actionPerformed(ActionEvent t) {
                    showAboutDialog();
                }
            });

            menuFile.add(preferences);
            menuFile.add(about);
            menuFile.add(exit);
            
            this.add(menuFile);
        }
        
        JMenu eventsMenu = new JMenu("Events");
        
        JMenuItem clearEvents = new JMenuItem(new AbstractAction("Clear Events") {

            @Override
            public void actionPerformed(ActionEvent t) {
                ctApplication.clearEvents();
            }
        });
        
        eventsMenu.add(clearEvents);
        
        this.add(eventsMenu);
        
        /** Test Code **/
        JMenu menuTest = new JMenu("Test");

        JMenuItem dialogTest = new JMenuItem(new AbstractAction("S3 Dialog") {

            @Override
            public void actionPerformed(ActionEvent t) {
                final List<String> files = EnhancedS3FileChooser.showDialog(CloudTrailViewer.frame, EnhancedS3FileChooser.MODE_SCAN);
                for (String file : files) {
                    System.out.println(file);
                }
            }
        });

        menuTest.add(dialogTest);
//        this.add(menuTest);
    }
    
    private void showAboutDialog() {
        
        String app_version = PropertiesController.getInstance().getProperty("application.version");
        int db_version = DbManager.getInstance().getCurrentDbVersion();
        
        StringBuilder message = new StringBuilder();
        message.append("CloudTrailViewr\n");
        message.append(app_version);
        message.append(" [").append(db_version).append("]");
        
        JOptionPane.showMessageDialog(
            CloudTrailViewer.frame, 
            message.toString(), 
            "CloudTrail Viewer?",
            JOptionPane.INFORMATION_MESSAGE,
            ToolBarUtils.getIcon("logo_50x50.png")
        );
        
    }
    
    private void handleCloseApplication() {
        
        Object[] options = {"Yes", "No"};
            
        if(JOptionPane.showOptionDialog(
            CloudTrailViewer.frame, 
            "Are you sure you want to quit?", "Quit?",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[1]) == JOptionPane.OK_OPTION) {
            
                System.exit(0);
        }
    }
    
    private void showPreferencesDialog() {
        PreferencesDialog.showDialog(CloudTrailViewer.frame);
    }
}
