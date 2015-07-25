/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer;

import com.haskins.cloudtrailviewer.application.TrailSenseApplication;
import com.haskins.cloudtrailviewer.core.PropertiesController;
import javax.swing.JFrame;

/**
 *
 * @author mark
 */
public class CloudTrailViewer extends JFrame {

    public static final TrailSenseApplication frame = new TrailSenseApplication();
    
    private static void createAndShowGUI() {
        
        PropertiesController.getInstance();
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } 
    /**
     * Main Class used to run the application
     * @param args Array of arguments
     */
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
