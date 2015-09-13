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
import com.haskins.cloudtrailviewer.feature.NoDataFeature;
import com.haskins.cloudtrailviewer.feature.SimpleTableFeature;
import com.haskins.cloudtrailviewer.core.EventLoader;
import com.haskins.cloudtrailviewer.core.EventLoaderListener;
import com.haskins.cloudtrailviewer.core.FilteredEventDatabase;
import com.haskins.cloudtrailviewer.feature.ErrorFeature;
import com.haskins.cloudtrailviewer.feature.InvokersFeature;
import com.haskins.cloudtrailviewer.feature.OverviewFeature;
import com.haskins.cloudtrailviewer.feature.ResourceFeature;
import com.haskins.cloudtrailviewer.feature.SecurityFeature;
import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import com.haskins.cloudtrailviewer.model.load.LoadFileRequest;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
    
    private final StatusBar statusBar = new StatusBar();
    private final HelpToolBar helpToolBar = new HelpToolBar();;
    private final FeatureToolBar featureToolBar;
    
    private final Filter filter = new AllFilter();
    
    /**
     * Default Constructor
     */
    public CloudTrailViewerApplication() {
        
        super("CloudTrail Viewer");
                
        database = new FilteredEventDatabase(filter, statusBar);
        
        eventLoader = new EventLoader(database);
        eventLoader.addEventLoaderListener(this);
        
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
                if (file.isFile() && file.getName().endsWith(".gz")) {
                    filePaths.add(file.getAbsolutePath());
                }
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
            
            LoadFileRequest loadRequest = new LoadFileRequest(files, filter);
            eventLoader.loadEventsFromS3(loadRequest);
        }
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
                feature.will_hide();
            }
            
            CardLayout cl = (CardLayout)(features.getLayout());
            cl.show(features, name); 
            Feature f = featureMap.get(name);
            f.will_appear();
        }
    }
    
    /**
     * When called clears the database of all events and then informs all features
     * to reset themselves.
     */
    public void clearEvents() {
        
        database.clear();
        statusBar.eventsCleared();
        
        for (Component component : features.getComponents()) {
            Feature feature = (Feature)component;
            feature.reset();
        } 
        
        CardLayout cl = (CardLayout)(features.getLayout());
        cl.show(features, NoDataFeature.NAME); 
        Feature f = featureMap.get(NoDataFeature.NAME);
        f.will_appear();
        
        this.revalidate();
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
        
        this.setTitle("CloudTrail Viewer");
        this.setLayout(new BorderLayout());
        
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width / 2,
                screenSize.height / 2);


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
                
        JPanel toolbars = new JPanel(new BorderLayout());
        toolbars.setBackground(Color.WHITE);
        toolbars.add(new LoadToolBar(this), BorderLayout.WEST);
        toolbars.add(featureToolBar, BorderLayout.CENTER);
        toolbars.add(helpToolBar, BorderLayout.EAST);
        
        topPanel.add(toolbars, BorderLayout.SOUTH);
           
        this.add(topPanel, BorderLayout.NORTH);
        this.add(features, BorderLayout.CENTER);
        this.add(statusBar, BorderLayout.PAGE_END);
    }
    
    private void defineFeatures() {

        NoDataFeature noData = new NoDataFeature(helpToolBar);
        featureMap.put(noData.getName(), noData);
        features.add((JPanel)noData, noData.getName());
        
        OverviewFeature serviceOverview = new OverviewFeature(statusBar, helpToolBar);
        database.addListener(serviceOverview);
        featureMap.put(serviceOverview.getName(), serviceOverview);
        features.add((JPanel)serviceOverview, serviceOverview.getName());
        
        SimpleTableFeature simpleTable = new SimpleTableFeature(database, helpToolBar);
        featureMap.put(simpleTable.getName(), simpleTable);
        features.add((JPanel)simpleTable, simpleTable.getName());
        
        InvokersFeature invokerOverview = new InvokersFeature(statusBar, helpToolBar);
        database.addListener(invokerOverview);
        featureMap.put(invokerOverview.getName(), invokerOverview);
        features.add((JPanel)invokerOverview, invokerOverview.getName());
        
        ErrorFeature errorFeature = new ErrorFeature(statusBar, helpToolBar);
        database.addListener(errorFeature);
        featureMap.put(errorFeature.getName(), errorFeature);
        features.add((JPanel)errorFeature, errorFeature.getName());
        
        SecurityFeature securityFeature = new SecurityFeature(statusBar, helpToolBar);
        database.addListener(securityFeature);
        featureMap.put(securityFeature.getName(), securityFeature);
        features.add((JPanel)securityFeature, securityFeature.getName());
        
        ResourceFeature resourceFeature = new ResourceFeature(statusBar, helpToolBar);
        database.addListener(resourceFeature);
        featureMap.put(resourceFeature.getName(), resourceFeature);
        features.add((JPanel)resourceFeature, resourceFeature.getName());
       
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