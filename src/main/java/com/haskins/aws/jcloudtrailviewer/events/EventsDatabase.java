package com.haskins.aws.jcloudtrailviewer.events;

import com.haskins.aws.jcloudtrailviewer.filters.Filters;
import com.haskins.aws.jcloudtrailviewer.filters.FiltersListener;
import com.haskins.aws.jcloudtrailviewer.models.Event;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mark
 */
public class EventsDatabase implements EventLoaderListener, FiltersListener {
    
    // --- table models
    private final ObservableList<Event> tableList = FXCollections.observableArrayList();
    private final ObservableList accountList = FXCollections.observableArrayList();
    private final ObservableList regionList = FXCollections.observableArrayList();
    private final ObservableList usernameList = FXCollections.observableArrayList();
    private final ObservableList eventList = FXCollections.observableArrayList();
    
    
    private final Map<String, Event> masterEventsMap    = new HashMap<>();
    private final Map<String, Event> accountMap   = new HashMap<>();
    private final Map<String, Event> regionMap    = new HashMap<>();
    private final Map<String, Event> usernameMap  = new HashMap<>();
    private final Map<String, Event> eventNameMap = new HashMap<>();
    
    private final Filters filters;
     
    public EventsDatabase(EventLoader eventLoader, Filters filters) {
        
        this.filters = filters;
        this.filters.addListener(this);
        
        eventLoader.addListener(this);
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// Expose Observables
    ////////////////////////////////////////////////////////////////////////////
    public ObservableList<Event> getEventsTableModel() {
        return this.tableList;
    }
    
    public ObservableList getAccountModel() {
        return this.accountList;
    }
    
    public ObservableList getRegionModel() {
        return this.regionList;
    }

    public ObservableList getUsernameModel() {
        return this.usernameList;
    }
    
    public ObservableList getEventNameModel() {
        return this.eventList;
    }
    
   
    ////////////////////////////////////////////////////////////////////////////
    ///// EventLoaderListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void newEvents(List<Event> events) {
    
        accountMap.put("", null);
        usernameMap.put("", null);
        eventNameMap.put("", null);
        regionMap.put("", null);
        
        for(Event event : events) {
            masterEventsMap.put(event.getEventId(), event);
            
            if (event.getUserIdentity().getAccountId() != null &&
                event.getUserIdentity().getAccountId().length() > 0) {
                
                accountMap.put(event.getUserIdentity().getAccountId(), event);
            }
            
            if (event.getUserIdentity().getUserName() != null &&
                event.getUserIdentity().getUserName().length() > 0) {
                
                usernameMap.put(event.getUserIdentity().getUserName(), event);
            }
            
            if (event.getEventName() != null &&
                event.getEventName().length() > 0) {
                
                eventNameMap.put(event.getEventName(), event);
            }
            
            if (event.getAwsRegion() != null &&
                event.getAwsRegion().length() > 0) {
                
                regionMap.put(event.getAwsRegion(), event);
            }  
        }
        
        updateTable(masterEventsMap.values());
        
        updateChoice(accountMap.keySet(), accountList);
        updateChoice(usernameMap.keySet(), usernameList);
        updateChoice(eventNameMap.keySet(), eventList);
        updateChoice(regionMap.keySet(), regionList);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventFilterListener implementation
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFilterChanged() {
        
        List<Event> filteredEvents = filters.filterEvents(masterEventsMap.values());
        updateTable(filteredEvents);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void updateChoice(Collection data, ObservableList list) {
        
        list.setAll(data);
    }
    
    private void updateTable(Collection data) {
        
        tableList.setAll(data);
    }
}
