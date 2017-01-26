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

package io.haskins.java.cloudtrailviewer.controller.widget.dialog;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import javafx.fxml.FXML;

/**
 * Dialog that offers the different types of Widget that can be added to a dashboard
 *
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
