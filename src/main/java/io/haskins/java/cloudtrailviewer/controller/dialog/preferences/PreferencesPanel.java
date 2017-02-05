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


package io.haskins.java.cloudtrailviewer.controller.dialog.preferences;

import io.haskins.java.cloudtrailviewer.model.dao.ResultSetRow;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;

import java.util.List;

/**
 * Abstract class providing common functionality for Preferences Panels.
 *
 * Created by markhaskins on 04/02/2017.
 */
abstract class PreferencesPanel {

    DatabaseService databaseService;

    abstract void removeItem(String itemToRemove);
    abstract void addItem(String itemToAdd);

    private int selectedIndex = 0;
    private String oldValue = "";

    @FXML ListView<String> listView;

    @FXML
    private void add() {

        listView.getItems().add(0,"");
        listView.edit(0);
    }

    @FXML
    private void remove() {
        removeItem(listView.getSelectionModel().getSelectedItem());
    }

    void configureListView() {

        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());

        listView.setOnEditStart(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {
                selectedIndex = listView.getSelectionModel().getSelectedIndex();
                oldValue = listView.getSelectionModel().getSelectedItem();
            }

        });

        listView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {

                listView.getItems().set(t.getIndex(), t.getNewValue());
                String newValue = listView.getSelectionModel().getSelectedItem();

                if (oldValue == null) {
                    addItem(newValue);
                }

                if (oldValue != null && !newValue.equalsIgnoreCase(oldValue)) {

                    removeItem(oldValue);
                    addItem(newValue);
                }
            }
        });

        listView.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> t) {

                String newValue = listView.getSelectionModel().getSelectedItem();

                if ( (oldValue == null && newValue == null) ||
                     (oldValue == null && newValue.trim().length() == 0) ) {

                    if (selectedIndex == -1) {
                        listView.getItems().remove(0);
                    } else {
                        listView.getItems().remove(selectedIndex);
                    }

                }

                if (oldValue != null &&
                    newValue != null &&
                    newValue.trim().equalsIgnoreCase("")) {
                        listView.getItems().remove(oldValue);
                }
            }
        });
    }

    void populateListBySQL(String sqlQuery) {

        listView.getItems().clear();

        List<ResultSetRow> rows = databaseService.executeCursorStatement(sqlQuery);
        for (ResultSetRow row : rows) {

            String event_name = (String)row.get("api_call");
            listView.getItems().add(event_name);
        }
    }
}

