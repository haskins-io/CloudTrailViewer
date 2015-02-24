package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.event.ActionEvent;
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
public class MenuPanel extends JMenuBar {
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    private final EventsDatabase eventsDatabase;
    private final EventLoader eventLoader;
    
    public MenuPanel(EventLoader eventLoader, EventsDatabase database) {
        
        this.eventLoader = eventLoader;
        eventsDatabase = database;
        
        buildMenu();
    }
    
    private void buildMenu() {
        
        fileChooser.setMultiSelectionEnabled(true);
        
        //Set up the lone menu.
        // -- Menu : File
        JMenu menuFile = new JMenu("File");
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                System.exit(0);
            }
        });
        
        menuFile.add(exit);
        
        this.add(menuFile);
        
        // -- Menu : Logs
        JMenu menuEvents = new JMenu("Events");
        
        JMenuItem loadLocal = new JMenuItem(new AbstractAction("Load Local Files") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                int status = fileChooser.showOpenDialog(jCloudTrailViewer.DESKTOP);
                if (status == JFileChooser.APPROVE_OPTION) {
                    
                    StatusBarPanel.getInstance().setMessage("Loading Files from Disk");
                    
                    SwingWorker worker = new SwingWorker<Void, Void>() {
                      
                        @Override
                        public Void doInBackground() {
                            
                            openLocalFiles();
                            
                            return null;
                        };
                    };
                    worker.execute();
                }
            }
        });
        
        JMenuItem loadS3 = new JMenuItem(new AbstractAction("Load S3 Files") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
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
        });
        
        JMenuItem eventsByService = new JMenuItem(new AbstractAction("Events by Service") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                ChartData chartData = new ChartData();
                chartData.setChartStyle("bar");
                chartData.setChartSource("Events by Service");

                List<Map.Entry<String, Integer>> events = EventUtils.entriesSortedByValues(eventsDatabase.getEventsPerService());

                ChartWindow chart = new ChartWindow(chartData, events);
                chart.setVisible(true);

                jCloudTrailViewer.DESKTOP.add(chart);

                try {
                    chart.setSelected(true);
                }
                catch (java.beans.PropertyVetoException e) { }
            }
        });
        
        JMenuItem serviceTps = new JMenuItem(new AbstractAction("Service TPS") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                ChartData chartData = new ChartData();
                chartData.setChartStyle("TimeSeries");
                chartData.setChartSource("Service TPS");

                ChartWindow chart = new ChartWindow(chartData, eventsDatabase.getTransactionsPerService());
                chart.setVisible(true);

                jCloudTrailViewer.DESKTOP.add(chart);

                try {
                    chart.setSelected(true);
                }
                catch (java.beans.PropertyVetoException e) { }
            }
        });
        
        menuEvents.add(loadLocal);
        menuEvents.add(loadS3);
        menuEvents.addSeparator();
        menuEvents.add(eventsByService);
        menuEvents.add(serviceTps);
        
        if (!PropertiesSingleton.getInstance().configLoaded()) {
            loadS3.setEnabled(false);
        }
        
        this.add(menuEvents);
    }
    
    private void openLocalFiles() {
        
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
    }
}
