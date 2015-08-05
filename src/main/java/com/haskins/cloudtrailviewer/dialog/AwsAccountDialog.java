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

package com.haskins.cloudtrailviewer.dialog;

import com.haskins.cloudtrailviewer.core.PreferencesController;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * AWS Account dialog.
 * 
 * @author mark
 */
public class AwsAccountDialog extends JDialog implements ActionListener {

    public static final String S3_BUCKET_PROPERTY = "aws.bucket";
    public static final String S3_KEY_PROPERTY = "aws.key";
    public static final String S3_SECRET_PROPERTY = "aws.secret";
    
    private static AwsAccountDialog dialog;
    
    private final JTextField bucket = new JTextField();
    private final JTextField key = new JTextField();
    private final JPasswordField secret = new JPasswordField();
    
    /**
     * Shows the Dialog
     * @param parent 
     */
    public static void showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new AwsAccountDialog(frame);
        dialog.setVisible(true);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            
            PreferencesController.getInstance().setProperty(S3_BUCKET_PROPERTY, bucket.getText());
            PreferencesController.getInstance().setProperty(S3_KEY_PROPERTY, key.getText());
            PreferencesController.getInstance().setProperty(S3_SECRET_PROPERTY, String.valueOf(secret.getPassword()));
        }
        
        AwsAccountDialog.dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////
    private AwsAccountDialog(Frame frame) {

        super(frame, "AWS Account", true);
        
        this.setResizable(false);
                        
        final JButton btnLoad = new JButton("OK");
        btnLoad.setActionCommand("OK");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        // set existing values
        bucket.setText(PreferencesController.getInstance().getProperty(S3_BUCKET_PROPERTY));
        key.setText(PreferencesController.getInstance().getProperty(S3_KEY_PROPERTY));
        secret.setText(PreferencesController.getInstance().getProperty(S3_SECRET_PROPERTY));
        
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
       
        JLabel lblBucket = new JLabel("Bucket Name");
        JLabel lblKey = new JLabel("IAM Key");
        JLabel lblSecret = new JLabel("IAM Secret");
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(
            layout.createParallelGroup().
                addComponent(lblBucket).
                addComponent(lblKey).
                addComponent(lblSecret)
            );
        
        hGroup.addGroup(
            layout.createParallelGroup().
                addComponent(bucket).
                addComponent(key).
                addComponent(secret)
            );

        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(
            layout.createParallelGroup(Alignment.BASELINE).
                addComponent(lblBucket).addComponent(bucket)
            );
        
        vGroup.addGroup(
            layout.createParallelGroup(Alignment.BASELINE).
                addComponent(lblKey).addComponent(key)
            );
        
        vGroup.addGroup(
            layout.createParallelGroup(Alignment.BASELINE).
                addComponent(lblSecret).addComponent(secret)
            );
        
        layout.setVerticalGroup(vGroup);
                
        Container contentPane = getContentPane();
        contentPane.add(preferencesPanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(frame);  
    }
    
}
