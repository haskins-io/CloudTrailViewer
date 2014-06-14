/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2014  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package cloudtrailviewer.components;

import cloudtrailviewer.events.EventsDatabase;
import cloudtrailviewer.filters.AccountFilter;
import cloudtrailviewer.filters.EventNameFilter;
import cloudtrailviewer.filters.Filters;
import cloudtrailviewer.filters.FreeformFilter;
import cloudtrailviewer.filters.RegionFilter;
import cloudtrailviewer.filters.UsernameFilter;
import cloudtrailviewer.models.Event;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author mark
 */
public class FilterPane extends AbstractPane {
    
    private final BorderPane pane = new BorderPane();
    private final TextField search = new TextField();
    
    private final ChoiceBox eventName = new ChoiceBox();
    
    private final ObservableList accountList = FXCollections.observableArrayList();
    private final ObservableList regionList = FXCollections.observableArrayList();
    private final ObservableList usernameList = FXCollections.observableArrayList();
    private final ObservableList eventList = FXCollections.observableArrayList();
    
    private final Filters filters;
    private final AccountFilter accountFilter = new AccountFilter();
    private final EventNameFilter eventNameFilter = new EventNameFilter();
    private final FreeformFilter freeformFilter = new FreeformFilter();
    private final RegionFilter regionFilter = new RegionFilter();
    private final UsernameFilter usernameFilter = new UsernameFilter();
    
    private String selectedAccount = "";
    private String selectedEventName = "";
    private String selectedRegion = "";
    private String selectedUsername = "";
    
    public FilterPane(Scene scene, EventsDatabase eventsDatabase, Filters filters) {
        
        super(scene);
        
        eventsDatabase.addListeners(this);
        
        this.filters = filters;
        
        buildPane();
        
        addFilters();
    }
    
    public BorderPane getPane() {
        
        return pane;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventsDatabaseListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onEventsUpdated(Map<String, Event> updatedEvents) {
        
        updateControls(updatedEvents.values());
    }
        
    ////////////////////////////////////////////////////////////////////////////
    ///// protected methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void widthChanged(Number newWidth) {
         search.setPrefWidth(newWidth.doubleValue() - 630);
    }
    
    @Override
    protected void heightChanged(Number newHeight) {
        // Not implemented
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildPane() {
        
        pane.setPrefHeight(30);
        
        // --- Top
        ChoiceBox account = new ChoiceBox();
        account.setPrefWidth(150);
        account.setItems(accountList);
        account.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                accountFilter.setAccount(newValue);
            }
        });
        
        ChoiceBox region = new ChoiceBox();
        region.setPrefWidth(150);
        region.setItems(regionList);
        region.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                regionFilter.setRegion(newValue);
            }
        });
                
        ChoiceBox userName = new ChoiceBox();
        userName.setPrefWidth(150);
        userName.setItems(usernameList);
        userName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                usernameFilter.setUsername(newValue);
            }
        });
        
        
        eventName.setPrefWidth(150);
        eventName.setItems(eventList);
        eventName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedEventName = newValue;
                eventNameFilter.setEventName(selectedEventName);
            }
        });
        
        search.setPrefWidth(pane.getWidth() - 630);
        search.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
                    freeformFilter.setValue(search.getText());
                }
            }
        });
        
        HBox choiceContainer = new HBox();
        choiceContainer.setPadding(new Insets(5,5,5,5));
        choiceContainer.setSpacing(5);
        choiceContainer.setPrefHeight(30);
        
        choiceContainer.getChildren().addAll(account, region, userName, eventName, search);

        pane.setTop(choiceContainer);
    }
        
    private void addFilters() {
        
        filters.addEventFilter(this.accountFilter);
        filters.addEventFilter(this.eventNameFilter);
        filters.addEventFilter(this.freeformFilter);
        filters.addEventFilter(this.regionFilter);
        filters.addEventFilter(this.usernameFilter);
    }
    
    private void updateControls(Collection<Event> events) {
        
        Map<String, Event> accountMap   = new HashMap<String, Event>();
        Map<String, Event> regionMap    = new HashMap<String, Event>();
        Map<String, Event> usernameMap  = new HashMap<String, Event>();
        Map<String, Event> eventNameMap = new HashMap<String, Event>();
        
        accountMap.put("", null);
        usernameMap.put("", null);
        eventNameMap.put("", null);
        regionMap.put("", null);
        
        for(Event event : events) {
            
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
        
        updateChoice(accountMap.keySet(), accountList);
        updateChoice(usernameMap.keySet(), usernameList);
        updateChoice(eventNameMap.keySet(), eventList);
        updateChoice(regionMap.keySet(), regionList);
    }
    
    private void updateChoice(Collection data, ObservableList list) {
        
        list.setAll(data);
    }
}
