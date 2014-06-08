package com.haskins.aws.jcloudtrailviewer.components;

import com.haskins.aws.jcloudtrailviewer.events.EventsDatabase;
import com.haskins.aws.jcloudtrailviewer.models.Event;
import com.haskins.aws.jcloudtrailviewer.models.EventDetail;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark
 */
public class MainPane {
    
    private final Scene scene;
    
    private final BorderPane borderPane = new BorderPane();
    private final TableView mainTable = new TableView();
    private final TableView detailTable = new TableView();
    private final TextArea jsonTextArea = new TextArea();
    
    private final EventsDatabase eventsDatabase;
    
    private final ObservableList<EventDetail> detailTableModel = FXCollections.observableArrayList();
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    public MainPane(Scene scene, EventsDatabase eventsDatabase) {
        
        this.eventsDatabase = eventsDatabase;
        this.scene = scene;
        
        addListeners();
    }
    
    public BorderPane getPane() {
        
        // --- Center Pane
        createMainTablePane();

        // --- Right Pane
        createDetailTable();
        
        return borderPane;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void addListeners() {
                
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                borderPane.setPrefWidth(newSceneWidth.doubleValue());
            }
        });
        
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                borderPane.setPrefHeight(newSceneHeight.doubleValue() - 60);
            }
        }); 
    }
    
    private void createMainTablePane() { 
 
        TableColumn eventTimeCol = new TableColumn("Date/Time");
        eventTimeCol.setMinWidth(200);
        eventTimeCol.setCellValueFactory(new PropertyValueFactory<>("eventTime"));
        
        TableColumn eventNameCol = new TableColumn("Event Name");
        eventNameCol.setMinWidth(260);
        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        
        TableColumn userNameCol = new TableColumn("Username");
        userNameCol.setMinWidth(100);
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userIdentity.userName"));
        
        TableColumn eventSourceCol = new TableColumn("Event Source");
        eventSourceCol.setMinWidth(260);
        eventSourceCol.setCellValueFactory(new PropertyValueFactory<>("eventSource"));
        
        TableColumn regionCol = new TableColumn("Region");
        regionCol.setMinWidth(100);
        regionCol.setCellValueFactory(new PropertyValueFactory<>("awsRegion"));
        
        TableColumn userAgentCol = new TableColumn("User Agent");
        userAgentCol.setMinWidth(300);
        userAgentCol.setCellValueFactory(new PropertyValueFactory<>("userAgent"));
                
        mainTable.getColumns().addAll(eventTimeCol, eventNameCol, userNameCol, eventSourceCol, regionCol, userAgentCol);
        
        mainTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(mainTable.getSelectionModel().getSelectedItem() != null)  {    
                    
                    TableViewSelectionModel selectionModel = mainTable.getSelectionModel();
                    ObservableList selected = selectionModel.getSelectedItems();
                    Event event = (Event)selected.get(0);
                    
                    showEventDetail(event);
                }
            }
        });
        
        mainTable.setItems(eventsDatabase.getEventsTableModel());
        
        borderPane.setCenter(mainTable);
    }
    
    private void createDetailTable() {

        // --- Table tab
        TableColumn label = new TableColumn();
        label.setMinWidth(150);
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        
        TableColumn detail = new TableColumn();
        detail.setMinWidth(450);
        detail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        
        detailTable.setPrefWidth(600);
        detailTable.getColumns().addAll(label, detail);
        detailTable.setItems(detailTableModel);
        
        Tab tableTab = new Tab();
        tableTab.setText("Table");
        tableTab.setContent(detailTable);
        
        // --- Raw Tab
        jsonTextArea.setEditable(false);
        
        Tab jsonTab = new Tab();
        jsonTab.setText("JSON");
        jsonTab.setContent(jsonTextArea);
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(tableTab, jsonTab);
        
        borderPane.setRight(tabPane);
    }
    
    private void showEventDetail(Event event) {
                
        // -- update Table
        detailTableModel.clear();
        
        detailTableModel.add(new EventDetail("Event Version", event.getEventVersion()));
        
        if (event.getRawUserIdentity()== null && event.getUserIdentity() != null) {
            try {
                event.setRawUserIdentity(mapper.defaultPrettyPrintingWriter().writeValueAsString(event.getUserIdentity()) );
            }
            catch (IOException ex) {
                event.setRawUserIdentity("");
            }
        }
        detailTableModel.add(new EventDetail("User Identity", event.getRawUserIdentity()));
        detailTableModel.add(new EventDetail("Event Time", event.getEventTime()));
        detailTableModel.add(new EventDetail("Event Source", event.getEventSource()));
        detailTableModel.add(new EventDetail("Event Name", event.getEventName()));
        detailTableModel.add(new EventDetail("AWS Region", event.getAwsRegion()));
        detailTableModel.add(new EventDetail("Source IP Address", event.getSourceIPAddress()));
        detailTableModel.add(new EventDetail("UserAgent", event.getUserAgent()));
        
        if (event.getRawRequestParameters() == null && event.getRequestParameters() != null) {
            try {
                event.setRawRequestParameters( mapper.defaultPrettyPrintingWriter().writeValueAsString(event.getRequestParameters()) );
            }
            catch (IOException ex) {
                event.setRawRequestParameters("");
            }
        }
        detailTableModel.add(new EventDetail("Request Parameters", event.getRawRequestParameters()));
        
        if (event.getRawResponseElements() == null && event.getResponseElements()!= null) {
            try {
                event.setRawResponseElements( mapper.defaultPrettyPrintingWriter().writeValueAsString(event.getResponseElements()) );
            }
            catch (IOException ex) {
                event.setRawResponseElements("");
            }
        }
        detailTableModel.add(new EventDetail("Response Elements", event.getRawResponseElements()));
        
        detailTableModel.add(new EventDetail("Request ID", event.getRequestId()));
        detailTableModel.add(new EventDetail("EventID", event.getEventId()));
        detailTableModel.add(new EventDetail("Error Code", event.getErrorCode()));
        detailTableModel.add(new EventDetail("Error Message", event.getErrorMessage()));
        
        
        // -- put json in textarea
        jsonTextArea.setText(event.getRawJSON());
    }
}
