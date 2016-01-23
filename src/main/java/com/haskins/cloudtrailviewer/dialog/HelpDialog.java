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
package com.haskins.cloudtrailviewer.dialog;

import com.haskins.cloudtrailviewer.core.PropertiesController;
import com.haskins.cloudtrailviewer.model.Help;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Dialog that shows any help page associated with a Feature
 * 
 * @author mark
 */
public class HelpDialog extends JDialog implements ActionListener {
    
    private final static Logger LOGGER = Logger.getLogger("CloudTrail");
    
    private static HelpDialog dialog;
    private static final long serialVersionUID = -1161803908200818777L;
    
    private final JEditorPane helpPane = new JEditorPane();
    private final HTMLEditorKit kit = new HTMLEditorKit();
    
    /**
     * Shows the Dialog
     * @param parent 
     * @param help
     */
    public static void showDialog(Component parent, Help help) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new HelpDialog(frame, help);
        dialog.setVisible(true);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        HelpDialog.dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////
    private HelpDialog(Frame frame, Help help) {
     
        super(frame, help.getHelpTitle(), true);
        
        this.setMinimumSize(new Dimension(800,600));
        this.setMaximumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(800,600));
        
        helpPane.setEditable(false);
        helpPane.setEditorKit(kit);
        
        JScrollPane sp = new JScrollPane(helpPane);
        
        defineStyles();
        
        String helpFile = "help/" + help.getHelpFilename() + ".html";
        
        ClassLoader cl = PropertiesController.class.getClassLoader();
        try  {
            URL url = cl.getResource(helpFile);
            
            if(url==null){
                System.out.println("Sorry, unable to find " + helpFile);
            }
            
            helpPane.setPage(url);
            
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "Problem loading help file " + helpFile, ioe);
        }
        
        Container contentPane = getContentPane();
        contentPane.add(sp, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(frame); 
    }
    
    private void defineStyles() {

        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {color:#000; font-family:times; margin: 5px; }");
        
        Document doc = kit.createDefaultDocument();
        helpPane.setDocument(doc);
    }
}
