package com.github.githublemming.jcloudtrailerviewer.filter;

import com.github.githublemming.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark
 */
public class EventNameFilter extends AbstractEventFilter {
    
    private String eventName;
    
    public void setEventName(String eventName) {
        
        this.eventName = eventName;
        filterChanged();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// Abstract implementations
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean passesFilter(Event event) {
        
        boolean passesFilter = false;
        
        if (eventName == null || eventName.trim().length() == 0) {
            
            passesFilter = true;
            
        } else {
            
            String needle = event.getEventName();

            if (needle != null && needle.equalsIgnoreCase(this.eventName)) {
                passesFilter = true;
            }
        }
        
        return passesFilter;
    }
}
