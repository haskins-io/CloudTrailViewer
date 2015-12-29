package com.haskins.cloudtrailviewer.feature;

import com.haskins.cloudtrailviewer.application.HelpToolBar;
import com.haskins.cloudtrailviewer.application.StatusBar;
import com.haskins.cloudtrailviewer.components.EventTablePanel;
import com.haskins.cloudtrailviewer.components.OverviewContainer;
import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.model.Help;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author mark.haskins
 */
public class GeoDataFeature extends JPanel implements Feature, EventDatabaseListener {
    
    public static final String NAME = "GeoData Feature";
    
    private final Help help = new Help("GeoData Feature", "geoData");
    
    private final OverviewContainer geoIpContainer;
    private final EventTablePanel eventTable = new EventTablePanel(EventTablePanel.CHART_EVENT);
    
    private JSplitPane jsp;
    
    private final StatusBar statusBar;
    private final HelpToolBar helpBar;
    
    public GeoDataFeature(StatusBar sb, HelpToolBar helpBar) {
        
        this.helpBar = helpBar;
        this.statusBar = sb;
        
        geoIpContainer = new OverviewContainer(this);
        
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
        return "Geo-Data-48.png";
    }

    @Override
    public String getTooltip() {
        return "GeoData Overview";
    }
    
    @Override
    public String getName() {
        return GeoDataFeature.NAME;
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
    public void showEventsTable(List<Event> events) {
        
        if (!eventTable.isVisible()) {
            
            jsp.setDividerLocation(0.5);
            jsp.setDividerSize(3);
            eventTable.setVisible(true);
        }
        
        eventTable.clearEvents();
        statusBar.setEvents(events);
        eventTable.setEvents(events);
    }
        
    @Override
    public void reset() {
        
        geoIpContainer.reset();
        geoIpContainer.revalidate();
        this.revalidate();
    }
    

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
        
        String cityName = event.getCity();
        if (cityName != null && cityName.trim().length() > 0) {
            geoIpContainer.addEvent(event, "City");
        }
    }
    
    @Override
    public void finishedLoading() {
        geoIpContainer.finishedLoading();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    //////////////////////////////////////////////////////////////////////////// 
    private void buildUI() {

        geoIpContainer.setBackground(Color.white);
        JScrollPane sPane = new JScrollPane(geoIpContainer);
        sPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        eventTable.setVisible(false);
        
        jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sPane, eventTable);
        jsp.setDividerSize(0);
        jsp.setResizeWeight(1);
        jsp.setDividerLocation(jsp.getSize().height - jsp.getInsets().bottom - jsp.getDividerSize());
        jsp.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
        
        this.setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
    }
}
