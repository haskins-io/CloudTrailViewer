/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.model.filter;

import com.haskins.cloudtrailviewer.model.event.Event;

/**
 *
 * @author mark
 */
public interface Filter {
    
    public boolean passesFilter(Event event);
    
    public void setNeedle(String needle);
    
    public void addListener(FilterListener l);
}
