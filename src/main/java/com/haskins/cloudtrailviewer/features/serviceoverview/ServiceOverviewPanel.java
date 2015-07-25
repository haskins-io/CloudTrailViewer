/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.features.serviceoverview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceOverviewPanel extends JPanel {
    
    private final ServiceCountPanel countPanel = new ServiceCountPanel();
    
    public ServiceOverviewPanel(String serviceName) {

        this.setLayout(new BorderLayout());
        
        this.setMinimumSize(new Dimension(200,200));
        this.setMaximumSize(new Dimension(200,200));
        this.setPreferredSize(new Dimension(200,200));
        
        this.setBackground(Color.white);
        this.setOpaque(true);
        
        JLabel title = new JLabel(serviceName);
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel,BoxLayout.PAGE_AXIS));
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(title);
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.setOpaque(false);
        
        this.add(labelPanel, BorderLayout.PAGE_START);
        this.add(countPanel, BorderLayout.CENTER);
    }

    public void incrementCount() {
        countPanel.incrementCount();
    }
    
}
