/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventLoaderListener;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.table.EventDetailTable;
import com.haskins.jcloudtrailerviewer.table.EventsTable;
import com.haskins.jcloudtrailerviewer.util.EventTimestampComparator;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author mark
 */
public class ScanChartPanel extends JInternalFrame implements EventLoaderListener {
    
    private final EventLoader eventLoader = new EventLoader();
    
   
    public ScanChartPanel(String windowName, List<String> actions) {
        
        super(windowName, true, true, false, true);
                        
        eventLoader.addListener(this);
        
        Object[] options = {"Local Files", "S3 Files"};
        int i = JOptionPane.showOptionDialog(
            jCloudTrailViewer.DESKTOP, 
            "Do you want to scan local files or remote files", 
            "Choose File location", 
            JOptionPane.YES_NO_CANCEL_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);
        
        if (i == 0) {
            eventLoader.showFileBrowser();
            buildUI();
        } else if (i == 1) {
            eventLoader.showS3Browser();
            buildUI();
        } else {
            this.dispose();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {
               
        for (Event event : events) {
            
        }
    }
    
    @Override
    public void finishedLoading() {

    }
    
    @Override
    public void newMessage(String message) {}
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        this.setSize(640, 480);
    }
    
}
