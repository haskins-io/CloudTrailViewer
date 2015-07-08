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

package com.haskins.jcloudtrailerviewer.components;

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
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Provides an implementation of JDialog that allows the user to define the
 * attributes of a chart.
 * 
 * @author mark.haskins
 */
public class ChartDialog extends JDialog implements ActionListener {
    
    private final String[] type = {"Top"};
    private final String[] styles = {"Pie", "Pie3d", "Bar", "Bar3d"};
    private final String[] sources = {
        "EventName", "EventSource", "SourceIPAddress", "UserAgent", "AwsRegion", "ErrorCode", "ErrorMessage", "EventType",
        "UserIdentity.PrincipalId", "UserIdentity.Arn", "UserIdentity.UserName", "UserIdentity.InvokedBy", "UserIdentity.AccessKeyId"
    };
    
    private static ChartDialog dialog;
    
    private final JComboBox chartStyleCombo;
    private final JComboBox chartSourceCombo;
    private final JComboBox chartTypeCombo;
    
    private final JRadioButton allRadioButton;
    private final JRadioButton ignoreRootRadio;
    private final JRadioButton justRootRadio;
    
    private static ChartData chartData = null;
    
    /**
     * shows the dialog.
     * @param parent The parent window for the dialog to be associated with.
     * @return A ChartData object containing the required chart configuration.
     */
    public static ChartData showDialog(Component parent) {
        
        chartData = null;
            
        Frame frame = JOptionPane.getFrameForComponent(parent);
        dialog = new ChartDialog(frame);
        dialog.setVisible(true);
        
        return chartData;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "Cancel":
                ChartDialog.dialog.setVisible(false);
                break;
            case "Show":
                chartData = new ChartData();
                chartData.setChartSource(chartSourceCombo.getSelectedItem().toString());
                chartData.setChartStyle(chartStyleCombo.getSelectedItem().toString());
                chartData.setChartType(chartTypeCombo.getSelectedItem().toString());
                chartData.setIgnoreRoot(ignoreRootRadio.isSelected());
                chartData.setJustRoot(justRootRadio.isSelected());
                
                ChartDialog.dialog.setVisible(false);
                break;
            case "TypeChange":
                System.out.println("Top Changned : " + chartTypeCombo.getSelectedItem().toString() );
                break;
                
        }           
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // private methods
    ////////////////////////////////////////////////////////////////////////////
    private ChartDialog(Frame frame) {

        super(frame, "New Chart", true);
                
        final JButton btnLoad = new JButton("Show");
        btnLoad.setActionCommand("Show");
        btnLoad.addActionListener(this);
        getRootPane().setDefaultButton(btnLoad);
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setActionCommand("Cancel");
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
        chartTypeCombo.setActionCommand("TypeChange");
        chartTypeCombo.addActionListener(this);
                
        optionsPanel.add(new JLabel("Chart Style"));
        optionsPanel.add(chartStyleCombo);
        optionsPanel.add(new JLabel("Chart Source"));
        optionsPanel.add(chartSourceCombo);
        optionsPanel.add(new JLabel("Chart Type"));
        optionsPanel.add(chartTypeCombo);
        
        ignoreRootRadio = new JRadioButton();
        justRootRadio = new JRadioButton();
        allRadioButton = new JRadioButton();
        allRadioButton.setSelected(true);
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(allRadioButton);
        buttonGroup.add(ignoreRootRadio);
        buttonGroup.add(justRootRadio);
        
        JPanel rootPanel = new JPanel();
        rootPanel.add(new JLabel("All"));
        rootPanel.add(allRadioButton);
        rootPanel.add(new JLabel("Ignore Root"));
        rootPanel.add(ignoreRootRadio);
        rootPanel.add(new JLabel("Just Root"));
        rootPanel.add(justRootRadio);
                
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new BorderLayout());
        layoutPanel.add(optionsPanel, BorderLayout.CENTER);
        layoutPanel.add(rootPanel, BorderLayout.SOUTH);
        
        Container contentPane = getContentPane();
        contentPane.add(layoutPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);
 
        pack();
        setLocationRelativeTo(frame);  
    }
}
