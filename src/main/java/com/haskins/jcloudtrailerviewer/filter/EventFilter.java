package com.haskins.jcloudtrailerviewer.filter;

import com.haskins.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark
 */
public interface EventFilter {
    
    public boolean passesFilter(Event event);
    public void addListener(EventFilterListener l);
}
