/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import io.haskins.java.cloudtrailviewer.controls.warningcell.WarningCellFactory;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.filter.EventFieldFilter;
import io.haskins.java.cloudtrailviewer.filter.Filter;
import io.haskins.java.cloudtrailviewer.model.observable.LogFileFilter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

/**
 * Provides the logic for the filter panel
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FilterPanelController extends BorderPane {

    @FXML private TableView<LogFileFilter> filterTable;

    @FXML private Button addBtn;

    private final ObservableList<LogFileFilter> data = FXCollections.observableArrayList();

    private final ContextMenu addFilterMenu = new ContextMenu();

    private FilterPanelControllerListener listener;

    private CompositeFilter filters = new CompositeFilter();

    @FXML
    public void initialize() {

        initTable();
        initContextMenu();

        addBtn.setOnAction(e -> addFilterMenu.show(addBtn, Side.BOTTOM, 0, 0));
    }

    @FXML
    private void remove() {

        LogFileFilter selected = filterTable.getSelectionModel().getSelectedItem();
        filterTable.getItems().remove(selected);
    }

    @FXML private void modeOr() {
        filters.setMode(CompositeFilter.BITWISE_OR);
    }

    @FXML private void modeAnd() {
        filters.setMode(CompositeFilter.BITWISE_AND);
    }

    void addListener(FilterPanelControllerListener listener) {
        this.listener = listener;
    }

    CompositeFilter getFilters() {
        return filters;
    }

    private void initTable() {

        TableColumn<LogFileFilter, String> nameCol =  new TableColumn<>("Filter");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<LogFileFilter, String> valueCol =  new TableColumn<>("Value");
        valueCol.setMinWidth(100);
        valueCol.setCellValueFactory(new PropertyValueFactory<>("needle"));

        valueCol.setCellFactory(new WarningCellFactory());
        valueCol.setOnEditCommit(
            (TableColumn.CellEditEvent<LogFileFilter, String> t) -> {

                LogFileFilter acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                acct.setNeedle(t.getNewValue());

                filtersUpdated();
            }
        );

        filterTable.getColumns().addAll(nameCol, valueCol);
        filterTable.setEditable(true);
        filterTable.setItems(data);

        data.addListener(new ListChangeListener<LogFileFilter>(){

            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends LogFileFilter> pChange) {
                while(pChange.next()) {
                    filtersUpdated();
                }
            }
        });
    }

    private void initContextMenu() {

        configureMenuItem(new MenuItem("No Filter"), new LogFileFilter("No Filter", "", "", ""));

        configureMenuItem(new MenuItem("Text Filter"), new LogFileFilter("Text Filter", "AllFilter", "", ""));
        configureMenuItem(new MenuItem("Date Filter"), new LogFileFilter("Date Filter", "DateFilter", "", ""));
        configureMenuItem(new MenuItem("Ignore Filter"), new LogFileFilter("Ignore Filter", "IgnoreFilter", "", ""));

        configureMenuItem(new MenuItem("Event Name"), new LogFileFilter("Event Name", "EventFieldFilter", "eventName", ""));
        configureMenuItem(new MenuItem("AWS Region"), new LogFileFilter("AWS Region", "EventFieldFilter", "awsRegion", ""));
        configureMenuItem(new MenuItem("Source IP Address"), new LogFileFilter("Source IP Address", "EventFieldFilter", "sourceIPAddress", ""));
        configureMenuItem(new MenuItem("User Agent"), new LogFileFilter("User Agent", "EventFieldFilter", "userAgent", ""));
        configureMenuItem(new MenuItem("Event Source"), new LogFileFilter("Event Source", "EventFieldFilter", "eventSource", ""));
        configureMenuItem(new MenuItem("Error Code"), new LogFileFilter("Error Code", "EventFieldFilter", "errorCode", ""));
        configureMenuItem(new MenuItem("Error Message"), new LogFileFilter("Error Message", "EventFieldFilter", "errorMessage", ""));
        configureMenuItem(new MenuItem("Recipient Account Id"), new LogFileFilter("Recipient Account Id", "EventFieldFilter", "recipientAccountId", ""));

        configureMenuItem(new MenuItem("User Identity : Type"), new LogFileFilter("User Identity : Type", "EventFieldFilter", "userIdentity.type", ""));
        configureMenuItem(new MenuItem("User Identity : Principal Id"), new LogFileFilter("User Identity : Principal Id", "EventFieldFilter", "userIdentity.principalId", ""));
        configureMenuItem(new MenuItem("User Identity : Arn"), new LogFileFilter("User Identity : Arn", "EventFieldFilter", "userIdentity.arn", ""));
        configureMenuItem(new MenuItem("User Identity : Account Id"), new LogFileFilter("User Identity : Account Id", "EventFieldFilter", "userIdentity.accountId", ""));
        configureMenuItem(new MenuItem("User Identity : Access Key Id"), new LogFileFilter("User Identity : Access Key Id", "EventFieldFilter", "userIdentity.accessKeyId", ""));
        configureMenuItem(new MenuItem("User Identity : User name"), new LogFileFilter("User Identity : User name", "EventFieldFilter", "userIdentity.userName", ""));
        configureMenuItem(new MenuItem("User Identity : Invoked By"), new LogFileFilter("User Identity : Invoked By", "EventFieldFilter", "userIdentity.invokedBy", ""));
        configureMenuItem(new MenuItem("User Identity : Web Id Federation Data"), new LogFileFilter("User Identity : Web Id Federation Data", "EventFieldFilter", "userIdentity.webIdFederationData", ""));

        configureMenuItem(new MenuItem("Session Issuer : Type"), new LogFileFilter("Session Issuer : Type", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.type", ""));
        configureMenuItem(new MenuItem("Session Issuer : Principal Id"), new LogFileFilter("Session Issuer : Principal Id", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.principalId", ""));
        configureMenuItem(new MenuItem("Session Issuer : Arn"), new LogFileFilter("Session Issuer : Arn", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.arn", ""));
        configureMenuItem(new MenuItem("Session Issuer : Account Id"), new LogFileFilter("Session Issuer : Account Id", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.accountId", ""));
        configureMenuItem(new MenuItem("Session Issuer : User name"), new LogFileFilter("Session Issuer : User name", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.userName", ""));

    }

    private void configureMenuItem(MenuItem menuItem, LogFileFilter observable) {

        menuItem.setUserData(observable);

        menuItem.setOnAction(e -> {

            LogFileFilter selected = (LogFileFilter)((MenuItem)e.getSource()).getUserData();
            filterTable.getItems().add(selected);
        });

        addFilterMenu.getItems().add(menuItem);
    }

    private void filtersUpdated() {

        boolean passed = true;

        if (data.size() == 0) {
            passed = false;

        } else {
            for (LogFileFilter aData : data) {
                passed &= aData.isfilterConfigure();
            }
        }

        if (passed) {
            for (LogFileFilter aData : data) {
                filters.addFilter(getFilter(aData));
            }
        } else {
            filters.clear();
        }

        listener.scanAvailable(passed);
    }

    private Filter getFilter(LogFileFilter filterChoiceObservable) {

        Filter filter = null;

        try {

            String filterFQPN = "io.haskins.java.cloudtrailviewer.filter." + filterChoiceObservable.getFilter();
            Class filterClass = Class.forName(filterFQPN);

            if (filterClass != null) {

                filter = (Filter)filterClass.newInstance();
                if (filter instanceof EventFieldFilter) {
                    ((EventFieldFilter)filter).setOption(filterChoiceObservable.getField());
                }
                filter.setNeedle(filterChoiceObservable.getNeedle());
            }

        } catch (InstantiationException | IllegalAccessException |ClassNotFoundException ex) {
//            LOGGER.log(Level.WARNING, "Failed to load filter", ex);
        }

        return filter;
    }
}

