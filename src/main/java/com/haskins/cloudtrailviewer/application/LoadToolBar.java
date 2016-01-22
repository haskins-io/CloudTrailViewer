/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.application;

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.dao.AccountDao;
import com.haskins.cloudtrailviewer.dialog.SearchOptions;
import com.haskins.cloudtrailviewer.dialog.s3filechooser.EnhancedS3FileChooser;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.model.filter.CompositeFilter;
import com.haskins.cloudtrailviewer.utils.ToolBarUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Provides the Load / Scan Event Toolbar
 * 
 * @author mark
 */
public class LoadToolBar extends JToolBar {

    private static final long serialVersionUID = -6865662163634950921L;

    private final JFileChooser fileChooser = new JFileChooser();
    
    private final CloudTrailViewerApplication application;
    
    /**
     * Default Constructor
     * @param application reference to the application.
     */
    public LoadToolBar(CloudTrailViewerApplication application) {
        
        this.application = application;
        
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Log files", "gz", "json"));
        
        buildToolBar();
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildToolBar(){
        
        this.setFloatable(false);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        
        addLocalOpenButton(buttonsPanel);
        addCloudDownloadButton(buttonsPanel);
        addLocalScanButton(buttonsPanel);
        addCloudScanButton(buttonsPanel);
        
        this.add(buttonsPanel, BorderLayout.WEST);
    }
    
    private void addLocalOpenButton(JPanel buttonsPanel) {
        
        JButton btnLocal = new JButton();
        ToolBarUtils.addImageToButton(btnLocal, "Local-Open-48.png", "Load", "Load Local Files");
        btnLocal.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                
                loadLocalFiles(null);
            }
        }); 
        
        buttonsPanel.add(btnLocal);
    }
    
    private void addLocalScanButton(JPanel buttonsPanel) {
        
        JButton btnLocal = new JButton();
        ToolBarUtils.addImageToButton(btnLocal, "Local-Scan-48.png", "Local Scan", "Scan Local Files");
        btnLocal.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                
                if (SearchOptions.showDialog(CloudTrailViewer.frame) == SearchOptions.SCAN_OK) {
                    
                    loadLocalFiles(SearchOptions.getSearchFilter());
                }
            }
        }); 
        
        buttonsPanel.add(btnLocal);
    }
    
    private void addCloudDownloadButton(JPanel buttonsPanel) {
        
        JButton btnDownload = new JButton();
        ToolBarUtils.addImageToButton(btnDownload, "Cloud-Download-48.png", "Download", "Download from AWS"); 
        btnDownload.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                loadS3files(false);
            }
        }); 
        
        buttonsPanel.add(btnDownload);
    }
    
    private void addCloudScanButton(JPanel buttonsPanel) {
        
        JButton btnDownload = new JButton();
        ToolBarUtils.addImageToButton(btnDownload, "Cloud-Scan-48.png", "Download", "Scan S3 Files"); 
        btnDownload.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                loadS3files(true);
            }
        }); 
        
        buttonsPanel.add(btnDownload);
    }
    
    private void loadLocalFiles(CompositeFilter filter) {
        
        int status = fileChooser.showOpenDialog(CloudTrailViewer.frame);
        if (status == JFileChooser.APPROVE_OPTION) {

            File[] list;

            if (fileChooser.getSelectedFile() != null && fileChooser.getSelectedFile().isDirectory()) {
                
                File dir = fileChooser.getSelectedFile();
                list = dir.listFiles();
                
            } else { 
                
                if (fileChooser.getSelectedFiles().length != 0)  {

                list = fileChooser.getSelectedFiles();

                } else {

                    list = new File[1];
                    list[0] = fileChooser.getSelectedFile();
                }
            }

            if (list != null) {
                 application.loadLocalFiles(list, filter);
            }
        }
    }
    
    private void loadS3files(boolean is_scan) {
        
        List<AwsAccount> accounts = AccountDao.getAllAccounts(false);
        if (!accounts.isEmpty()) {

            int mode = EnhancedS3FileChooser.MODE_OPEN;
            if (is_scan) {
                mode = EnhancedS3FileChooser.MODE_SCAN;
            }

            application.newS3Files(EnhancedS3FileChooser.showDialog(CloudTrailViewer.frame, mode));

        } else {
            JOptionPane.showMessageDialog(
                CloudTrailViewer.frame, 
                "To download file from a S3 bucket you\nwill need to define an AWS Account in Preferences."
            );
        } 
    }
}
