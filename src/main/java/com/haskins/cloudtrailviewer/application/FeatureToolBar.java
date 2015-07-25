/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.application;

import com.haskins.cloudtrailviewer.features.Feature;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author mark
 */
public class FeatureToolBar extends JToolBar implements ActionListener {
        
    private final JPanel buttonsPanel = new JPanel();
    
    private final TrailSenseApplication application;
    
    public FeatureToolBar(TrailSenseApplication application) {
                
        this.application = application;
        
        buildToolBar();
    }
    
    private void buildToolBar(){
        
        this.setFloatable(false);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        
        buttonsPanel.setBackground(Color.WHITE);
                
        this.add(buttonsPanel, BorderLayout.CENTER);
    }
    
    public void addFeature(Feature feature) {
        
        JButton btnLocal = new JButton();
        btnLocal.setActionCommand(feature.getName());
        btnLocal.addActionListener(this);
        
        ToolBarUtils.addImageToButton(btnLocal, feature.getIcon(), feature.getName(), feature.getTooltip());
        buttonsPanel.add(btnLocal);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        this.application.changeFeature(e.getActionCommand());
    }
}
