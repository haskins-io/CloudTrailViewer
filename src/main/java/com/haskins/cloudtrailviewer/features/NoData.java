/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.features;

import com.haskins.cloudtrailviewer.model.event.Event;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mark
 */
public class NoData extends JPanel implements Feature {
        
    public static final String NAME = "No Data";
    
    private static final String noEvents = "No Events Loaded.";
    private static final String events = "";
    
    JLabel label = new JLabel(noEvents);
    
    public NoData() {
        buildPanel();
    }
    
    public void showEventsAvailable() {
        label.setText(events);
    }
        ////////////////////////////////////////////////////////////////////////////
    ///// Card implementation
    ////////////////////////////////////////////////////////////////////////////    
    @Override
    public void eventLoadingComplete() { }
    
    @Override
    public void toggleSideBar() {}
    
    @Override
    public boolean providesSideBar() {
        return false;
    }
    
    @Override
    public boolean showOnToolBar() {
        return false;
    }
    
    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getTooltip() {
        return null;
    }
    
    @Override
    public String getName() {
        return NoData.NAME;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildPanel() {
                
        this.setLayout(new GridBagLayout());
        this.add(label);
    }

}
