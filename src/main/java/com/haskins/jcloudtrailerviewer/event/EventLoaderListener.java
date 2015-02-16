package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public interface EventLoaderListener {
    
    public void newEvents(List<Event> events);
}
