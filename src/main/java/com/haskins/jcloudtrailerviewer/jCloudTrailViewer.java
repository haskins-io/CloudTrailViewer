package com.haskins.jcloudtrailerviewer;

import com.haskins.jcloudtrailerviewer.event.EventLoader;
import com.haskins.jcloudtrailerviewer.event.EventsDatabase;
import com.haskins.jcloudtrailerviewer.model.ChartData;
import com.haskins.jcloudtrailerviewer.panel.ChartDialog;
import com.haskins.jcloudtrailerviewer.util.InternalChartFactory;
import com.haskins.jcloudtrailerviewer.panel.S3FileChooserDialog;
import com.haskins.jcloudtrailerviewer.panel.StatusBarPanel;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

/**
 *
 * @author mark.haskins
 */
public class jCloudTrailViewer extends JFrame {

    private JDesktopPane desktop;
        
    private final EventLoader eventLoader;
    private final EventsDatabase eventsDatabase;
    private final EventUtils eventFilter = new EventUtils();
    
    private final JFileChooser fileChooser = new JFileChooser();

    public jCloudTrailViewer() {
        
        super("jCloudTrailViewer");

        eventsDatabase = new EventsDatabase();
        
        eventLoader = new EventLoader();
        eventLoader.addListener(eventsDatabase);
        
        PropertiesSingleton.getInstance();
        
        buildUI();
    }

    private void buildUI() {
        
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        desktop = new JDesktopPane(); //a specialized layered pane
        
        StatusBarPanel.getInstance();
        JPanel statusBarPanel = StatusBarPanel.getInstance().getStatusBar();
                
        JPanel layout = new JPanel();
        layout.setLayout(new BorderLayout());
        layout.add(createToolBar(), BorderLayout.NORTH);
        layout.add(desktop, BorderLayout.CENTER);
        layout.add(statusBarPanel, BorderLayout.SOUTH);
        
        setContentPane(layout);
        setJMenuBar(createMenuBar());
    }
    
    private JMenuBar createMenuBar() {
        
        JMenuBar menuBar = new JMenuBar();

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
        
        menuBar.add(menuFile);
        
        // -- Menu : Logs
        JMenu menuLogs = new JMenu("Logs");
        
        JMenuItem loadLocal = new JMenuItem(new AbstractAction("Load Local Files") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                int status = fileChooser.showOpenDialog(desktop);
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
                
                final List<String> files = S3FileChooserDialog.showDialog(desktop);
                               
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
        
        menuLogs.add(loadLocal);
        menuLogs.add(loadS3);
        
        if (!PropertiesSingleton.getInstance().configLoaded()) {
            loadS3.setEnabled(false);
        }
        
        menuBar.add(menuLogs);
     
        return menuBar;
    }

    private JToolBar createToolBar() {
        
        String imgLocation = "icons/";
        
        JToolBar toolbar = new JToolBar();
        
        JButton btnNewChart = new JButton();
        btnNewChart.setActionCommand("NewChart");
        btnNewChart.setToolTipText("Add new Chart");
        
        URL imageUrl = jCloudTrailViewer.class.getResource("../../../" + imgLocation + "chart_pie.png");
        btnNewChart.setIcon(new ImageIcon(imageUrl));
        
        btnNewChart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!eventsDatabase.getEvents().isEmpty()) {
                    createAndShowChart();
                }
            }
        });
        
        toolbar.add(btnNewChart);
        
        return toolbar;
    }
    
    private void createAndShowChart() {
        
        // need to open a modal dialog to get the appropriate information
        ChartData chartData = ChartDialog.showDialog(desktop);
        
        // get data for chart
        if (chartData != null) {
            
            List<Entry<String,Integer>> events = eventFilter.getRequiredEvents(eventsDatabase.getEvents(), chartData);

            JInternalFrame chart = InternalChartFactory.createChart(chartData, events);
            chart.setVisible(true);

            desktop.add(chart);

            try {
                chart.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
            }
        }
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
    
    private static void createAndShowGUI() {
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        jCloudTrailViewer frame = new jCloudTrailViewer();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
    
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
