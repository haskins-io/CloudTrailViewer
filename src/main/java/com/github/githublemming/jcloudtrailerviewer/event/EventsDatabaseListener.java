package com.github.githublemming.jcloudtrailerviewer.event;

import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.util.List;

/**
 *
 * @author mark.haskins
 */
public interface EventsDatabaseListener {
    
    public void onEventsUpdated(List<Event> updatedEvents);
    
}