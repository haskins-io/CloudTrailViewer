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
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.observable.KeyIntegerValue;
import io.haskins.java.cloudtrailviewer.service.DataService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    protected FontAwesomeIconView getWidgetIcon() {
        return new FontAwesomeIconView(FontAwesomeIcon.TABLE);
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


        try {
            List<KeyIntegerValue> items = new ArrayList<>();

            TermStats[] top = dataService.getTop(widget.getTop(), widget.getSeriesField());
            for (TermStats stat : top) {

                KeyIntegerValue kv = new KeyIntegerValue(stat.termtext.utf8ToString(), stat.docFreq);
                items.add(kv);
            }

            data.addAll(items);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DataService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        tableView.setPrefWidth(widget.getWidth());
        tableView.setPrefHeight(widget.getHeight());
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addTopColumns(widget);
        tableView.setItems(keyValueData);

        tableView.setRowFactory(tv -> {
            TableRow<KeyIntegerValue> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    KeyIntegerValue rowData = row.getItem();

                    try {
                        TopDocs result = LuceneUtils.performQuery(widget.luceneDir(), widget.getSeriesField(), rowData.getField());
                        eventTableService.setTableEvents(result, widget.getType());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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

    public void newEvent(org.apache.lucene.document.Document document) { }
}
