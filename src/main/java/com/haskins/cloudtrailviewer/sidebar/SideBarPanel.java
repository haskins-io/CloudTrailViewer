/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.model.event.Event;
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
                
        this.add(buttonsPanel, BorderLayout.PAGE_START);
        this.add(sideBars, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        String sideBar = e.getActionCommand();
        showSideBar(sideBar);
    }
}
