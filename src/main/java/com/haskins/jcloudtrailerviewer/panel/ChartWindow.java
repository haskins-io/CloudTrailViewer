package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.util.ChartCreator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;

/**
 *
 * @author mark.haskins
 */
public class ChartWindow extends JInternalFrame {
    
    private final List<Entry<String,Integer>> events;
    private final ChartData chartData;
    
    public ChartWindow(ChartData chartData, List<Entry<String,Integer>> data) {
        
        super(chartData.getChartSource(), true, true, false, true);
        
        this.chartData = chartData;
        events = data;
        
        buildGui();
    }
    
    private void buildGui() {
        
        this.setLayout(new BorderLayout());
        
        this.setTitle(chartData.getChartSource());
        this.setSize(500, 280);
        
        if (!events.isEmpty()) {
            
            JTabbedPane tabs = new JTabbedPane();
            
            addChart(tabs);
            addTable(tabs);
            addData(tabs);
            
            this.add(tabs, BorderLayout.CENTER);
        }
        else {
            this.add(new JLabel("No Data"), BorderLayout.CENTER);
        }
    }
    
    private void addChart(JTabbedPane panel) {
                
        final ChartPanel chartPanel;
        
        if (chartData.getChartStyle().equalsIgnoreCase("Pie")) {

            chartPanel = ChartCreator.createPieChart(events, 480, 260);

        } else if (chartData.getChartStyle().equalsIgnoreCase("Bar")) {

            chartPanel = ChartCreator.createBarChart(
                    events, 
                    480, 260,
                    chartData.getChartSource(), "Count",
                    PlotOrientation.VERTICAL);
        } else {
            chartPanel = null;
        }
        
        if (chartPanel != null) {
                        
//            JPopupMenu chartMenu = chartPanel.getPopupMenu();
//            JMenuItem newItem = new JMenuItem(new AbstractAction("Drill Down") {
//            
//                @Override
//                public void actionPerformed(ActionEvent t) {                    
//                }
//            });
//            chartMenu.add(newItem);

            panel.addTab("Chart", chartPanel);
        }
    }
    
    private void addTable(JTabbedPane panel) {
        
        DefaultTableModel tableModel = new DefaultTableModel();
        
        JTable table = new JTable(tableModel);
        table.setPreferredSize(new Dimension(480, 260));
        
        tableModel.addColumn("Property");
        tableModel.addColumn("Value");
                
        for (Entry entry : events) {
            tableModel.addRow(new Object[] { entry.getKey(), entry.getValue() });
        }
        
        panel.addTab("Table", table);
    }
    
    private void addData(JTabbedPane panel) {
        
        String newline = "\n";
        
        JTextArea dataTextArea = new JTextArea();
        dataTextArea.setPreferredSize(new Dimension(600, 440));
        dataTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        
        StringBuilder dataString = new StringBuilder();
        for (Entry entry : events) {
               
            dataString.append(entry.getKey()).append(" : ").append(entry.getValue()).append(newline);
        }
        
        dataTextArea.setText(dataString.toString());
        
        panel.addTab("Data", dataTextArea);
    }
}
