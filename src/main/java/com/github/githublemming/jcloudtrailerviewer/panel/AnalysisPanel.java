package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import com.github.githublemming.jcloudtrailerviewer.util.ChartCreator;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author mark.haskins
 */
public class AnalysisPanel extends JPanel implements EventsDatabaseListener {
    
    private CopyOnWriteArrayList<Event> filteredEvents = new CopyOnWriteArrayList<>();
    
    private final DefaultPieDataset sourceDataset = new DefaultPieDataset();
    private final DefaultPieDataset typeDataset = new DefaultPieDataset();
    private final DefaultPieDataset nameDataset = new DefaultPieDataset();
    private final DefaultPieDataset regionDataset = new DefaultPieDataset();
    private final DefaultPieDataset sourceIpDataset = new DefaultPieDataset();
    private final DefaultPieDataset userAgentDataset = new DefaultPieDataset();
    private final DefaultPieDataset erroMessageDataset = new DefaultPieDataset();
    private final DefaultPieDataset usernameDataset = new DefaultPieDataset();
    private final DefaultPieDataset accountIdDataset = new DefaultPieDataset();
    private final DefaultPieDataset principalIDDataset = new DefaultPieDataset();
    private final DefaultPieDataset accessKeyDataset = new DefaultPieDataset();
    private final DefaultPieDataset arnDataset = new DefaultPieDataset();
    private final DefaultPieDataset invokedByDataset = new DefaultPieDataset();
    
    private final DefaultCategoryDataset transactionPerSecond = new DefaultCategoryDataset();
    
    private final List<ChartPanel> chartPanels = new ArrayList<>();
    
    public AnalysisPanel() {
        
        buildUI();
    }
    
    @Override
    public void onEventsUpdated(CopyOnWriteArrayList<Event> events) {
        
        filteredEvents = events;
        
        Thread updateCharts = new Thread() {
            @Override
            public void run() {
                clearDatasets();

                for (Event event : filteredEvents) {

                    updatePieCharts(event);
                }

                reloadCharts();
            };
        };
        updateCharts.start(); 
    }
    
    private void buildUI() {
        
        this.setLayout(new BorderLayout());
        
        JPanel pieChartPanel = new JPanel();
        pieChartPanel.setLayout(new GridLayout(4,4));
        
        // Pie Charts
        chartPanels.add(ChartCreator.createPieChart("Sources", sourceDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Types", typeDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Event Name", nameDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Region", regionDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Source IP", sourceIpDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("User Agent", userAgentDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Error Message", erroMessageDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Username", usernameDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Account Id", accountIdDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Principal Id", principalIDDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Access Key", accessKeyDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Arn", arnDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Invoked By", invokedByDataset, 200, 200));
        
        for (ChartPanel panel : chartPanels) {
            pieChartPanel.add(panel);
        }
        
        this.add(pieChartPanel, BorderLayout.CENTER);
        
        // Line Chart
        ChartPanel tpsChart = ChartCreator.createLineChart("TPS", transactionPerSecond, 100, 200);
        chartPanels.add(tpsChart);
        this.add(tpsChart, BorderLayout.SOUTH);
    }
    
    private void updatePieCharts(Event event) {
        
            ChartCreator.updatePieChartDataset(event.getEventSource(), sourceDataset);
            ChartCreator.updatePieChartDataset(event.getEventType(), typeDataset);
            ChartCreator.updatePieChartDataset(event.getEventName(), nameDataset);
            ChartCreator.updatePieChartDataset(event.getAwsRegion(), regionDataset);
            ChartCreator.updatePieChartDataset(event.getSourceIPAddress(), sourceIpDataset);
            ChartCreator.updatePieChartDataset(event.getUserAgent(), userAgentDataset);
            ChartCreator.updatePieChartDataset(event.getErrorMessage(), erroMessageDataset);
            ChartCreator.updatePieChartDataset(event.getUserIdentity().getUserName(), usernameDataset);
            ChartCreator.updatePieChartDataset(event.getUserIdentity().getAccountId(), accountIdDataset);
            ChartCreator.updatePieChartDataset(event.getUserIdentity().getPrincipalId(), principalIDDataset);
            ChartCreator.updatePieChartDataset(event.getUserIdentity().getAccessKeyId(), accessKeyDataset);
            ChartCreator.updatePieChartDataset(event.getUserIdentity().getArn(), arnDataset);
            ChartCreator.updatePieChartDataset(event.getUserIdentity().getInvokedBy(), invokedByDataset);  
            
            ChartCreator.updateLineChartDataset("TPS", event.getEventTime(), transactionPerSecond);  
        
    }
    
    private void clearDatasets() {
       
        sourceDataset.clear();
        typeDataset.clear();
        nameDataset.clear();
        regionDataset.clear();
        sourceIpDataset.clear();
        userAgentDataset.clear();
        erroMessageDataset.clear();
        usernameDataset.clear();
        accountIdDataset.clear();
        principalIDDataset.clear();
        accessKeyDataset.clear();
        arnDataset.clear();
        invokedByDataset.clear();
    }
        
    private void reloadCharts() {
        
        for (ChartPanel panel : chartPanels) {
            panel.getChart().fireChartChanged();
        }
    }
}
