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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceCountPanel extends JPanel {
    
    private final JLabel eventCount = new JLabel(String.valueOf(0));
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
                
        g.setColor(Color.lightGray);
        g.fillOval(13, 8, 175, 175);
        
        g.setColor(Color.green);
        g.fillOval(10, 5, 175, 175);
    }
    
    public ServiceCountPanel() {
        
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(180,180));
        this.setMaximumSize(new Dimension(180,180));
        this.setPreferredSize(new Dimension(180,180));
        
        GridBagConstraints cl = new GridBagConstraints();
        
        eventCount.setForeground(Color.white);
        eventCount.setAlignmentX(CENTER_ALIGNMENT);
        
        Font labelFont = eventCount.getFont();
        eventCount.setFont(new Font(labelFont.getName(), labelFont.getStyle(), 60));
        
        this.add(eventCount, cl);
        
        this.setOpaque(false);
    }
    
    public void incrementCount() {
        
        int count = Integer.parseInt(eventCount.getText());
        count++;
        eventCount.setText(String.valueOf(count));
        
        this.revalidate();
    }
}
