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

import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.feature.NoData;
import com.haskins.cloudtrailviewer.feature.SimpleTable;
import com.haskins.cloudtrailviewer.core.EventLoader;
import com.haskins.cloudtrailviewer.core.EventLoaderListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.feature.overview.OverviewFeature;
import com.haskins.cloudtrailviewer.feature.user.UserFeature;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Main Application class, in some ways it acts as a Controller to controller the views (or features).
 * 
 * @author mark
 */
public class CloudTrailViewerApplication extends JFrame implements EventLoaderListener {
        
    private final FilteredEventDatabase database ;
    private final EventLoader eventLoader;
    
    private final JPanel features = new JPanel(new CardLayout());
    private final Map<String,Feature> featureMap = new LinkedHashMap<>();
    private String currentFeature;
    
    private final StatusBar statusBar = new StatusBar();
    private final SidePanelToolBar sidePanelToolBar;
    private final FeatureToolBar featureToolBar;
    
    private final Filter filter = new AllFilter();
    
    /**
     * Default Constructor
     */
    public CloudTrailViewerApplication() {
        
        super("CloudTrail Analyser");
        
        database = new FilteredEventDatabase(filter, statusBar);
        
        eventLoader = new EventLoader(database);
        eventLoader.addEventLoaderListener(this);
        
        sidePanelToolBar = new SidePanelToolBar(this);
        featureToolBar = new FeatureToolBar(this);
        
        defineFeatures();
        buildUI();
    }
        
    /**
     * Loads files from the local file system.
     * 
     * @param files An Array of FILEs to be loaded
     * @param filter An object to filter only specific events. Pass NULL in if no
     * filtering is required.
     */
    public void loadLocalFiles(File[] files, Filter filter) {
        
        if (files != null && files.length > 0) {
            
            changeFeature(OverviewFeature.NAME, true);
            
            List<String> filePaths = new ArrayList<>();
            for (File file : files) {
                filePaths.add(file.getAbsolutePath());
            }
            
            LoadFileRequest loadRequest = new LoadFileRequest(filePaths, filter);
            eventLoader.loadEventsFromLocalFiles(loadRequest);            
        }
    }
    
    /**
     * Loads files from the an AWS S3 Bucket.
     * 
     * @param files A List of S3 Bucket Keys as a String
     * @param filter An object to filter only specific events. Pass NULL in if no
     * filtering is required.
     */
    public void newS3Files(List<String> files, Filter filter) {
    
        if (files != null && files.size() > 0) {
            
            changeFeature(OverviewFeature.NAME, true);
            
            LoadFileRequest loadRequest = new LoadFileRequest(files, null);
            eventLoader.loadEventsFromS3(loadRequest);
        }
    }
    
    /**
     * Causing the Sidebar to be toggle into / out of view
     */
    public void toggleSidebar() {
        
        Feature feature = featureMap.get(currentFeature);
        feature.toggleSideBar();
    } 
    
    /**
     * Changes the feature that is visible.
     * @param name The name of the Feature to show.
     * @param loading 
     */
    public void changeFeature(String name, boolean loading) {
            
        if (database.size() > 0 || loading) {
        
            for (Component component : features.getComponents()) {
                Feature feature = (Feature)component;
                feature.is_hidden();
            }
            
            currentFeature = name;

            CardLayout cl = (CardLayout)(features.getLayout());
            cl.show(features, name);

            Feature feature = featureMap.get(name);
            feature.is_visible();
            if (feature.providesSideBar()) {
                sidePanelToolBar.showSideBarButton(true);
            } else {
                sidePanelToolBar.showSideBarButton(false);
            }  
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void processingFile(int fileCount, int total) {
        this.statusBar.setStatusMessage("Processing file " + fileCount + " of " + total);
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
        changeFeature(NoData.NAME, true);
        
        OverviewFeature serviceOverview = new OverviewFeature(database);
        featureMap.put(serviceOverview.getName(), serviceOverview);
        features.add((JPanel)serviceOverview, serviceOverview.getName());
        
        SimpleTable simpleTable = new SimpleTable(database);
        featureMap.put(simpleTable.getName(), simpleTable);
        features.add((JPanel)simpleTable, simpleTable.getName());
        
        UserFeature userOverview = new UserFeature(database);
        featureMap.put(userOverview.getName(), userOverview);
        features.add((JPanel)userOverview, userOverview.getName());
       
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