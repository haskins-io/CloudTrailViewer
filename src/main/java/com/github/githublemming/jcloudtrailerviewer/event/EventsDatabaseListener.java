package com.github.githublemming.jcloudtrailerviewer.event;

import com.github.githublemming.jcloudtrailerviewer.model.Event;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author mark.haskins
 */
public interface EventsDatabaseListener {
    
    public void onEventsUpdated(CopyOnWriteArrayList<Event> updatedEvents);
    
}