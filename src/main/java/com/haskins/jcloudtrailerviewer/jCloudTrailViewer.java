package com.haskins.jcloudtrailerviewer;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.panel.MenuPanel;
import com.haskins.jcloudtrailerviewer.panel.StatusBarPanel;
import com.haskins.jcloudtrailerviewer.panel.ToolBarPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class jCloudTrailViewer extends JFrame {

    private JDesktopPane desktop;
        
    private final EventLoader eventLoader;
    private final EventsDatabase eventsDatabase;
    
    public jCloudTrailViewer() {
        
        super("jCloudTrailViewer");

        eventsDatabase = new EventsDatabase();
        
        eventLoader = new EventLoader();
        eventLoader.addListener(eventsDatabase);
        
        PropertiesSingleton.getInstance();
        
        buildUI();
    }

    private void buildUI() {
        
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width / 2,
                screenSize.height / 2);

        desktop = new JDesktopPane(); //a specialized layered pane
        
        StatusBarPanel.getInstance();
        JPanel statusBarPanel = StatusBarPanel.getInstance().getStatusBar();
                
        JPanel layout = new JPanel();
        layout.setLayout(new BorderLayout());
        layout.add(new ToolBarPanel(eventsDatabase, desktop), BorderLayout.NORTH);
        layout.add(desktop, BorderLayout.CENTER);
        layout.add(statusBarPanel, BorderLayout.SOUTH);
        
        setContentPane(layout);
        setJMenuBar(new MenuPanel(eventLoader, desktop));
    }
      
    private static void createAndShowGUI() {
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        jCloudTrailViewer frame = new jCloudTrailViewer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
    
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
