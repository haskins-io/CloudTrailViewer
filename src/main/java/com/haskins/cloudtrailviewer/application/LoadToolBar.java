/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.application;

import com.haskins.cloudtrailviewer.CloudTrailViewer;
import com.haskins.cloudtrailviewer.dialog.S3FileChooser;
import com.haskins.cloudtrailviewer.core.PropertiesController;
import com.haskins.cloudtrailviewer.dialog.AwsAccount;
import com.haskins.cloudtrailviewer.dialog.SearchOptions;
import com.haskins.cloudtrailviewer.model.filter.Filter;
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
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 *
 * @author mark
 */
public class LoadToolBar extends JToolBar {

    private final JFileChooser fileChooser = new JFileChooser();
    
    private final TrailSenseApplication application;
    
    public LoadToolBar(TrailSenseApplication application) {
        
        this.application = application;
        fileChooser.setMultiSelectionEnabled(true);
        
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
                
                if (SearchOptions.showDialog(CloudTrailViewer.frame) == SearchOptions.SCAN_OK && 
                    SearchOptions.getSearchFilter() != null) {
                    
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

                loadS3files(null);
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

                loadS3files(null);
            }
        }); 
        
        buttonsPanel.add(btnDownload);
    }
    
    private void loadLocalFiles(Filter filter) {
        
        int status = fileChooser.showOpenDialog(CloudTrailViewer.frame);
        if (status == JFileChooser.APPROVE_OPTION) {

            File[] list;

            if (fileChooser.getSelectedFiles().length != 0)  {

                list = fileChooser.getSelectedFiles();

            } else {

                list = new File[1];
                list[0] = fileChooser.getSelectedFile();
            }

            if (list != null) {
                 application.newLocalFiles(list, filter);
            }
        }
    }
    
    private void loadS3files(Filter filter) {
        
        if (PropertiesController.getInstance().checkS3Credentials()) {

            final List<String> files = S3FileChooser.showDialog(CloudTrailViewer.frame);
            if (!files.isEmpty()) {
                application.newS3Files(files, filter);
            }
            
        } else {
            
            AwsAccount.showDialog(CloudTrailViewer.frame);
            
            if (PropertiesController.getInstance().checkS3Credentials()) {
                
                final List<String> files = S3FileChooser.showDialog(CloudTrailViewer.frame);
                if (!files.isEmpty()) {
                    application.newS3Files(files, filter);
                }
            }
        } 
    }
}
