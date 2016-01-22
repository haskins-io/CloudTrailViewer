/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.haskins.cloudtrailviewer.components;

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
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Panel that shows a name and a count.
 * 
 * @author mark.haskins
 */
public class NameValuePanel extends JPanel {

    private static final long serialVersionUID = 829334953720731893L;
    
    private final List<Event> events = new ArrayList<>();
    
    private final JLabel iconPanel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JLabel totalLabel = new JLabel("0");
    
    private boolean sorted = false;
    private final Feature parent;
    
    /**
     * Default constructor
     * @param event Event name to be shown
     * @param icon Optional. If passed the icon will be shown on the left side of
     * the panel
     * @param f Reference to feature that should be called when panel is clicked.
     */
    public NameValuePanel(String event, Icon icon, Feature f) {
     
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        this.setBackground(Color.white);
        
        this.setMaximumSize(new Dimension(1750,30));
        
        this.nameLabel.setText(event);
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
        nameLabel.setBackground(Color.white);
        nameLabel.setOpaque(true);
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        totalLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        totalLabel.setBackground(new Color(102, 153, 153));
        totalLabel.setPreferredSize(new Dimension(50,30));
        totalLabel.setMinimumSize(new Dimension(50,30));
        totalLabel.setMaximumSize(new Dimension(50,30));
        totalLabel.setOpaque(true);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.setLayout(new BorderLayout());
        
        if (icon != null) {
            iconPanel.setIcon(icon);
            this.add(iconPanel, BorderLayout.WEST);
        }
        
        this.add(nameLabel, BorderLayout.CENTER);
        this.add(totalLabel, BorderLayout.EAST);   
    }
    
    /**
     * Adds an event to the panel.
     * @param event 
     * @return total number of events
     */
    public int addEvent(Event event) {
                
        events.add(event);
        totalLabel.setText(String.valueOf(events.size()));
        this.revalidate();
        
        return events.size();
    }
    
    /**
     * Returns the number of events associated with the panel.
     * @return 
     */
    public int getEventCount() {
        return events.size();
    }
    
    /**
     * returns a single Event associated with the Panel
     * @return 
     */
    public Event getSampleEvent() {
        return events.get(0);
    }
    
    /**
     * Resizes the Panel with a new width.
     * @param newWidth 
     */
    public void changeWidth(int newWidth) {
        this.setMaximumSize(new Dimension(newWidth,30));
        this.setMinimumSize(new Dimension(newWidth,30));
        this.setMaximumSize(new Dimension(newWidth,30));
    }
}
