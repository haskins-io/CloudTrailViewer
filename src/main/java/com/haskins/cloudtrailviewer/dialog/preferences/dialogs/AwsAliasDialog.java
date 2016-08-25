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

import com.haskins.cloudtrailviewer.model.AwsAlias;
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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mark
 */
public class AwsAliasDialog extends JDialog implements ActionListener {
    
    private static AwsAliasDialog dialog;
    private static final long serialVersionUID = 7401945055664690976L;
    
    private final JTextField accountNumber = new JTextField();
    private final JTextField accountAlias = new JTextField();
    
    private static AwsAlias alias = null;
    
    /**
     * Shows the Dialog
     * @param parent Component to be used as Parent for positioning the dialog
     * @return AWS Alias object
     */
    public static AwsAlias showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new AwsAliasDialog(frame);
        dialog.setVisible(true);
        
        return alias;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            
            alias = new AwsAlias(accountNumber.getText(), accountAlias.getText());
            
            // check values first
            AwsAliasDialog.dialog.setVisible(false);
        
        } else {
            AwsAliasDialog.dialog.setVisible(false);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////
    private AwsAliasDialog(Frame frame) {

        super(frame, "AWS Account Alias", true);
        
        this.setResizable(false);
                        
        final JButton btnLoad = new JButton("OK");
        btnLoad.setActionCommand("OK");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
                
        //Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnCancel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnLoad);
         
        JPanel preferencesPanel = new JPanel();
        GroupLayout layout = new GroupLayout(preferencesPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        preferencesPanel.setLayout(layout);
        preferencesPanel.setMinimumSize(new Dimension(500,100));
        preferencesPanel.setMaximumSize(new Dimension(500,100));
        preferencesPanel.setPreferredSize(new Dimension(500,100));
       
        JLabel lblAccount = new JLabel("Account Number");
        JLabel lblAlias = new JLabel("Alias");
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(
            layout.createParallelGroup().
                addComponent(lblAccount).
                addComponent(lblAlias)
            );
        
        hGroup.addGroup(
            layout.createParallelGroup().
                addComponent(accountNumber).
                addComponent(accountAlias)
            );

        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(
            layout.createParallelGroup(Alignment.BASELINE).
                addComponent(lblAccount).addComponent(accountNumber)
            );
        
        vGroup.addGroup(
            layout.createParallelGroup(Alignment.BASELINE).
                addComponent(lblAlias).addComponent(accountAlias)
            );
        
        layout.setVerticalGroup(vGroup);
                
        Container contentPane = getContentPane();
        contentPane.add(preferencesPanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(frame);  
    }
}
