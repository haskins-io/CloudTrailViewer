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
package com.haskins.cloudtrailviewer.feature.user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class CountPanel extends JPanel {
    
    private final JLabel countLabel = new JLabel();
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
                        
        g.setColor(new Color(255, 251, 208));
        g.fillOval(0, 3, countLabel.getWidth(), 20);
    }
    
    public CountPanel(int count) {
                        
        this.countLabel.setText(String.valueOf(count));
        this.setBackground(Color.white);
        this.setOpaque(true);
        
        FontMetrics metrics = countLabel.getFontMetrics(countLabel.getFont());
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(countLabel.getText());
        Dimension size = new Dimension(adv+10, hgt);
        
        countLabel.setMinimumSize(size);
        countLabel.setMaximumSize(size);
        countLabel.setPreferredSize(size);
        
        GridBagConstraints cl = new GridBagConstraints();
        this.add(this.countLabel, cl);
    }
}
