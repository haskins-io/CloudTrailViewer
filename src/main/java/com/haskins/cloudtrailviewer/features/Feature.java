/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.features;

/**
 *
 * @author mark
 */
public interface Feature {
    
    public void eventLoadingComplete();
    
    public boolean providesSideBar();
    public void toggleSideBar();
    
    public boolean showOnToolBar();
    public String getName();
    public String getIcon();
    public String getTooltip();
}
