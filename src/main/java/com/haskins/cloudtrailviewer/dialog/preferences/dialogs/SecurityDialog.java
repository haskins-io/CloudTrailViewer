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

import com.haskins.cloudtrailviewer.components.ServiceApiPanel;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class SecurityDialog extends JDialog implements ActionListener {
    
    private static SecurityDialog dialog;
    private static final long serialVersionUID = -7512275475148979297L;
    
    private final ServiceApiPanel servicePanel = new ServiceApiPanel();
    
    private static String eventApi = null;
    
    /**
     * Shows the Dialog
     * @param parent 
     * @return
     */
    public static String showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new SecurityDialog(frame);
        dialog.setVisible(true);
        
        return eventApi;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            eventApi = servicePanel.getApi();
        }
        
        SecurityDialog.dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////
    private SecurityDialog(Frame frame) {

        super(frame, "API Security", true);
        
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
                         
        Container contentPane = getContentPane();
        contentPane.add(servicePanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(frame);  
    }
}
