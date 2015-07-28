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

package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class SideBarPanel extends JPanel implements ActionListener {
            
    private final JPanel sideBars = new JPanel(new CardLayout());
    private final Map<String, SideBar> sideBarMap = new HashMap<>();
    private SideBar currentSideBar = null;
    
    private final JPanel buttonsPanel = new JPanel();
    
    public SideBarPanel() {
        
        buildToolBar();
    }
    
    public void addSideBar(SideBar sidebar) {
        
        sideBars.add((JPanel)sidebar, sidebar.getName());
        sideBarMap.put(sidebar.getName(), sidebar);
        
        if (sidebar.showOnToolBar()) {
            
            JButton btnLocal = new JButton();
            btnLocal.addActionListener(this);
            btnLocal.setActionCommand(sidebar.getName());
            
            ToolBarUtils.addImageToButton(btnLocal, sidebar.getIcon(), sidebar.getName(), sidebar.getTooltip());
            buttonsPanel.add(btnLocal);
        }        
    }    
    
    public void showSideBar(String name) {
        
        currentSideBar = sideBarMap.get(name);
        
        CardLayout cl = (CardLayout)(sideBars.getLayout());
        cl.show(sideBars, name);
    }
        
    public void eventLoadingComplete() {

        for (Component component : sideBars.getComponents()) {
            
            SideBar sideBar = (SideBar)component;
            sideBar.eventLoadingComplete();
        }
    }
    
    public void currentEvent(Event event) {
        
        for (Component component : sideBars.getComponents()) {
            
            SideBar sideBar = (SideBar)component;
            sideBar.setCurrentEvent(event);
        } 
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildToolBar(){
        
        this.setLayout(new BorderLayout());
        
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
                
        JButton print = new JButton("Print");
        print.setActionCommand("Print");
        print.addActionListener(this);
        ToolBarUtils.addImageToButton(print, "", "Print", "Print Sidebar");
        
        buttonsPanel.add(print);
        buttonsPanel.add(Box.createHorizontalGlue());
        
        this.add(buttonsPanel, BorderLayout.PAGE_START);
        this.add(sideBars, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equalsIgnoreCase("Print")) {
            
            GeneralUtils.savePanelAsImage(currentSideBar);
            
        } else {
            String sideBar = e.getActionCommand();
            showSideBar(sideBar);
        }
    }
}
