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
    
    private static ChartDialog dialog;
    
    private final JComboBox chartStyleCombo;
    private final JComboBox chartSourceCombo;
    private final JComboBox chartTypeCombo;
    
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
        optionsPanel.setLayout(new GridLayout(3,2));
        
        JLabel chartStyleLabel = new JLabel("Chart Style");
        String[] styles = {"Pie"};
        chartStyleCombo = new JComboBox(styles);
        
        JLabel chartSourceLabel = new JLabel("Chart Source");
        String[] sources = {"eventName", "eventSource", "sourceIPAddress", "userAgent", "principalId", "arn", "userName", "invokedBy"};
        chartSourceCombo = new JComboBox(sources);
        
        JLabel chartTypeLabel = new JLabel("Chart Type");
        String[] type = {"Top"};
        chartTypeCombo = new JComboBox(type);
        
        optionsPanel.add(chartStyleLabel);
        optionsPanel.add(chartStyleCombo);
        optionsPanel.add(chartSourceLabel);
        optionsPanel.add(chartSourceCombo);
        optionsPanel.add(chartTypeLabel);
        optionsPanel.add(chartTypeCombo);
        
        
        Container contentPane = getContentPane();
        contentPane.add(optionsPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.PAGE_END);
 
        pack();
        setLocationRelativeTo(frame);  
    }
}
