package io.haskins.java.cloudtrailviewer.controller.widget.dialog;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Created by markhaskins on 25/01/2017.
 */
public class StackedBarChartDialogController extends AbstractDialogController {

    @FXML private TextField top;
    @FXML private ChoiceBox<String> category;
    @FXML private ChoiceBox<String> orientation;

    @FXML
    protected void handleUpdate() {

        widget.setTop(Integer.parseInt(top.getText()));

        widget.setCategoryField(category.getSelectionModel().getSelectedItem());
        widget.setOrientation(orientation.getSelectionModel().getSelectedItem());

        super.handleUpdate();
    }

    public void setWidget(DashboardWidget widget) {

        super.setWidget(widget);

        if (widget.getTop() != 0) {
            top.setText(String.valueOf(widget.getTop()));
        }

        if (widget.getCategoryField() != null && category != null) {
            category.setValue(widget.getCategoryField());
        }

        if (widget.getOrientation() != null && orientation != null) {
            orientation.setValue(widget.getOrientation());
        }
    }
}
