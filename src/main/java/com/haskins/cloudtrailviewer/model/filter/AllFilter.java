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
public class AllFilter extends AbstractFilter implements Filter {

    
    ///////////////////////////////////////////////////////////////////////////
    // Filter overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (needle == null || needle.trim().length() == 0) {
            passesFilter = true;
            
        } else {
            
            String lowerJSON = event.toString().toLowerCase();
            String lowerFilter = this.needle.toLowerCase();

            if (lowerJSON.contains(lowerFilter)) {
                passesFilter = true;
            }
        }
        
        return passesFilter;
    }
        
    ///////////////////////////////////////////////////////////////////////////
    // Other overrides
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        return "All";
    }
    
}
