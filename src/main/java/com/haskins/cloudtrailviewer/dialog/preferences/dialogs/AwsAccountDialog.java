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

package com.haskins.cloudtrailviewer.dialog.preferences.dialogs;

import com.haskins.cloudtrailviewer.model.AwsAccount;
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
    
    private final JTextField name = new JTextField();
    private final JTextField bucket = new JTextField();
    private final JTextField key = new JTextField();
    private final JPasswordField secret = new JPasswordField();
    
    private static AwsAccount account = null;
    
    /**
     * Shows the Dialog
     * @param parent 
     * @return
     */
    public static AwsAccount showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new AwsAccountDialog(frame);
        dialog.setVisible(true);
        
        return account;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            
            account = new AwsAccount(
                    name.getText(), 
                    bucket.getText(),
                    key.getText(),
                    String.valueOf(secret.getPassword()),
                    ""
            );
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
         
        JPanel accountPanel = new JPanel();
        GroupLayout layout = new GroupLayout(accountPanel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        accountPanel.setLayout(layout);
        accountPanel.setMinimumSize(new Dimension(500,150));
        accountPanel.setMaximumSize(new Dimension(500,150));
        accountPanel.setPreferredSize(new Dimension(500,150));
       
        JLabel lblName = new JLabel("Name");
        JLabel lblBucket = new JLabel("Bucket Name");
        JLabel lblKey = new JLabel("IAM Key");
        JLabel lblSecret = new JLabel("IAM Secret");
        
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(
            layout.createParallelGroup().
                addComponent(lblName).
                addComponent(lblBucket).
                addComponent(lblKey).
                addComponent(lblSecret)
            );
        
        hGroup.addGroup(
            layout.createParallelGroup().
                addComponent(name).
                addComponent(bucket).
                addComponent(key).
                addComponent(secret)
            );

        layout.setHorizontalGroup(hGroup);
        
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(
            layout.createParallelGroup(Alignment.BASELINE).
                addComponent(lblName).addComponent(name)
            );
        
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
        contentPane.add(accountPanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(frame);  
    }
    
}
