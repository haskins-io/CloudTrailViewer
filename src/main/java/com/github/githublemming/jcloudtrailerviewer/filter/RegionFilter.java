package com.github.githublemming.jcloudtrailerviewer.filter;

import com.github.githublemming.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark
 */
public class RegionFilter extends AbstractEventFilter {
    
    private String region;
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract implementations
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (region == null || region.trim().length() == 0) {
            
            passesFilter = true;
            
        } else {
        
            String needle = event.getAwsRegion();

            if (needle != null && needle.equalsIgnoreCase(this.region)) {
                passesFilter = true;
            }
        }
        
        return passesFilter;
    }
}
