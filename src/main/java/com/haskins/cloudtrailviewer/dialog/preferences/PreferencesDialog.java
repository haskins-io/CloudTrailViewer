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
package com.haskins.cloudtrailviewer.dialog.preferences;

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
import javax.swing.JTabbedPane;

/**
 *
 * @author mark
 */
public class PreferencesDialog extends JDialog implements ActionListener {

    private static PreferencesDialog dialog;
    private static final long serialVersionUID = 4307269898547650497L;
    
    private final JTabbedPane tPane = new JTabbedPane();
    
    /**
     * Shows the Dialog
     * @param parent Component to be used as the Parent when positioning the dialog
     */
    public static void showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new PreferencesDialog(frame);
        dialog.setVisible(true);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            
            for (int i = 0; i < tPane.getComponentCount(); i++) {
                
                Preferences p = (Preferences)tPane.getComponentAt(i);
                p.savePreferences();
            }
        }
        
        PreferencesDialog.dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private PreferencesDialog(Frame frame) {
        
        super(frame, "Preferences", true);
        
        this.setLayout(new BorderLayout());
        this.setResizable(true);
        this.setMinimumSize(new Dimension(400,500));
        this.setPreferredSize(new Dimension(400,500));
        
        final JButton btnOK = new JButton("OK");
        btnOK.setActionCommand("OK");
        btnOK.addActionListener(this);
        getRootPane().setDefaultButton(btnOK);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnCancel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnOK);
        
        Container contentPane = getContentPane();
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        addPreferencesPanel();
        
        pack();
        setLocationRelativeTo(frame);  
    }
    
    private void addPreferencesPanel() {
        
        AwsPanel awsPreferences = new AwsPanel();
        tPane.add("AWS", awsPreferences);
        
        SecurityPanel security = new SecurityPanel();
        tPane.add("Security", security);
        
        ResourcePanel resources = new ResourcePanel();
        tPane.add("Resources", resources);
        
        IgnorePanel ignores = new IgnorePanel();
        tPane.add("Scan Ignores", ignores);
        
        Container contentPane = getContentPane();
        contentPane.add(tPane, BorderLayout.CENTER);
    }
    
}
