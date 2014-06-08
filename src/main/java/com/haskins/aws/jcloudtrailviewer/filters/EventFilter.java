package com.haskins.aws.jcloudtrailviewer.filters;

import com.haskins.aws.jcloudtrailviewer.models.Event;

/**
 *
 * @author mark
 */
public interface EventFilter {
    
    public boolean passesFilter(Event event);
    
    public void addListener(EventFilterListener l);
}
