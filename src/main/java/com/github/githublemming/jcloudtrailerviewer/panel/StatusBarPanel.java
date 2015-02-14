package com.github.githublemming.jcloudtrailerviewer.panel;

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
    
    private final JLabel filesLoaded = new JLabel();
    private final JLabel message = new JLabel();
    
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
    
    public void setMessage(String message) {
        this.message.setText(message);
        
        this.message.revalidate();
        this.ui.revalidate();
    }
    
    private void buildUI() {
        
        JPanel leftSection = new JPanel();
        JPanel middleSection = new JPanel();
        JPanel rightSection = new JPanel();
        
        // Left Section
        JLabel filesLoadedLbl = new JLabel("Files Loaded :");
        leftSection.add(filesLoadedLbl);
        leftSection.add(filesLoaded);
        
        
        // middle section
        middleSection.add(message);
        
        
        // right section
        
        
        // put everything together
        this.ui.setLayout(new BorderLayout());
        this.ui.add(leftSection, BorderLayout.WEST);
        this.ui.add(middleSection, BorderLayout.CENTER);
        this.ui.add(rightSection, BorderLayout.EAST);
        
        this.ui.setVisible(true);
    }
    
}
