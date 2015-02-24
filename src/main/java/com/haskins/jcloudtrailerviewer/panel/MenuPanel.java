package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;

/**
 *
 * @author mark.haskins
 */
public class MenuPanel extends JMenuBar implements ActionListener {
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    private final EventsDatabase eventsDatabase;
    private final EventLoader eventLoader;
    
    public MenuPanel(EventLoader eventLoader, EventsDatabase database) {
        
        this.eventLoader = eventLoader;
        eventsDatabase = database;
        
        buildMenu();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String actionCommand = e.getActionCommand();
        
        switch(actionCommand) {
            case "LoadLocal":
                loadFiles();
                break;
            case "LoadS3":
                loadS3Files();
                break;
            case "EventsByService":
                showEventsByServiceChart();
                break;
            case "ServiceTps":
                showServiceTpsChart();
                break;
        }
    }
        
    private void buildMenu() {
        
        fileChooser.setMultiSelectionEnabled(true);
        
        // -- Menu : File
        JMenu menuFile = new JMenu("File");
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                System.exit(0);
            }
        });
        
        menuFile.add(exit);
        
        
        // -- Menu : Events
        JMenu menuEvents = new JMenu("Events");
        
        JMenuItem loadLocal = new JMenuItem("Load Local Files");
        loadLocal.setActionCommand("LoadLocal");
        loadLocal.addActionListener(this);
        
        JMenuItem loadS3 = new JMenuItem("Load S3 Files");
        loadS3.setActionCommand("LoadS3");
        loadS3.addActionListener(this);
        
        if (!PropertiesSingleton.getInstance().configLoaded()) {
            loadS3.setEnabled(false);
        }
        
        JMenuItem clearDatabase = new JMenuItem(new AbstractAction("Clear Events") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                eventsDatabase.clear();
            }
        });
        
        menuEvents.add(loadLocal);
        menuEvents.add(loadS3);
        menuEvents.addSeparator();
        menuEvents.add(clearDatabase);
        
        
        // -- Menu : Services
        JMenu menuServices = new JMenu("Services");
        
        JMenuItem eventsByService = new JMenuItem("Events by Service");
        eventsByService.setActionCommand("EventsByService");
        eventsByService.addActionListener(this);
        
        JMenuItem serviceTps = new JMenuItem("Service Tps");
        serviceTps.setActionCommand("ServiceTps");
        serviceTps.addActionListener(this);
        
        menuServices.add(eventsByService);
        menuServices.add(serviceTps);

        this.add(menuFile);
        this.add(menuEvents);
        this.add(menuServices);
    }
    
    private void loadFiles() {
        
        int status = fileChooser.showOpenDialog(jCloudTrailViewer.DESKTOP);
        if (status == JFileChooser.APPROVE_OPTION) {

            StatusBarPanel.getInstance().setMessage("Loading Files from Disk");

            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                public Void doInBackground() {

                    File[] list;

                    if (fileChooser.getSelectedFiles().length != 0)  {

                        list = fileChooser.getSelectedFiles();

                    } else {

                        list = new File[1];
                        list[0] = fileChooser.getSelectedFile();
                    }

                    if (list != null) {
                        eventLoader.loadFromLocalFiles(list);
                    }

                    return null;
                };
            };
            worker.execute();
        }
    }
    
    private void loadS3Files() {
        
        final List<String> files = S3FileChooserDialog.showDialog(jCloudTrailViewer.DESKTOP);

        if (!files.isEmpty()) {

            StatusBarPanel.getInstance().setMessage("Loading Files from S3");

            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                public Void doInBackground() {

                    eventLoader.loadFromS3Files(files);

                    return null;
                };
            };
            worker.execute();
        }
    }
    
    private void showEventsByServiceChart() {
        
        ChartData chartData = new ChartData();
        chartData.setChartStyle("bar");
        chartData.setChartSource("Events by Service");

        List<Map.Entry<String, Integer>> events = EventUtils.entriesSortedByValues(eventsDatabase.getEventsPerService());

        ChartWindow chart = new ChartWindow(chartData, events);
        chart.setVisible(true);

        jCloudTrailViewer.DESKTOP.add(chart);
    }
    
    private void showServiceTpsChart() {
        
        ChartData chartData = new ChartData();
        chartData.setChartStyle("TimeSeries");
        chartData.setChartSource("Service TPS");

        ChartWindow chart = new ChartWindow(chartData, eventsDatabase.getTransactionsPerService());
        chart.setVisible(true);

        jCloudTrailViewer.DESKTOP.add(chart);
    }
}
