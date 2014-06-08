package com.haskins.aws.jcloudtrailviewer.filters;

import com.haskins.aws.jcloudtrailviewer.models.Event;

/**
 *
 * @author mark
 */
public class UsernameFilter extends AbstractEventFilter {
    
    private String username;
    
    public void setUsername(String username) {
        
        this.username = username;
        filterChanged();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract implementations
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (username == null || username.trim().length() == 0) {
            
            passesFilter = true;
            
        } else {
        
            String needle = event.getUserIdentity().getUserName();

            if (needle != null && needle.equalsIgnoreCase(this.username)) {

                passesFilter = true;
            }
        }
            
        return passesFilter;
    }
}
