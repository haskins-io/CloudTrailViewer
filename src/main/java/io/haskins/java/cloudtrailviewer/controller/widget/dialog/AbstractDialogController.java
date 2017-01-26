package io.haskins.java.cloudtrailviewer.controller.widget.dialog;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.KeyStringValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Abstract base that that provides common Dialog controller functionality
 *
 * Created by markhaskins on 06/01/2017.
 */
public abstract class AbstractDialogController {

    @FXML protected Button updateButton;

    @FXML protected TextField title;
    @FXML protected ChoiceBox<String> type;
    @FXML protected ChoiceBox<KeyStringValue> series;

    int action = DialogAction.ACTION_CANCEL;

    Stage dialogStage;
    DashboardWidget widget;

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
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

        if (widget.getType() != null && type != null) {
            type.setValue(widget.getType());
        }

        if (widget.getSeriesField() != null) {
            series.setValue(new KeyStringValue(widget.getSeriesField(), ""));
        }
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public DialogAction getAction() {
        return new DialogAction(action, widget);
    }

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
