package com.haskins.aws.jcloudtrailviewer.components;

import com.haskins.aws.jcloudtrailviewer.events.EventsDatabase;
import com.haskins.aws.jcloudtrailviewer.filters.AccountFilter;
import com.haskins.aws.jcloudtrailviewer.filters.EventNameFilter;
import com.haskins.aws.jcloudtrailviewer.filters.Filters;
import com.haskins.aws.jcloudtrailviewer.filters.FreeformFilter;
import com.haskins.aws.jcloudtrailviewer.filters.RegionFilter;
import com.haskins.aws.jcloudtrailviewer.filters.UsernameFilter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class FilterPane {

    private final EventsDatabase eventsDatabase;
    private final Scene scene;
    
    private final BorderPane pane = new BorderPane();
    private final TextField search = new TextField();
    
    private final Filters filters;
    private final AccountFilter accountFilter = new AccountFilter();
    private final EventNameFilter eventNameFilter = new EventNameFilter();
    private final FreeformFilter freeformFilter = new FreeformFilter();
    private final RegionFilter regionFilter = new RegionFilter();
    private final UsernameFilter usernameFilter = new UsernameFilter();
    
    public FilterPane(Scene scene, EventsDatabase eventsDatabase, Filters filters) {
        
        this.eventsDatabase = eventsDatabase;
        this.scene = scene;
        
        this.filters = filters;
        
        buildPane();
        
        addListeners();
        addFilters();
    }
    
    public BorderPane getPane() {
        
        return pane;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildPane() {
        
        pane.setPrefHeight(30);
        
        // --- Top
        ChoiceBox account = new ChoiceBox();
        account.setPrefWidth(150);
        account.setItems(eventsDatabase.getAccountModel());
        account.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                accountFilter.setAccount(newValue);
            }
        });
        
        ChoiceBox region = new ChoiceBox();
        region.setPrefWidth(150);
        region.setItems(eventsDatabase.getRegionModel());
        region.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                regionFilter.setRegion(newValue);
            }
        });
        
        ChoiceBox userName = new ChoiceBox();
        userName.setPrefWidth(150);
        userName.setItems(eventsDatabase.getUsernameModel());
        userName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                usernameFilter.setUsername(newValue);
            }
        });
        
        ChoiceBox eventName = new ChoiceBox();
        eventName.setPrefWidth(150);
        eventName.setItems(eventsDatabase.getEventNameModel());
        eventName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                eventNameFilter.setEventName(newValue);
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
    
    private void addListeners() {
                        
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                search.setPrefWidth(newSceneWidth.doubleValue() - 630);
            }
        }); 
    }
    
    private void addFilters() {
        
        filters.addEventFilter(this.accountFilter);
        filters.addEventFilter(this.eventNameFilter);
        filters.addEventFilter(this.freeformFilter);
        filters.addEventFilter(this.regionFilter);
        filters.addEventFilter(this.usernameFilter);
    }
}
