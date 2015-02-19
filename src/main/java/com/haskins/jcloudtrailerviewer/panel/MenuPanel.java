package com.haskins.jcloudtrailerviewer.panel;

import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.event.EventLoader;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JDesktopPane;
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
    
    private final EventLoader eventLoader;
    
    public MenuPanel(EventLoader eventLoader, JDesktopPane desktop) {
        
        this.eventLoader = eventLoader;
        
        buildMenu(desktop);
    }
    
    private void buildMenu(final JDesktopPane desktop) {
        
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
        JMenu menuLogs = new JMenu("Events");
        
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
        
        this.add(menuLogs);
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
