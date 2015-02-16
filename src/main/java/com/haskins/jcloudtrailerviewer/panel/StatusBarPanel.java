package com.haskins.jcloudtrailerviewer.panel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class StatusBarPanel {
    
    private static StatusBarPanel instance = null;
    
    private final JPanel ui = new JPanel();
    
    private final JLabel filesLoaded = new JLabel("0");
    private final JLabel message = new JLabel("Load some CloudTrail events");
    private final JLabel eventsLoaded = new JLabel("0");
    private final JLabel memoryUsed = new JLabel("0");
    
    private StatusBarPanel() {
        buildUI();
    }
    
    public static StatusBarPanel getInstance() {
     
        if (instance == null) {
            
            instance = new StatusBarPanel();
        }
        
        return instance;
    }
    
    public JPanel getStatusBar() {
        return this.ui;
    }
    
    public void setLoadedFiles(int numLoadedFiles) {
        this.filesLoaded.setText(String.valueOf(numLoadedFiles));
    }
    
    public void setEventsLoaded(int events) {
        this.eventsLoaded.setText(String.valueOf(events));
    }
    public void incrementEventsLoaded(int events) {
        
        String currentNumEvents = this.eventsLoaded.getText();
        int count = Integer.valueOf(currentNumEvents);
        count = count + events;
        
        this.eventsLoaded.setText(String.valueOf(count));
    }
    
    public void setMessage(String message) {
        this.message.setText(message);
        
        this.message.revalidate();
        this.ui.revalidate();
    }
    
    public void setMemory(long memory) {
        this.memoryUsed.setText(String.valueOf(memory));
        
    }
    
    private void buildUI() {
        
        JPanel leftSection = new JPanel();
        JPanel middleSection = new JPanel();
        JPanel rightSection = new JPanel();
        
        // Left Section
        leftSection.add(new JLabel("Files Loaded :"));
        leftSection.add(filesLoaded);
        
        leftSection.add(new JLabel("  Events Loaded :"));
        leftSection.add(eventsLoaded);
        
        // middle section
        middleSection.add(message);
        
        // right section
        rightSection.add(new JLabel("Memory Used :"));
        rightSection.add(memoryUsed);
        rightSection.add(new JLabel("Mb"));
        
        // put everything together
        this.ui.setLayout(new BorderLayout());
        this.ui.add(leftSection, BorderLayout.WEST);
        this.ui.add(middleSection, BorderLayout.CENTER);
        this.ui.add(rightSection, BorderLayout.EAST);
        
        this.ui.setVisible(true);
    }
    
}
