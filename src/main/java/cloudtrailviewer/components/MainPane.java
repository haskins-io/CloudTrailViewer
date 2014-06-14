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
import cloudtrailviewer.models.Event;
import cloudtrailviewer.models.EventDetail;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
public class MainPane extends AbstractPane {
            
    private final ObservableList accountList = FXCollections.observableArrayList();
    private final ObservableList<EventDetail> detailTableModel = FXCollections.observableArrayList();
    
    private final BorderPane borderPane = new BorderPane();
    private final TableView mainTable = new TableView();
    private final TableView detailTable = new TableView();
    private final TextArea jsonTextArea = new TextArea();
        
    private final ObjectMapper mapper = new ObjectMapper();
    
    public MainPane(Scene scene, EventsDatabase eventsDatabase, FilterPane filterPane) {
        
        super(scene);
        
        eventsDatabase.addListeners(this);
    }
    
    public BorderPane getPane() {
        
        // --- Center Pane
        createMainTablePane();

        // --- Right Pane
        createDetailTable();
        
        return borderPane;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventsDatabaseListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onEventsUpdated(Map<String, Event> updatedEvents) {
        
        Collection<Event> events = updatedEvents.values();
        
        Map<String, Event> eventMap   = new HashMap<String, Event>();
        
        for(Event event : events) {
            
            eventMap.put(event.getEventId(), event);
 
        }
        
        accountList.setAll(events);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// protected methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void widthChanged(Number newWidth) {
         borderPane.setPrefWidth(newWidth.doubleValue());
    }
    
    @Override
    protected void heightChanged(Number newHeight) {
        borderPane.setPrefHeight(newHeight.doubleValue() - 60);
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////    
    private void createMainTablePane() { 
 
        TableColumn eventTimeCol = new TableColumn("Date/Time");
        eventTimeCol.setMinWidth(200);
        eventTimeCol.setCellValueFactory(new PropertyValueFactory<Event,String>("eventTime"));
        
        TableColumn eventNameCol = new TableColumn("Event Name");
        eventNameCol.setMinWidth(260);
        eventNameCol.setCellValueFactory(new PropertyValueFactory<Event,String>("eventName"));
        
        TableColumn userNameCol = new TableColumn("Username");
        userNameCol.setMinWidth(100);
        userNameCol.setCellValueFactory(new PropertyValueFactory<Event,String>("userIdentity.userName"));
        
        TableColumn eventSourceCol = new TableColumn("Event Source");
        eventSourceCol.setMinWidth(260);
        eventSourceCol.setCellValueFactory(new PropertyValueFactory<Event,String>("eventSource"));
        
        TableColumn regionCol = new TableColumn("Region");
        regionCol.setMinWidth(100);
        regionCol.setCellValueFactory(new PropertyValueFactory<Event,String>("awsRegion"));
        
        TableColumn userAgentCol = new TableColumn("User Agent");
        userAgentCol.setMinWidth(300);
        userAgentCol.setCellValueFactory(new PropertyValueFactory<Event,String>("userAgent"));
                
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
        
        mainTable.setItems(accountList);
        
        borderPane.setCenter(mainTable);
    }
    
    private void createDetailTable() {

        // --- Table tab
        TableColumn label = new TableColumn();
        label.setMinWidth(150);
        label.setCellValueFactory(new PropertyValueFactory<Event,String>("label"));
        
        TableColumn detail = new TableColumn();
        detail.setMinWidth(450);
        detail.setCellValueFactory(new PropertyValueFactory<Event,String>("detail"));
        
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
