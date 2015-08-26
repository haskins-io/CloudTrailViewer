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

import static com.haskins.cloudtrailviewer.CloudTrailViewer.frame;
import com.haskins.cloudtrailviewer.dialog.HelpDialog;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * Provides a JToolBar that provides the tool bar on the right of the application
 * that shows such items as the Toggle Side Bar functionality
 * 
 * @author mark
 */
public class HelpToolBar extends JToolBar {
    
    private Help help = null;
    private final JButton btnHelp = new JButton();
    
    /**
     * Default Constructor
     */
    public HelpToolBar() {
        
        buildToolBar();
    }
    
    /**
     * Sets the visible state of the Side Bar
     * @param help
     */
    public void setHelp(Help help) {
        this.help = help;
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
                
        ToolBarUtils.addImageToButton(btnHelp, "Help-48.png", "Help", "Show Help page");
        btnHelp.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                showHelp();
            }
        }); 
        
        buttonsPanel.add(btnHelp);
        
        this.add(buttonsPanel, BorderLayout.EAST);
    }    
    
    private void showHelp() {
        if (this.help == null) {
            
            JOptionPane.showMessageDialog(frame,
                "No help is available for this Feature.",
                "Help",
                JOptionPane.ERROR_MESSAGE);
            
        }  else {

            HelpDialog.showDialog(frame, help);
        }
    }
}