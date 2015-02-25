/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.model.ChartData;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class ChartDialog extends JDialog implements ActionListener {
    
    private final String[] type = {"Top"};
    private final String[] styles = {"Pie", "Bar"};
    private final String[] sources = {
        "EventName", "EventSource", "SourceIPAddress", "UserAgent", "AwsRegion", "ErrorCode", "ErrorMessage", "EventType",
        "UserIdentity.PrincipalId", "UserIdentity.Arn", "UserIdentity.UserName", "UserIdentity.InvokedBy", "UserIdentity.AccessKeyId"
    };
    
    private static ChartDialog dialog;
    
    private final JComboBox chartStyleCombo;
    private final JComboBox chartSourceCombo;
    private final JComboBox chartTypeCombo;
    
    private final JCheckBox ignoreRootCheckBox;
    
    private static ChartData chartData = null;
    
    public static ChartData showDialog(Component parent) {
        
        chartData = null;
            
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new ChartDialog(frame);
        dialog.setVisible(true);
        
        return chartData;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ("Show".equals(e.getActionCommand())) {
            
            chartData = new ChartData();
            chartData.setChartSource(chartSourceCombo.getSelectedItem().toString());
            chartData.setChartStyle(chartStyleCombo.getSelectedItem().toString());
            chartData.setChartType(chartTypeCombo.getSelectedItem().toString());
            
            chartData.setIgnoreRoot(ignoreRootCheckBox.isSelected());
        }
        
        ChartDialog.dialog.setVisible(false);
    }
    
    private ChartDialog(Frame frame) {

        super(frame, "New Chart", true);
                
        final JButton btnLoad = new JButton("Show");
        btnLoad.setActionCommand("Show");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(btnCancel);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnLoad);
 
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4,2));
        
        chartStyleCombo = new JComboBox(styles);
        chartSourceCombo = new JComboBox(sources);
        chartTypeCombo = new JComboBox(type);
        
        ignoreRootCheckBox = new JCheckBox();
        
        optionsPanel.add(new JLabel("Chart Style"));
        optionsPanel.add(chartStyleCombo);
        optionsPanel.add(new JLabel("Chart Source"));
        optionsPanel.add(chartSourceCombo);
        optionsPanel.add(new JLabel("Chart Type"));
        optionsPanel.add(chartTypeCombo);
        
        optionsPanel.add(new JLabel("Ignore Root"));
        optionsPanel.add(ignoreRootCheckBox);
        
        Container contentPane = getContentPane();
        contentPane.add(optionsPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);
 
        pack();
        setLocationRelativeTo(frame);  
    }
}
