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

package com.haskins.cloudtrailviewer.features.serviceoverview;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceOverviewPanel extends JPanel {
    
    private final ServiceCountPanel awsCountPanel;
    private final ServiceCountPanel iamCountPanel;
    
    private final JLabel totalLabel = new JLabel("0");
    
    private final List<Event> events = new ArrayList<>();
    
    public ServiceOverviewPanel(String serviceName) {

        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(200,100));
        this.setMaximumSize(new Dimension(200,100));
        this.setPreferredSize(new Dimension(200,100));
        this.setBackground(Color.white);
        this.setOpaque(true);
        
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
        
        JLabel title = new JLabel(serviceName);
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel,BoxLayout.PAGE_AXIS));
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.add(title);
        labelPanel.add(Box.createHorizontalGlue());
        labelPanel.setOpaque(false);
        
        JPanel countsPanel = new JPanel(new GridLayout(1,2));
        awsCountPanel = new ServiceCountPanel("AWS", new Color(51, 102, 153));
        iamCountPanel = new ServiceCountPanel("IAM", new Color(102, 153, 153));
        
        countsPanel.add(awsCountPanel);
        countsPanel.add(iamCountPanel);
        
        JPanel totalPanel = new JPanel();
        totalPanel.add(new JLabel("Total : "));
        totalPanel.add(totalLabel);
        
        this.add(labelPanel, BorderLayout.PAGE_START);
        this.add(countsPanel, BorderLayout.CENTER);
        this.add(totalPanel, BorderLayout.PAGE_END);
    }

    public void addEvent(Event event) {
        events.add(event);
        
        if (event.getUserIdentity().getType().equalsIgnoreCase("Root")) {
            awsCountPanel.incrementCount();
        } else {
            iamCountPanel.incrementCount();
        }
        
        String strTotal = totalLabel.getText();
        int intCount = Integer.parseInt(strTotal);
        intCount++;
        totalLabel.setText(String.valueOf(intCount));
        
        this.revalidate();
    }

    public List getEvents() {
        return this.events;
    }
}
