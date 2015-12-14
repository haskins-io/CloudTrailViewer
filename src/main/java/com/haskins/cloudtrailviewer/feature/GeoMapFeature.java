package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class GeoMapFeature  extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "GeOMap Feature";
    
    private final Help help = new Help("GeoMap", "geomap");
    
    private final HelpToolBar helpBar;
    private final StatusBar statusBar;
    
    private final Map<String, Integer> longLats = new HashMap<>();
        
    public GeoMapFeature(StatusBar sb, HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        this.statusBar = sb;
        
        buildUI();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Feature implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventLoadingComplete() { }
    
    @Override
    public boolean showOnToolBar() {
        return true;
    }

    @Override
    public String getIcon() {
        return "Service-Overview-48.png";
    }

    @Override
    public String getTooltip() {
        return "GeoMap Overview";
    }
    
    @Override
    public String getName() {
        return OverviewFeature.NAME;
    }
    
    @Override
    public void will_hide() {
        helpBar.setHelp(null);
    }
    
    @Override
    public void will_appear() {
        helpBar.setHelp(help);
    }
    
    @Override
    public void showEventsTable(List<Event> events) { }
        
    @Override
    public void reset() { }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) { 
    
        if (event.getLatLng() != null) {
            
            int count = 1;
            
            if (longLats.containsKey(event.getLatLng())) {
                count = longLats.get(event.getLatLng()) + 1;
            } 
            
            longLats.put(event.getLatLng(), count);
        }
    }
    
    @Override
    public void finishedLoading() {
        // group by long/lat and update google map
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {
    }
    
}
