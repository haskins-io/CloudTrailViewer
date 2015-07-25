/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.application;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class StatusBar extends JPanel {
    
    private final JLabel loadedEvents = new JLabel();
    private final JLabel statusMessage = new JLabel();
    
    public StatusBar() {
        
        buildStatusBar();
    }
    
    public void setStatusMessage(String message) {
        
        statusMessage.setText(message);
    }
    
    public void setLoadedEvents(int eventCount) {
        
        loadedEvents.setText("Events Loaded : " + eventCount);
    }
    
    private void buildStatusBar() {
        
        this.setBackground(Color.WHITE);
        
        JPanel leftSection = new JPanel();
        leftSection.add(loadedEvents);
        
        JPanel middleSection = new JPanel();
        middleSection.add(statusMessage);
        
        JPanel rightSection = new JPanel();
        rightSection.add(loadedEvents);
        
        this.setLayout(new GridLayout(0,3));
        
        this.add(leftSection);
        this.add(middleSection);
        this.add(rightSection);
        
        this.setVisible(true);
        
    }

}
