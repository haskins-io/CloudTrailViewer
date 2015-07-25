/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.application;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author mark
 */
public class Menu extends JMenuBar {

    public Menu() {
        
        buildMenu();
    }
    
    private void buildMenu() {
        
        // -- Menu : File
        JMenu menuFile = new JMenu("File");
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                System.exit(0);
            }
        });
        
        menuFile.add(exit);
        
        this.add(menuFile);
    }
}
