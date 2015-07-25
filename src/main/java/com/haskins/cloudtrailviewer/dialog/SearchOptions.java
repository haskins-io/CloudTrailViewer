/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.dialog;

import com.haskins.cloudtrailviewer.components.SearchPanel;
import com.haskins.cloudtrailviewer.model.filter.Filter;
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
 * @author mark
 */
public class SearchOptions extends JDialog implements ActionListener {
    
    private static final int SCAN_CANCELLED = 0;
    public static final int SCAN_OK = 1; 
    
    private static SearchOptions dialog;
    private static final SearchPanel searchPanel = new SearchPanel();
    
    private static int CLOSE_STATE = SCAN_CANCELLED;
    
    public static int showDialog(Component parent) {
        
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new SearchOptions(frame);
        dialog.setVisible(true);
        
        return CLOSE_STATE;
    }
    
    public static Filter getSearchFilter() {
        return searchPanel.getFilter();
    }
        
    private SearchOptions(Frame frame) {

        super(frame, "Scan", true);
        
        this.setResizable(false);
        this.setSize(400, 200);
                        
        final JButton btnLoad = new JButton("OK");
        btnLoad.setActionCommand("OK");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(btnCancel);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(btnLoad);
        
        Container contentPane = getContentPane();
        contentPane.add(searchPanel, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        
        pack();
        setLocationRelativeTo(frame);  
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("OK".equals(e.getActionCommand())) {
            CLOSE_STATE = SCAN_OK;
        }
        else {
            CLOSE_STATE = SCAN_CANCELLED;
        }
        
        SearchOptions.dialog.setVisible(false);
    }
}
