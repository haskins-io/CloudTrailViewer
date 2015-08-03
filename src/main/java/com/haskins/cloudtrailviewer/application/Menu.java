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
import com.haskins.cloudtrailviewer.dialog.AboutDialog;
import com.haskins.cloudtrailviewer.dialog.preferences.PreferencesDialog;
import com.haskins.cloudtrailviewer.thirdparty.org.simplericity.macify.eawt.Application;
import com.haskins.cloudtrailviewer.thirdparty.org.simplericity.macify.eawt.ApplicationEvent;
import com.haskins.cloudtrailviewer.thirdparty.org.simplericity.macify.eawt.ApplicationListener;
import com.haskins.cloudtrailviewer.thirdparty.org.simplericity.macify.eawt.DefaultApplication;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * Provides the Application Menu
 * 
 * @author mark
 */
public class Menu extends JMenuBar implements ApplicationListener {

    private final Application application;
    
    /**
     * Default Constructor
     */
    public Menu() {
        
        this.application = new DefaultApplication();
        application.addApplicationListener(this);
        application.addPreferencesMenuItem();
        application.setEnabledPreferencesMenu(true);
        
        buildMenu();
    }
    
    @Override
    public void handleAbout(ApplicationEvent event) {
        showAboutDialog();
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

            menuFile.add(preferences);
            menuFile.add(exit);

            this.add(menuFile);
        }
    }
    
    private void showAboutDialog() {
        AboutDialog.showDialog(CloudTrailViewer.frame);
    }
    
    private void handleCloseApplication() {
        
        Object[] options = {"Yes", "No"};
            
        if(JOptionPane.showOptionDialog(
            CloudTrailViewer.frame, 
            "Are you sure you want to quit?", "Quit?",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, null) == JOptionPane.OK_OPTION) {
            
                System.exit(0);
        }
    }
    
    private void showPreferencesDialog() {
        PreferencesDialog.showDialog(CloudTrailViewer.frame);
    }
}
