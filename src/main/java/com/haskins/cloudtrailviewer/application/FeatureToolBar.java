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

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.FeatureAdditionButton;
import com.haskins.cloudtrailviewer.components.DropDownButton;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

/**
 * Provides the Feature toolbar
 * 
 * @author mark
 */
class FeatureToolBar extends JToolBar implements ActionListener {

    private static final long serialVersionUID = 3551717031217911244L;
        
    private final JPanel buttonsPanel = new JPanel();
    
    private final CloudTrailViewerApplication application;
    
    /**
     * Default Constructor
     * @param application reference to the application
     */
    FeatureToolBar(CloudTrailViewerApplication application) {
                
        this.application = application;
        
        buildToolBar();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds a feature to the tool bar
     * @param feature Feature to add
     */
    void addFeature(Feature feature) {
        
        JButton btn = new JButton();
        btn.setActionCommand(feature.getName());
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                application.changeFeature(e.getActionCommand(), false);
            }
        });

        ToolBarUtils.addImageToButton(btn, feature.getIcon(), feature.getName(), feature.getTooltip());

        if (!feature.getAdditionalButtons().isEmpty()) {

            JPopupMenu popupMenu = new JPopupMenu();

            List<FeatureAdditionButton> additionalButtons = feature.getAdditionalButtons();
            for (FeatureAdditionButton additionButton : additionalButtons) {

                JMenuItem item = new JMenuItem(additionButton.getDescription(), ToolBarUtils.getIcon(additionButton.getIcon()));
                item.addActionListener(additionButton.getActionCommand());
                popupMenu.add(item);
            }

            DropDownButton dropDownButton = new DropDownButton(btn, popupMenu);
            buttonsPanel.add(dropDownButton);

        } else {
            buttonsPanel.add(btn);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {        
        this.application.changeFeature(e.getActionCommand(), false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildToolBar(){
        
        this.setFloatable(false);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        
        buttonsPanel.setBackground(Color.WHITE);
                
        this.add(buttonsPanel, BorderLayout.CENTER);
    }
}
