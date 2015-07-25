/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.haskins.cloudtrailviewer.model.filter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */
public abstract class AbstractFilter implements Filter {
    
    private final List<FilterListener> listeners = new ArrayList<>();
    
    protected String needle;
    
    @Override
    public void setNeedle(String needle) {
        this.needle = needle;
        
        for (FilterListener l : listeners) {
            l.onFilterChanged();
        }
    }
    
    @Override
    public void addListener(FilterListener l) {
        listeners.add(l);
    }
}
