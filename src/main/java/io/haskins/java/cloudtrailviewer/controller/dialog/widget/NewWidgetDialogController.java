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

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Arrays;
import java.util.List;

/**
 * Dialog that offers the different types of Widget that can be added to a dashboard
 *
 * Created by markhaskins on 25/01/2017.
 */
public class NewWidgetDialogController extends AbstractDialogController {

    private static final String WIDGET_CHART_BAR = "ChartBar";
    private static final String WIDGET_CHART_PIE = "ChartPie";
    private static final String WIDGET_CHART_BAR_STACKED = "ChartBarStacked";
    private static final String WIDGET_CHART_LINE = "ChartLine";

    private static final String WIDGET_MAP = "Map";
    private static final String WIDGET_TABLE = "Table";

    private static final String WIDGET_TABLE_ERROR = "TableError";
    private static final String WIDGET_TABLE_RESOURCES = "TableResources";
    private static final String WIDGET_TABLE_SECURITY = "TableSecurity";

    public static final List<String> configureLessWidgets = Arrays.asList(WIDGET_TABLE_ERROR, WIDGET_TABLE_RESOURCES, WIDGET_TABLE_SECURITY);

    @FXML private Button btnMap;
    @FXML private Button btnTable;
    @FXML private Button btnPieChart;
    @FXML private Button btnBarChart;
    @FXML private Button btnSBarChart;
    @FXML private Button btnLineChart;
    @FXML private Button btnErros;
    @FXML private Button btnResources;
    @FXML private Button btnSecurity;

    @FXML
    public void initialize() {
        btnMap.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.MAP_MARKER));
        btnTable.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TABLE));
        btnPieChart.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PIE_CHART));
        btnBarChart.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART));
        btnSBarChart.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART_ALT));
        btnLineChart.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.LINE_CHART));
        btnErros.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE));
        btnResources.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SERVER));
        btnSecurity.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SHIELD));
    }

    @FXML
    protected void handleUpdate() {

        action = DialogAction.ACTION_OK;
        super.handleUpdate();
    }

    @FXML
    private void PieChartSelected() {
        widget.setWidget(WIDGET_CHART_PIE);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void BarChartSelected() {
        widget.setWidget(WIDGET_CHART_BAR);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void StackedBarChartSelected() {
        widget.setWidget(WIDGET_CHART_BAR_STACKED);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void LineChartSelected() {
        widget.setWidget(WIDGET_CHART_LINE);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }


    @FXML
    private void MapSelected() {
        widget.setWidget(WIDGET_MAP);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void TableSelected() {
        widget.setWidget(WIDGET_TABLE);
        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void ErrorsSelected() {

        configureFixedWidgets();

        widget.setWidget(WIDGET_TABLE_ERROR);
        widget.setTitle("Errors");
        widget.setSeriesField("ErrorCode");

        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void ResourcesSelected() {

        configureFixedWidgets();

        widget.setWidget(WIDGET_TABLE_RESOURCES);
        widget.setTitle("Resources");

        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    @FXML
    private void SecuritySelected() {

        configureFixedWidgets();

        widget.setWidget(WIDGET_TABLE_SECURITY);
        widget.setTitle("Security");

        action = DialogAction.ACTION_OK;
        dialogStage.close();
    }

    private void configureFixedWidgets() {

        widget.setType(AbstractBaseController.WIDGET_TYPE_ALL);
        widget.setTop(-1);
        widget.setSeriesField("EventName");

        widget.setWidth(335);
        widget.setHeight(327);
    }

    public void setWidget(DashboardWidget widget) {
        this.widget = widget;
    }
}
