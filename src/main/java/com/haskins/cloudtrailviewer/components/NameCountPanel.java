/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.components;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.TimeStampComparator;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class NameCountPanel extends JPanel {
    
    public final static Icon USER = ToolBarUtils.getIcon("");
    public final static Icon SERVER = ToolBarUtils.getIcon("");
    
    private final List<Event> events = new ArrayList<>();
    private boolean sorted = false;
    
    private final Feature feature;
    
    private final JLabel count = new JLabel();
    
    public NameCountPanel(Icon image, String name, Feature f) {
        
        this.feature = f;
        
        this.setLayout(new BorderLayout());
        
        if (image != null) {
            JLabel icon = new JLabel();
            icon.setIcon(image);
            this.add(icon, BorderLayout.WEST);
        }
        
        JLabel nameLabel = new JLabel(name + " : ");
        nameLabel.setToolTipText("Total Errors");
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nameLabel.addMouseListener(new MouseAdapter() {
                
            @Override
            public void mouseClicked(MouseEvent e) {

                if (!sorted) {
                    Collections.sort(events, new TimeStampComparator());
                    sorted = true;
                }

                feature.showEventsTable(events);
            }
        });
        
        this.add(nameLabel, BorderLayout.CENTER);
        this.add(count, BorderLayout.EAST);
    }
    
    public void addEvent(Event event) {
        
        this.events.add(event);
        count.setText(String.valueOf(this.events.size()));

        this.revalidate();
    }
}
