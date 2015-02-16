package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.util.JvmUtils;
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
        
        StatusBarPanel.getInstance().setMemory(JvmUtils.getMemoryUsed());
    }
}
