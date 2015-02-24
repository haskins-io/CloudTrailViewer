package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.Event;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author mark
 */
public class StatusBarPanel implements EventsDatabaseListener {
        
    private static StatusBarPanel instance = null;
    
    private final JPanel ui = new JPanel();
    
    private JButton btnIamWarning;
    private JButton btnSecWarning;
    private JButton btnErrWarning;
    
    private final JLabel message = new JLabel("Load some CloudTrail events");
    private final JLabel eventsLoaded = new JLabel("0");
        
    private final List<Event> errorsEvents = new ArrayList<>();
    private final List<Event> iamEvents = new ArrayList<>();
    private final List<Event> securityEvents = new ArrayList<>();
    

    @Override
    public void onEvent(Event event) {
        updateWarnings(event);
        updateIamWarnings(event);
    }
    
    
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
    }

    private void buildUI() {
        
        btnIamWarning = new JButton("IAM Warnings");
        btnIamWarning.setVisible(false);
        btnIamWarning.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showTable("IAM Warnings", errorsEvents);
            }
        });
        
        btnSecWarning = new JButton("Security Warnings");
        btnSecWarning.setVisible(false);
        btnSecWarning.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        
        btnErrWarning = new JButton("Error Warnings");
        btnErrWarning.setVisible(false);
        btnErrWarning.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        
        JToolBar warnings = new JToolBar();
        warnings.setFloatable(false);
        
        JPanel rightSection = new JPanel();
        rightSection.add(new JLabel("Events Loaded :"));
        rightSection.add(eventsLoaded);
        
        this.ui.setLayout(new BorderLayout());
        this.ui.add(warnings, BorderLayout.WEST);
        this.ui.add(message, BorderLayout.CENTER);
        this.ui.add(rightSection, BorderLayout.EAST);
        
        this.ui.setVisible(true);
    }
    
    private void showTable(String windowTitle, List<Event> events) {
        
        TableWindow window = new TableWindow(windowTitle, events);
        window.setVisible(true);

        jCloudTrailViewer.DESKTOP.add(window);

        try {
            window.setSelected(true);
        }
        catch (java.beans.PropertyVetoException pve) {
        }
    }   
    
    private void updateWarnings(Event event) {
        
        if (event.getErrorCode().length() > 1) {
            errorsEvents.add(event);
            btnErrWarning.setVisible(true);
        }
    }
    
    private void updateIamWarnings(Event event) {
        
        if(event.getEventSource().equalsIgnoreCase("iam.amazonaws.com")) {
            iamEvents.add(event);
            btnIamWarning.setVisible(true); 
        }
    }
}
