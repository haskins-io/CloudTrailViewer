package com.haskins.aws.jcloudtrailviewer.filters;

import com.haskins.aws.jcloudtrailviewer.models.Event;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mark
 */
public abstract class AbstractEventFilter implements EventFilter {
    
    @Override
    public abstract boolean passesFilter(Event awsEvent);
    
    private final List<EventFilterListener> listeners = new LinkedList<>();
    
    
    ////////////////////////////////////////////////////////////////////////////
    ///// public methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void addListener(EventFilterListener l) {
        
        this.listeners.add(l);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// protected methods
    ////////////////////////////////////////////////////////////////////////////
    protected void filterChanged() {
        
        for (EventFilterListener l : this.listeners) {
            
            l.onFilterChanged();
        }
    }
}
