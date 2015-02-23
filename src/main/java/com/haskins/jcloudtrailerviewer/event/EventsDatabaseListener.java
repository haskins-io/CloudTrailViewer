package com.haskins.jcloudtrailerviewer.event;

import com.haskins.jcloudtrailerviewer.model.Event;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author mark.haskins
 */
public interface EventsDatabaseListener {
    
    public void onEventsUpdated(CopyOnWriteArrayList<Event> updatedEvents);
    
}