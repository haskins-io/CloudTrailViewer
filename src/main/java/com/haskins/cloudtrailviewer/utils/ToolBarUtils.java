/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.utils;

import java.awt.Cursor;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author mark
 */
public class ToolBarUtils {

    public static void addImageToButton(JButton theButton, String image, String text, String tooltip) {
        
        ClassLoader cl = ToolBarUtils.class.getClassLoader();
        
        try {
            
            theButton.setIcon(new ImageIcon(cl.getResource("icons/" + image)));
        }
        catch (Exception e) {
            theButton.setText(text);
        }
        
        theButton.setBorder(null);
        theButton.setBorderPainted(false);
        theButton.setMargin(new Insets(0,0,0,0));
        theButton.setFocusPainted(false);
        theButton.setContentAreaFilled(false);
        theButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        theButton.setToolTipText(tooltip);
    }
}
