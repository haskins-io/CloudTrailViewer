package io.haskins.java.cloudtrailviewer.controller.widget.dialog;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import javafx.fxml.FXML;

/**
 * Created by markhaskins on 25/01/2017.
 */
public class NewWidgetDialogController extends AbstractDialogController {

    private static final String WIDGET_MAP = "Map";
    private static final String WIDGET_PIE_CHART = "PieChart";
    private static final String WIDGET_STACKED_BAR_CHART = "StackedBarChart";
    private static final String WIDGET_TABLE = "Table";

    @FXML
    protected void handleUpdate() {

        action = DialogAction.ACTION_OK;
        super.handleUpdate();
    }

    @FXML
    private void MapSelected() {
        widget.setWidget(WIDGET_MAP);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void PieChartSelected() {
        widget.setWidget(WIDGET_PIE_CHART);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML private void StackedBarChartSelected() {
        widget.setWidget(WIDGET_STACKED_BAR_CHART);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void TableSelected() {
        widget.setWidget(WIDGET_TABLE);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    public void setWidget(DashboardWidget widget) {
        this.widget = widget;
    }
}
