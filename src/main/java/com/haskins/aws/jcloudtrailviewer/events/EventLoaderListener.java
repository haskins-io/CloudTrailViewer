package com.haskins.aws.jcloudtrailviewer.events;

import com.haskins.aws.jcloudtrailviewer.models.Event;
import java.util.List;

/**
 *
 * @author mark
 */
public interface EventLoaderListener {
        
    public void newEvents(List<Event> events);
}
