/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.dialog;

import com.haskins.cloudtrailviewer.core.PropertiesController;
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
public class AwsAccount extends JDialog implements ActionListener {

    public static final String S3_BUCKET_PROPERTY = "aws.bucket";
    public static final String S3_KEY_PROPERTY = "aws.key";
    public static final String S3_SECRET_PROPERTY = "aws.secret";
    
    private static AwsAccount dialog;
    
    private final JTextField bucket = new JTextField();
    private final JTextField key = new JTextField();
    private final JTextField secret = new JTextField();
    
    public static void showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new AwsAccount(frame);
        dialog.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            
            PropertiesController.getInstance().setProperty(S3_BUCKET_PROPERTY, bucket.getText());
            PropertiesController.getInstance().setProperty(S3_KEY_PROPERTY, key.getText());
            PropertiesController.getInstance().setProperty(S3_SECRET_PROPERTY, secret.getText());
        }
        
        AwsAccount.dialog.setVisible(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////
    private AwsAccount(Frame frame) {

        super(frame, "AWS Account", true);
        
        this.setResizable(false);
                        
        final JButton btnLoad = new JButton("OK");
        btnLoad.setActionCommand("OK");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        
        // set existing values
        bucket.setText(PropertiesController.getInstance().getProperty(S3_BUCKET_PROPERTY));
        key.setText(PropertiesController.getInstance().getProperty(S3_KEY_PROPERTY));
        secret.setText(PropertiesController.getInstance().getProperty(S3_SECRET_PROPERTY));
        
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
