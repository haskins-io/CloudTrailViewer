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
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author mark.haskins
 */
public class AnalysisOverviewPanel extends JPanel implements EventsDatabaseListener {
    
    private final EventsDatabase eventsDatabase;
   
    private final DefaultPieDataset sourceDataset = new DefaultPieDataset();
    private final DefaultPieDataset typeDataset = new DefaultPieDataset();
    private final DefaultPieDataset identityDataset = new DefaultPieDataset();
    private final DefaultPieDataset nameDataset = new DefaultPieDataset();
    
    List<ChartPanel> chartPanels = new ArrayList<>();
    
    public AnalysisOverviewPanel(EventsDatabase database) {
        
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
            ChartCreator.updateChartDataset(event.getUserIdentity().getUserName(), nameDataset);
        }
        
        reloadCharts();
        
        this.setVisible(true);
    }
    
    private void buildUI() {
        
        chartPanels.add(ChartCreator.createPieChart("Sources", sourceDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Types", typeDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Identity", identityDataset, 200, 200));
        chartPanels.add(ChartCreator.createPieChart("Name", nameDataset, 200, 200));
        
        this.setLayout(new GridLayout());
        
        for (ChartPanel panel : chartPanels) {
            
            this.add(panel);
        }
        
        this.setVisible(false);
    }
    
    private void clearDatasets() {
        
        sourceDataset.clear();
        typeDataset.clear();
        identityDataset.clear();
        nameDataset.clear();
    }
        
    private void reloadCharts() {
        
        for (ChartPanel panel : chartPanels) {
            panel.getChart().fireChartChanged();
        }
    }
}
