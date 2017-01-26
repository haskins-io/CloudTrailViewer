package io.haskins.java.cloudtrailviewer.controller.widget.dialog;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by markhaskins on 25/01/2017.
 */
public class BarChartDialogController extends AbstractDialogController {

    @FXML
    private TextField top;

    @FXML
    protected void handleUpdate() {

        widget.setTop(Integer.parseInt(top.getText()));

        super.handleUpdate();
    }

    public void setWidget(DashboardWidget widget) {

        super.setWidget(widget);

        if (widget.getTop() != 0) {
            top.setText(String.valueOf(widget.getTop()));
        }

    }
}