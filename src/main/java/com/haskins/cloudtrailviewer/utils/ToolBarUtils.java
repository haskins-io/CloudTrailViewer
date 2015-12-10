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

package com.haskins.cloudtrailviewer.utils;

import java.awt.Cursor;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author mark
 */
public class ToolBarUtils {

    public static void addImageToButton(JButton theButton, String image, String text, String tooltip) {
            
        theButton.setIcon(ToolBarUtils.getIcon(image));
        theButton.setBorder(null);
        theButton.setBorderPainted(false);
        theButton.setMargin(new Insets(0,0,0,0));
        theButton.setFocusPainted(false);
        theButton.setContentAreaFilled(false);
        theButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        theButton.setToolTipText(tooltip);
    }
    
    public static Icon getIcon(String name) {
        
        ClassLoader cl = ToolBarUtils.class.getClassLoader();
        
        return new ImageIcon(cl.getResource("icons/" + name));
    }
}
