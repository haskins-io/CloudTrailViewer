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
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.observable.KeyStringValue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Abstract class that that provides common Dialog controller functionality
 *
 * Created by markhaskins on 06/01/2017.
 */
public abstract class AbstractDialogController {

    private final static int TYPE_TOP = 1;

    @FXML protected Button updateButton;

    @FXML protected TextField title;
    @FXML protected ChoiceBox<String> type;
    @FXML protected TextField top;
    @FXML protected ChoiceBox<KeyStringValue> series;

    @FXML private BorderPane topPanel;

    int action = DialogAction.ACTION_CANCEL;

    Stage dialogStage;
    DashboardWidget widget;

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage Stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setWidget(DashboardWidget widget) {

        this.widget = widget;

        if (widget.getTitle() != null) {
            title.setText(widget.getTitle());
        } else {
            updateButton.setText("Create");
        }

        type.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.intValue() == TYPE_TOP) {
                topPanel.setVisible(true);

            } else  {
                topPanel.setVisible(false);
            }
        });

        if (widget.getType() != null && type != null) {
            type.setValue(widget.getType());
        }

        ObservableList<KeyStringValue> items = series.getItems();
        for (KeyStringValue val : items) {

            String itemValue = val.getValue();

            if (itemValue.equalsIgnoreCase(widget.getSeriesField())) {
                series.setValue(val);
                break;
            }
        }
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return A DialogAction with the outcome of the dialog
     */
    public DialogAction getAction() {
        return new DialogAction(action, widget);
    }

    /**
     * Updates a DashboardWidget and closes the open Dialog
     */
    protected void handleUpdate() {

        action = DialogAction.ACTION_OK;

        widget.setTitle(title.getText());
        widget.setType(type.getSelectionModel().getSelectedItem());
        widget.setSeriesField(series.getSelectionModel().getSelectedItem().getValue());

        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        action = DialogAction.ACTION_CANCEL;
        dialogStage.close();
    }
}
