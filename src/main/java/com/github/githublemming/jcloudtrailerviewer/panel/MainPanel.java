package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.event.EventLoader;
import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabase;
import com.github.githublemming.jcloudtrailerviewer.filter.Filters;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author mark.haskins
 */
public class MainPanel extends JPanel {
    
    private final EventLoader eventLoader;
    
    public MainPanel(EventLoader eventLoader) {
        
        this.eventLoader = eventLoader;
        
        buildUI();
    }
    
    private void buildUI() {
        
        Filters filters = new Filters();
        EventsDatabase eventsDatabase = new EventsDatabase(eventLoader, filters);
        
        this.setLayout(new BorderLayout());

        EventsPanel eventsPanel = new EventsPanel(eventsDatabase, filters);
        AnalysisPanel analysisPanel = new AnalysisPanel(eventsDatabase);
        
        JTabbedPane tabbedPanel = new JTabbedPane();
        tabbedPanel.add("Events", eventsPanel);
        tabbedPanel.add("Analysis", analysisPanel);

        this.setLayout(new BorderLayout());
        this.add(tabbedPanel, BorderLayout.CENTER);
    }
}
