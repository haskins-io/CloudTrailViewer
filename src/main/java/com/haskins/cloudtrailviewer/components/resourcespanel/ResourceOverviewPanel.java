/*
 * Copyright (C) 2015 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.components.resourcespanel;

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.utils.TimeStampComparator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ResourceOverviewPanel extends JPanel {
    
    private final List<Event> events = new ArrayList<>();
    
    private final JLabel nameLabel = new JLabel();
    private final JLabel totalLabel = new JLabel("0");
    
    private boolean sorted = false;
    private final Feature parent;
    
    public ResourceOverviewPanel(String securityEvent, Feature f) {
     
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
        this.setBackground(Color.white);
        
        this.nameLabel.setText(securityEvent);
        this.parent = f;
        
        nameLabel.addMouseListener(new MouseAdapter() {
                
            @Override
            public void mouseClicked(MouseEvent e) {

                if (!sorted) {
                    Collections.sort(events, new TimeStampComparator());
                    sorted = true;
                }

                parent.showEventsTable(events);
            }
        });
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        nameLabel.setBackground(new Color(51, 102, 153));
        nameLabel.setOpaque(true);
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        totalLabel.setBackground(new Color(102, 153, 153));
        totalLabel.setPreferredSize(new Dimension(50,30));
        totalLabel.setMinimumSize(new Dimension(50,30));
        totalLabel.setMaximumSize(new Dimension(50,30));
        totalLabel.setOpaque(true);
        totalLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        this.setLayout(new BorderLayout());
        this.add(nameLabel, BorderLayout.CENTER);
        this.add(totalLabel, BorderLayout.EAST);   
    }
    
    public void addEvent(Event event) {
                
        events.add(event);
        
        String strTotal = totalLabel.getText();
        int intCount = Integer.parseInt(strTotal);
        intCount++;
        totalLabel.setText(String.valueOf(intCount));
        
        this.revalidate();
    }
}
