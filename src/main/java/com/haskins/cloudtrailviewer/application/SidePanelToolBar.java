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

import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * Provides a JToolBar that provides the tool bar on the right of the application
 * that shows such items as the Toggle Side Bar functionality
 * 
 * @author mark
 */
public class SidePanelToolBar extends JToolBar {
    
    private final JButton btnLocal = new JButton();
    private final CloudTrailViewerApplication application;
    
    /**
     * Default Constructor
     * @param application a reference to the application
     */
    public SidePanelToolBar(CloudTrailViewerApplication application) {
        
        this.application = application;
        
        buildToolBar();
    }
    
    /**
     * Sets the visible state of the Side Bar
     * @param show TRUE side bar will be shown, otherwise side bar will be hidden
     */
    public void showSideBarButton(boolean show) {
        btnLocal.setVisible(show);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildToolBar(){
        
        this.setFloatable(false);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        
        addToggleSideBarButton(buttonsPanel);
        
        this.add(buttonsPanel, BorderLayout.EAST);
    }
    
    private void addToggleSideBarButton(JPanel buttonsPanel) {
        
        ToolBarUtils.addImageToButton(btnLocal, "View-Split-48.png", "Side Bar", "Toggle Sidebar");
        btnLocal.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                application.toggleSidebar();
            }
        }); 
        
        buttonsPanel.add(btnLocal);
    }
    
}