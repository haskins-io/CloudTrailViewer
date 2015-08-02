/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.feature.security;

import com.haskins.cloudtrailviewer.core.EventDatabaseListener;
import com.haskins.cloudtrailviewer.feature.Feature;
import com.haskins.cloudtrailviewer.model.event.Event;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author mark.haskins
 */
public class SecurityFeature extends JPanel implements Feature, EventDatabaseListener {

    public static final String NAME = "Security Feature";
    
    public SecurityFeature() {
        
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
        return "Warning-48.png";
    }

    @Override
    public String getTooltip() {
        return "Potential Security Issues";
    }

    @Override
    public void will_hide() { }

    @Override
    public void will_appear() { }

    @Override
    public void showEventsTable(List<Event> events) { }
    
    @Override
    public String getName() {
        return SecurityFeature.NAME;
    }
    

    ////////////////////////////////////////////////////////////////////////////
    ///// EventDatabaseListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void eventAdded(Event event) {
                 
    }
}
