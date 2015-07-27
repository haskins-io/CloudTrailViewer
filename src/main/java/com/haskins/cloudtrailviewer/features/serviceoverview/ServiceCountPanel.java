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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceCountPanel extends JPanel {
    
    private final JLabel eventCount = new JLabel(String.valueOf(0));
        
    public ServiceCountPanel(String name, Color bgColour) {
        
        this.setBackground(bgColour);
        
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(90,45));
        this.setMaximumSize(new Dimension(90,45));
        this.setPreferredSize(new Dimension(90,45));
                
        Font labelFont = eventCount.getFont();
        eventCount.setForeground(Color.white);
        eventCount.setAlignmentX(CENTER_ALIGNMENT);
        eventCount.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 30));
        
        JPanel countPanel = new JPanel();
        countPanel.setLayout(new BoxLayout(countPanel,BoxLayout.PAGE_AXIS));
        countPanel.add(Box.createHorizontalGlue());
        countPanel.add(Box.createVerticalGlue());
        countPanel.add(eventCount);
        countPanel.add(Box.createHorizontalGlue());
        countPanel.add(Box.createVerticalGlue());
        countPanel.setOpaque(false);
        
        
        
        this.add(countPanel, BorderLayout.CENTER);
        
        JLabel title = new JLabel(name);
        title.setForeground(Color.white);
        this.add(title, BorderLayout.PAGE_START);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel,BoxLayout.PAGE_AXIS));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.setOpaque(false);
        
        this.setOpaque(true);
    }
    
    public void incrementCount() {
        
        int count = Integer.parseInt(eventCount.getText());
        count++;
        eventCount.setText(String.valueOf(count));
        
        Font labelFont = eventCount.getFont();
        if (eventCount.getText().length() >= 6) {
            eventCount.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 20));
        }
        
        this.revalidate();
    }
}
