/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.features.serviceoverview;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class ServiceEPSPanel extends JPanel {
    
    private final Map<Long, Integer> eps = new HashMap<>();
    
    private int peak = 0;
    private final JLabel epsLabel = new JLabel(String.valueOf(peak));
    
    public ServiceEPSPanel(Color bgColour) {
        
        this.setBackground(bgColour);
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(90,45));
        this.setMaximumSize(new Dimension(90,45));
        this.setPreferredSize(new Dimension(90,45));
                
        Font labelFont = epsLabel.getFont();
        epsLabel.setForeground(Color.white);
        epsLabel.setAlignmentX(CENTER_ALIGNMENT);
        epsLabel.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 20));
        
        JPanel countPanel = new JPanel();
        countPanel.setLayout(new BoxLayout(countPanel,BoxLayout.PAGE_AXIS));
        countPanel.add(Box.createHorizontalGlue());
        countPanel.add(Box.createVerticalGlue());
        countPanel.add(epsLabel);
        countPanel.add(Box.createHorizontalGlue());
        countPanel.add(Box.createVerticalGlue());
        countPanel.setOpaque(false);
        
        this.add(countPanel, BorderLayout.CENTER);
        this.setOpaque(true);
    }
    
    public void newEvent(Event event) {
        
        long time = event.getTimestamp();
        
        int count = 0;
        if (eps.containsKey(time)) {
            count = eps.get(time);
        }
        
        count++;
        eps.put(time, count);
        
        if (count > peak) {
            peak = count;
            epsLabel.setText(String.valueOf(peak));
            this.revalidate();
        }
    }
}
