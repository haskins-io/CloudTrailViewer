/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author mark
 */
public class SidePanelToolBar extends JToolBar {
    
    private final JButton btnLocal = new JButton();
    private final TrailSenseApplication application;
    
    public SidePanelToolBar(TrailSenseApplication application) {
        
        this.application = application;
        
        buildToolBar();
    }
    
    public void showSideBarButton(boolean show) {
        btnLocal.setVisible(show);
    }
    
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