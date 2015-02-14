package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.event.EventsDatabaseListener;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import com.github.githublemming.jcloudtrailerviewer.util.ChartCreator;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author mark.haskins
 */
public class AnalysisOverviewPanel extends JPanel implements EventsDatabaseListener {
    
    private final List<ChartPanel> chartPanels = new ArrayList<>();
    
    private final DefaultPieDataset sourceDataset = new DefaultPieDataset();
    private final DefaultPieDataset typeDataset = new DefaultPieDataset();
    private final DefaultPieDataset identityDataset = new DefaultPieDataset();
    private final DefaultPieDataset nameDataset = new DefaultPieDataset();
    
    private CopyOnWriteArrayList<Event> filteredEvents = new CopyOnWriteArrayList<>();
    
    public AnalysisOverviewPanel() {

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

                    ChartCreator.updatePieChartDataset(event.getEventSource(), sourceDataset);
                    ChartCreator.updatePieChartDataset(event.getEventType(), typeDataset);
                    ChartCreator.updatePieChartDataset(event.getEventName(), nameDataset);
                    ChartCreator.updatePieChartDataset(event.getUserIdentity().getUserName(), nameDataset);
                }

                reloadCharts();
            }
        };
        updateCharts.start(); 
        
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
