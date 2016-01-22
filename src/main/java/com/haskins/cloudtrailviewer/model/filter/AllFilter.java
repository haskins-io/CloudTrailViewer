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

package com.haskins.cloudtrailviewer.model.filter;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mark
 */
public class AllFilter extends AbstractFilter implements Serializable {

    private static final long serialVersionUID = -2890390951885621912L;

    ///////////////////////////////////////////////////////////////////////////
    // Filter overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (needle == null || needle.trim().length() == 0) {
            passesFilter = true;
            
        } else {
            
            String lowerJSON = event.toString().toLowerCase();
            String lowerFilter = this.needle.toLowerCase();

            if (lowerJSON.contains(lowerFilter)) {
                passesFilter = true;
            }
        }
        
        return passesFilter;
    }
    
    @Override
    public JPanel getFilterPanel(String name) {
        
        JPanel ui = new JPanel(new BorderLayout());
        ui.add(new JLabel("Find "), BorderLayout.LINE_START);
        
        final JTextField textField = new JTextField();
        textField.addKeyListener(new KeyListener(){
            
            @Override
            public void keyTyped(KeyEvent e) {
                needle = textField.getText();
            }

            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }
            
        });
        
        ui.add(textField, BorderLayout.CENTER);
        
        ui.setMinimumSize(DEFAULT_SIZE);
        ui.setPreferredSize(DEFAULT_SIZE);
        ui.setMaximumSize(DEFAULT_SIZE);
        
        return ui;
    }
    
    @Override
    public boolean isNeedleSet() {
        
        boolean needleSet = false;
        
        if (needle != null && needle.length() > 0) {
            needleSet = true;
        }
        
        return needleSet;
    }
        
    ///////////////////////////////////////////////////////////////////////////
    // Other overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "All";
    }
    
}
