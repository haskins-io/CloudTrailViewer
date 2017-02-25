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

package io.haskins.java.cloudtrailviewer.controller.widget;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.observable.KeyIntegerValue;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Controller that provides a Table Widget
 *
 * Created by markhaskins on 09/01/2017.
 */
public class TableWidgetController extends AbstractBaseController {

    @FXML private TableView tableView;

    public BorderPane loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/TableWidget.fxml"));
        loader.setController(this);
        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.TABLE);
    }

    public void newEvents(List<Event> events) {
        for (Event event : events) {
            newEvent(event);
        }
    }

    public void loadingFile(int fileName, int totalFiles) { }

    @Override
    public void finishedLoading(boolean reload) {

        ObservableList<KeyIntegerValue> data;
        if (reload) {
            tableView.getItems().clear();
            data = tableView.getItems();
        } else {
            data = tableView.getItems();
        }

        List<KeyIntegerValue> items = new ArrayList<>();

        List<Map.Entry<String, Integer>> topEvents = getTopEvents();
        for (Map.Entry<String, Integer> entry : topEvents) {

            KeyIntegerValue kv = new KeyIntegerValue(entry.getKey(), entry.getValue());
            items.add(kv);
        }

        data.addAll(items);
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        tableView.setPrefWidth(widget.getWidth());
        tableView.setPrefHeight(widget.getHeight());

        addTopColumns(widget);
        tableView.setItems(keyValueData);

        tableView.setRowFactory(tv -> {
            TableRow<KeyIntegerValue> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    KeyIntegerValue rowData = row.getItem();
                    eventTableService.setTableEvents(keyValueMap.get(rowData.getField()));
                }
            });
            return row;
        });
    }

    private void addTopColumns(DashboardWidget widget) {

        TableColumn col1 = new TableColumn(widget.getSeriesField());
        col1.setCellValueFactory(new PropertyValueFactory("Field"));

        TableColumn col2 = new TableColumn("Count");
        col2.setCellValueFactory(new PropertyValueFactory("Count"));

        tableView.getColumns().addAll(col1, col2);
    }
}
