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

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class ServiceLabel extends JPanel {
    
    private final JLabel serviceName = new JLabel();
    
    public ServiceLabel(String name, int count) {
        
        this.setLayout(new BorderLayout());
        this.setBackground(Color.white);
        this.setOpaque(true);

        serviceName.setText(name);
          
        this.add(serviceName, BorderLayout.CENTER);
        this.add(new CountPanel(count), BorderLayout.EAST);
    }
    
    public String getServiceName() {
        return serviceName.getText();
    }
    
}
