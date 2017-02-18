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

import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.filter.EventFieldFilter;
import io.haskins.java.cloudtrailviewer.filter.Filter;
import io.haskins.java.cloudtrailviewer.model.observable.FilterChoiceObservable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;

import java.util.logging.Level;

/**
 * Provides the logic for the filter panel
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FilterPanelController extends BorderPane {

    @FXML private TableView<FilterChoiceObservable> filterTable;

    @FXML private Button addBtn;

    private final ObservableList<FilterChoiceObservable> data = FXCollections.observableArrayList();

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

        FilterChoiceObservable selected = filterTable.getSelectionModel().getSelectedItem();
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

        TableColumn<FilterChoiceObservable, String> nameCol =  new TableColumn<>("Filter");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<FilterChoiceObservable, String> valueCol =  new TableColumn<>("Value");
        valueCol.setMinWidth(100);
        valueCol.setCellValueFactory(new PropertyValueFactory<>("needle"));

        valueCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valueCol.setOnEditCommit(
            (TableColumn.CellEditEvent<FilterChoiceObservable, String> t) -> {

                FilterChoiceObservable acct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                acct.setNeedle(t.getNewValue());
                filtersUpdated();

            }
        );

        filterTable.getColumns().addAll(nameCol, valueCol);
        filterTable.setEditable(true);
        filterTable.setItems(data);

        data.addListener(new ListChangeListener<FilterChoiceObservable>(){

            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends FilterChoiceObservable> pChange) {
                while(pChange.next()) {
                    filtersUpdated();
                }
            }
        });
    }

    private void initContextMenu() {

        configureMenuItem(new MenuItem("No Filter"), new FilterChoiceObservable("No Filter", "", "", ""));

        configureMenuItem(new MenuItem("Text Filter"), new FilterChoiceObservable("Text Filter", "AllFilter", "", ""));
        configureMenuItem(new MenuItem("Date Filter"), new FilterChoiceObservable("Date Filter", "DateFilter", "", ""));
        configureMenuItem(new MenuItem("Ignore Filter"), new FilterChoiceObservable("Ignore Filter", "IgnoreFilter", "", ""));

        configureMenuItem(new MenuItem("Event Name"), new FilterChoiceObservable("Event Name", "EventFieldFilter", "eventName", ""));
        configureMenuItem(new MenuItem("AWS Region"), new FilterChoiceObservable("AWS Region", "EventFieldFilter", "awsRegion", ""));
        configureMenuItem(new MenuItem("Source IP Address"), new FilterChoiceObservable("Source IP Address", "EventFieldFilter", "sourceIPAddress", ""));
        configureMenuItem(new MenuItem("User Agent"), new FilterChoiceObservable("User Agent", "EventFieldFilter", "userAgent", ""));
        configureMenuItem(new MenuItem("Event Source"), new FilterChoiceObservable("Event Source", "EventFieldFilter", "eventSource", ""));
        configureMenuItem(new MenuItem("Error Code"), new FilterChoiceObservable("Error Code", "EventFieldFilter", "errorCode", ""));
        configureMenuItem(new MenuItem("Error Message"), new FilterChoiceObservable("Error Message", "EventFieldFilter", "errorMessage", ""));
        configureMenuItem(new MenuItem("Recipient Account Id"), new FilterChoiceObservable("Recipient Account Id", "EventFieldFilter", "recipientAccountId", ""));

        configureMenuItem(new MenuItem("User Identity : Type"), new FilterChoiceObservable("User Identity : Type", "EventFieldFilter", "userIdentity.type", ""));
        configureMenuItem(new MenuItem("User Identity : Principal Id"), new FilterChoiceObservable("User Identity : Principal Id", "EventFieldFilter", "userIdentity.principalId", ""));
        configureMenuItem(new MenuItem("User Identity : Arn"), new FilterChoiceObservable("User Identity : Arn", "EventFieldFilter", "userIdentity.arn", ""));
        configureMenuItem(new MenuItem("User Identity : Account Id"), new FilterChoiceObservable("User Identity : Account Id", "EventFieldFilter", "userIdentity.accountId", ""));
        configureMenuItem(new MenuItem("User Identity : Access Key Id"), new FilterChoiceObservable("User Identity : Access Key Id", "EventFieldFilter", "userIdentity.accessKeyId", ""));
        configureMenuItem(new MenuItem("User Identity : User name"), new FilterChoiceObservable("User Identity : User name", "EventFieldFilter", "userIdentity.userName", ""));
        configureMenuItem(new MenuItem("User Identity : Invoked By"), new FilterChoiceObservable("User Identity : Invoked By", "EventFieldFilter", "userIdentity.invokedBy", ""));
        configureMenuItem(new MenuItem("User Identity : Web Id Federation Data"), new FilterChoiceObservable("User Identity : Web Id Federation Data", "EventFieldFilter", "userIdentity.webIdFederationData", ""));

        configureMenuItem(new MenuItem("Session Issuer : Type"), new FilterChoiceObservable("Session Issuer : Type", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.type", ""));
        configureMenuItem(new MenuItem("Session Issuer : Principal Id"), new FilterChoiceObservable("Session Issuer : Principal Id", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.principalId", ""));
        configureMenuItem(new MenuItem("Session Issuer : Arn"), new FilterChoiceObservable("Session Issuer : Arn", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.arn", ""));
        configureMenuItem(new MenuItem("Session Issuer : Account Id"), new FilterChoiceObservable("Session Issuer : Account Id", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.accountId", ""));
        configureMenuItem(new MenuItem("Session Issuer : User name"), new FilterChoiceObservable("Session Issuer : User name", "EventFieldFilter", "userIdentity.sessionContext.sessionIssuer.userName", ""));

    }

    private void configureMenuItem(MenuItem menuItem, FilterChoiceObservable observable) {

        menuItem.setUserData(observable);

        menuItem.setOnAction(e -> {

            FilterChoiceObservable selected = (FilterChoiceObservable)((MenuItem)e.getSource()).getUserData();
            filterTable.getItems().add(selected);
        });

        addFilterMenu.getItems().add(menuItem);
    }

    private void filtersUpdated() {

        boolean passed = true;

        if (data.size() == 0) {
            passed = false;

        } else {
            for (FilterChoiceObservable aData : data) {
                passed &= aData.isfilterConfigure();
            }
        }

        if (passed) {
            for (FilterChoiceObservable aData : data) {
                filters.addFilter(getFilter(aData));
            }
        } else {
            filters.clear();
        }

        listener.scanAvailable(passed);
    }

    private Filter getFilter(FilterChoiceObservable filterChoiceObservable) {

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

