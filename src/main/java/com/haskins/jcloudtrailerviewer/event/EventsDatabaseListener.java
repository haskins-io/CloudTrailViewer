package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;

/**
 *
 * @author mark.haskins
 */
public interface EventsDatabaseListener {
    
    public void onEvent(Event event);
    
}