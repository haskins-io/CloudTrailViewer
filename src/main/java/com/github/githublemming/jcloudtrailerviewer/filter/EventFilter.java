package com.github.githublemming.jcloudtrailerviewer.filter;

import com.github.githublemming.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark
 */
public interface EventFilter {
    
    public boolean passesFilter(Event event);
    public void addListener(EventFilterListener l);
}
