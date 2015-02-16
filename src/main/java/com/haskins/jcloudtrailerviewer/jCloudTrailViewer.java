package com.haskins.jcloudtrailerviewer;

import com.haskins.jcloudtrailerviewer.panel.ApplicationPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class jCloudTrailViewer {
    
    private static final int WINDOW_SIZE_HORIZONTAL = 1280;
    private static final int WINDOW_SIZE_VERTICAL = 800;

    private static final int WINDOW_LOCATION_X = 200;
    private static final int WINDOW_LOCATION_Y = 200;

    public static final String VERSION = "1.0";
    
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("CloudTrail Viewer : " + VERSION);

        JPanel panel = new ApplicationPanel(frame);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.setSize(WINDOW_SIZE_HORIZONTAL, WINDOW_SIZE_VERTICAL);
        frame.setLocation(WINDOW_LOCATION_X, WINDOW_LOCATION_Y);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
