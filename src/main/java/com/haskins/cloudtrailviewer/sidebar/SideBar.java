/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.sidebar;

import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public interface SideBar {
    
    public void eventLoadingComplete();
    
    public boolean showOnToolBar();
    public String getName();
    public String getIcon();
    public String getTooltip();  
    
    public void setCurrentEvent(Event event);
}
