package com.github.githublemming.jcloudtrailerviewer.panel;

import com.github.githublemming.jcloudtrailerviewer.event.EventLoader;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class MenuPanel extends JPanel {
    
    private final JMenuBar menu = new JMenuBar();
    private final JFileChooser fileChooser = new JFileChooser();
    
    private final EventLoader eventLoader;
    
    public MenuPanel(EventLoader eventLoader) {
        
        this.eventLoader = eventLoader;
        
        buildUI();
    }
    
    private void buildUI() {
        
        fileChooser.setMultiSelectionEnabled(true);
        
        // -- Menu : File
        JMenu menuFile = new JMenu("File");
        
        menu.add(menuFile);
        
        // -- Menu : Logs
        JMenu menuLogs = new JMenu("Logs");
        
        JMenuItem loadLocal = new JMenuItem(new AbstractAction("Load Local Files") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                int status = fileChooser.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION)
                {
                    openLocalFiles();
                }
            }
        });
        
        JMenuItem loadS3 = new JMenuItem(new AbstractAction("Load S3 Files") {
            
            @Override
            public void actionPerformed(ActionEvent t) {
                
                S3FileChooserDialog s3Dialog = new S3FileChooserDialog();
                List<String> files = s3Dialog.showDialog();
                               
                if (!files.isEmpty()) {
                    eventLoader.loadFromS3Files(files);
                }
            }
        });
        
        menuLogs.add(loadLocal);
        menuLogs.add(loadS3);
        
        menu.add(menuLogs);
     
        // -- Menu : Help
        JMenu menuHelp = new JMenu("Help");
        
        menu.add(menuHelp);
        
        this.setLayout(new BorderLayout());
        this.add(menu, BorderLayout.NORTH);
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
