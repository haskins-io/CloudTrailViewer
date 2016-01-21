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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mark
 */
public class AllFilter extends AbstractFilter implements Filter {

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
    public JPanel getFilterPanel() {
        
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
        
        return ui;
    }
        
    ///////////////////////////////////////////////////////////////////////////
    // Other overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "All";
    }
    
}
