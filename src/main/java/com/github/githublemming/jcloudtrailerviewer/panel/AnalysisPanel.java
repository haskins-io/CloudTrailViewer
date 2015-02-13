package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabase;
import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import com.github.githublemming.jcloudtrailerviewer.util.ChartCreator;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author mark.haskins
 */
public class AnalysisPanel extends JPanel implements EventsDatabaseListener {
    
    private final EventsDatabase eventsDatabase;
       
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
    
    List<ChartPanel> chartPanels = new ArrayList<>();
    
    public AnalysisPanel(EventsDatabase database) {
        
        eventsDatabase = database;
        eventsDatabase.addListeners(this);
        
        buildUI();
    }
    
    @Override
    public void onEventsUpdated(List<Event> events) {
        
        clearDatasets();
        
        for (Event event : events) {
            
            ChartCreator.updateChartDataset(event.getEventSource(), sourceDataset);
            ChartCreator.updateChartDataset(event.getEventType(), typeDataset);
            ChartCreator.updateChartDataset(event.getEventName(), nameDataset);
            ChartCreator.updateChartDataset(event.getAwsRegion(), regionDataset);
            ChartCreator.updateChartDataset(event.getSourceIPAddress(), sourceIpDataset);
            ChartCreator.updateChartDataset(event.getUserAgent(), userAgentDataset);
            ChartCreator.updateChartDataset(event.getErrorMessage(), erroMessageDataset);
            ChartCreator.updateChartDataset(event.getUserIdentity().getUserName(), usernameDataset);
            ChartCreator.updateChartDataset(event.getUserIdentity().getAccountId(), accountIdDataset);
            ChartCreator.updateChartDataset(event.getUserIdentity().getPrincipalId(), principalIDDataset);
            ChartCreator.updateChartDataset(event.getUserIdentity().getAccessKeyId(), accessKeyDataset);
            ChartCreator.updateChartDataset(event.getUserIdentity().getArn(), arnDataset);
            ChartCreator.updateChartDataset(event.getUserIdentity().getInvokedBy(), invokedByDataset);
        }
        
        reloadCharts();
    }
    
    private void buildUI() {
        
        this.setLayout(new GridLayout(4,4));
        
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
            this.add(panel);
        }
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
