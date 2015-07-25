/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.features.serviceoverview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceCountPanel extends JPanel {
    
    private final JLabel eventCount = new JLabel(String.valueOf(0));
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
                
        g.setColor(Color.lightGray);
        g.fillOval(13, 8, 175, 175);
        
        g.setColor(Color.green);
        g.fillOval(10, 5, 175, 175);
    }
    
    public ServiceCountPanel() {
        
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(180,180));
        this.setMaximumSize(new Dimension(180,180));
        this.setPreferredSize(new Dimension(180,180));
        
        GridBagConstraints cl = new GridBagConstraints();
        
        eventCount.setForeground(Color.white);
        eventCount.setAlignmentX(CENTER_ALIGNMENT);
        
        Font labelFont = eventCount.getFont();
        eventCount.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 60));
        
        this.add(eventCount, cl);
        
        this.setOpaque(false);
    }
    
    public void incrementCount() {
        
        int count = Integer.parseInt(eventCount.getText());
        count++;
        eventCount.setText(String.valueOf(count));
        
        this.revalidate();
    }
}
