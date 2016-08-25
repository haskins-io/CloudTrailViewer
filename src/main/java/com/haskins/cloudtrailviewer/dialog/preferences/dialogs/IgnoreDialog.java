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

package com.haskins.cloudtrailviewer.dialog.preferences.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mark.haskins
 */
public class IgnoreDialog extends JDialog implements ActionListener {
    
    private static IgnoreDialog dialog;
    private static final long serialVersionUID = -7512275475148979297L;
    
    private final JTextField needle = new JTextField();
    
    private static String ignore_value = null;
    
    /**
     * Shows the Dialog
     * @param parent Component to be used as the Parent for positioning of the dialog
     * @return String value that should be ignored
     */
    public static String showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new IgnoreDialog(frame);
        dialog.setVisible(true);
        
        return ignore_value;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            ignore_value = needle.getText();
        }
        
        IgnoreDialog.dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////
    private IgnoreDialog(Frame frame) {

        super(frame, "Scan Ignores", true);
        
        this.setResizable(false);
                        
        final JButton btnLoad = new JButton("OK");
        btnLoad.setActionCommand("OK");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setActionCommand("Cancel");
        btnCancel.addActionListener(this);
                
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnCancel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnLoad);
                         
        JPanel contents = new JPanel(new BorderLayout());
        contents.add(new JLabel("Ignore"), BorderLayout.LINE_START);
        contents.add(needle, BorderLayout.CENTER);
        
        Container contentPane = getContentPane();
        contentPane.add(contents, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(frame);  
    }
}
