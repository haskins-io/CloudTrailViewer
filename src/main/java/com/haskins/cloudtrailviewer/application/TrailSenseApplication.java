/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.application;

import com.haskins.cloudtrailviewer.features.Feature;
import com.haskins.cloudtrailviewer.features.NoData;
import com.haskins.cloudtrailviewer.features.SimpleTable;
import com.haskins.cloudtrailviewer.core.EventLoader;
import com.haskins.cloudtrailviewer.core.EventLoaderListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.features.serviceoverview.ServiceOverview;
import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import com.haskins.cloudtrailviewer.model.load.LoadFileRequest;
import com.haskins.cloudtrailviewer.utils.GeneralUtils;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class TrailSenseApplication extends JFrame implements EventLoaderListener {
        
    private final FilteredEventDatabase database ;
    private final EventLoader eventLoader;
    
    private final JPanel features = new JPanel(new CardLayout());
    private final Map<String,Feature> featureMap = new HashMap<>();
    private String currentFeature;
    
    private final StatusBar statusBar = new StatusBar();
    private final SidePanelToolBar sidePanelToolBar;
    private final FeatureToolBar featureToolBar;
    
    private final Filter filter = new AllFilter();
    
    public TrailSenseApplication() {
        
        super("CloudTrail Analyser");
        
        database = new FilteredEventDatabase(filter);
        
        eventLoader = new EventLoader(database);
        eventLoader.addEventLoaderListener(this);
        
        sidePanelToolBar = new SidePanelToolBar(this);
        featureToolBar = new FeatureToolBar(this);
        
        defineFeatures();
        buildUI();
    }
        
    public void newLocalFiles(File[] files, Filter filter) {
        
        if (files != null && files.length > 0) {
            
            changeFeature(ServiceOverview.NAME);
            
            List<String> filePaths = new ArrayList<>();
            for (File file : files) {
                filePaths.add(file.getAbsolutePath());
            }
            
            LoadFileRequest loadRequest = new LoadFileRequest(filePaths, filter);
            eventLoader.loadEventsFromLocalFiles(loadRequest);            
        }
    }
    
    public void newS3Files(List<String> files, Filter filter) {
    
        if (files != null && files.size() > 0) {
            
            changeFeature(ServiceOverview.NAME);
            
            LoadFileRequest loadRequest = new LoadFileRequest(files, null);
            eventLoader.loadEventsFromS3(loadRequest);
        }
    }
    
    public void toggleSidebar() {
        
        Feature feature = featureMap.get(currentFeature);
        feature.toggleSideBar();
    } 
    
    public void changeFeature(String name) {
            
        currentFeature = name;

        CardLayout cl = (CardLayout)(features.getLayout());
        cl.show(features, name);

        Feature feature = featureMap.get(name);
        if (feature.providesSideBar()) {
            sidePanelToolBar.showSideBarButton(true);
        } else {
            sidePanelToolBar.showSideBarButton(false);
        }  
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void processingFile(int fileCount) {
        this.statusBar.setStatusMessage("Processing file " + fileCount);
    }

    @Override
    public void finishedLoading() {
        
        this.statusBar.setStatusMessage("");
        
        for (Component component : features.getComponents()) {
            Feature feature = (Feature)component;
            feature.eventLoadingComplete();
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildUI() {
        
        this.setTitle("CloudTrail Analyser");
        this.setLayout(new BorderLayout());
        
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width / 2,
                screenSize.height / 2);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        
        if (!GeneralUtils.isMac()) {
        Menu menu = new Menu();    
        topPanel.add(menu, BorderLayout.NORTH);
        }
        
        JPanel toolbars = new JPanel(new GridLayout(1, 3));
        toolbars.setBackground(Color.WHITE);
        toolbars.add(new LoadToolBar(this));
        toolbars.add(featureToolBar);
        toolbars.add(sidePanelToolBar);
        
        topPanel.add(toolbars, BorderLayout.SOUTH);
           
        this.add(topPanel, BorderLayout.NORTH);
        this.add(features, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.PAGE_END);
    }
    
    private void defineFeatures() {

        NoData noData = new NoData();
        featureMap.put(noData.getName(), noData);
        features.add((JPanel)noData, noData.getName());
        changeFeature(NoData.NAME);
        
        SimpleTable simpleTable = new SimpleTable(database);
        featureMap.put(simpleTable.getName(), simpleTable);
        features.add((JPanel)simpleTable, simpleTable.getName());
        
        ServiceOverview serviceOverview = new ServiceOverview(database);
        featureMap.put(serviceOverview.getName(), serviceOverview);
        features.add((JPanel)serviceOverview, serviceOverview.getName());
        
        Set<String> keys = featureMap.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            
            String key = it.next();
            Feature feature = featureMap.get(key);
            if (feature.showOnToolBar()) {
                featureToolBar.addFeature(feature);
            }
        }
    }
}