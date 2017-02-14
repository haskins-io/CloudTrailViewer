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

package io.haskins.java.cloudtrailviewer.controller.dialog.widget;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.observable.KeyStringValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Dialog that provides functionality to modify a Stacked Bar Chart widget
 *
 * Created by markhaskins on 25/01/2017.
 */
public class StackedBarChartDialogController extends AbstractDialogController {

    @FXML private TextField top;
    @FXML private ChoiceBox<KeyStringValue> category;
    @FXML private ChoiceBox<String> orientation;

    @FXML
    protected void handleUpdate() {

        widget.setTop(Integer.parseInt(top.getText()));

        widget.setCategoryField(category.getSelectionModel().getSelectedItem().getValue());
        widget.setOrientation(orientation.getSelectionModel().getSelectedItem());

        super.handleUpdate();
    }

    public void setWidget(DashboardWidget widget) {

        super.setWidget(widget);

        if (widget.getTop() != 0) {
            top.setText(String.valueOf(widget.getTop()));
        }

        ObservableList<KeyStringValue> items = category.getItems();
        for (KeyStringValue val : items) {

            String itemValue = val.getValue();

            if (itemValue.equalsIgnoreCase(widget.getCategoryField())) {
                category.setValue(val);
            }
        }

        if (widget.getOrientation() != null && orientation != null) {
            orientation.setValue(widget.getOrientation());
        }
    }
}
