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

import io.haskins.java.cloudtrailviewer.model.observable.FilterChoiceObservable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Provides the logic for the filter panel
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FilterPanelController {

    @FXML
    private ComboBox<FilterChoiceObservable> filter;


    @FXML
    public void initialize() {

        filter.setCellFactory(new Callback<ListView<FilterChoiceObservable>, ListCell<FilterChoiceObservable>>() {

            @Override
            public ListCell<FilterChoiceObservable> call(ListView<FilterChoiceObservable> param) {

                final ListCell<FilterChoiceObservable> cell = new ListCell<FilterChoiceObservable>() {

                    @Override
                    public void updateItem(FilterChoiceObservable item, boolean empty) {

                        super.updateItem(item, empty);

                        if (item != null) {
                            setText(item.getName());
                        }
                        else {
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });
    }
}

