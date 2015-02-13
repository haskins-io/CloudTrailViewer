package com.github.githublemming.jcloudtrailerviewer.filter;

import com.github.githublemming.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark
 */
public class FreeformFilter extends AbstractEventFilter {
    
    private String value;
    
    public void setValue(String value) {
        
        this.value = value;
        filterChanged();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract implementations
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (value == null || value.trim().length() == 0) {
            
            passesFilter = true;
            
        } else {
            
            String lowerJSON = event.getRawJSON().toLowerCase();
            String lowerFilter = this.value.toLowerCase();

            if (lowerJSON.contains(lowerFilter)) {
                passesFilter = true;
            }
        }
        
        return passesFilter;
    }
}
