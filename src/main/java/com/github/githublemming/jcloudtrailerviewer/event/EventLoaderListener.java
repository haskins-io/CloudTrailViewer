package com.github.githublemming.jcloudtrailerviewer.event;

import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public interface EventLoaderListener {
    
    public void newEvents(List<Event> events);
}
