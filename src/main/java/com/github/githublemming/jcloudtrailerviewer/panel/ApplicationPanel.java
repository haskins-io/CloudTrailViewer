package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.PropertiesSingleton;
import com.github.githublemming.jcloudtrailerviewer.event.EventLoader;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class ApplicationPanel extends JPanel {
    
    private final EventLoader eventLoader;
    
    public ApplicationPanel(JFrame frame) {
        
        eventLoader = new EventLoader();
                
        PropertiesSingleton.getInstance();
        
        buildUI();
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        
        MenuPanel menuPane = new MenuPanel(eventLoader);
        MainPanel mainPanel = new MainPanel(eventLoader);
        
        StatusBarPanel.getInstance();
        JPanel statusBarPanel = StatusBarPanel.getInstance().getStatusBar();
        
        this.add(menuPane, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(statusBarPanel, BorderLayout.SOUTH);
    }
}
