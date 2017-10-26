package io.haskins.java.cloudtrailviewer.controller.components.elblogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.*;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElbLogToolBarController extends ToolBarController {

    private final ElbLogService elbLogService;
    private final EventTableService eventTableService;
    private final DashboardService dashboardService;

    @Autowired
    public ElbLogToolBarController(ElbLogService elbLogService1, EventTableService eventTableService, DashboardService dashboardService1) {

        this.elbLogService = elbLogService1;
        this.eventTableService = eventTableService;
        this.dashboardService = dashboardService1;
    }

    @FXML private void doLocal() {
        LoadLogsRequest request = openDialog();

        if (request != null && !request.getFilenames().isEmpty()) {
            elbLogService .processRecords(request.getFilenames());
        }
    }

    @FXML private void doChartPie() {
        DashboardWidget newWidget = new DashboardWidget("elblogs","ChartPie");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.elbLogService);
    }

    @FXML private void doChartBar() {
        DashboardWidget newWidget = new DashboardWidget("elblogs","ChartBar");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.elbLogService);
    }

//    @FXML private void doChartStacked() {
//        DashboardWidget newWidget = new DashboardWidget("elblogs","ChartBarStacked");
//
//        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
//        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;
//
//        dashboardService.addWidgetToDashboard(newWidget, this.elbLogService);
//    }

    @FXML private void allEvents() {
        this.eventTableService.setTableEvents(elbLogService.getAllLogs());
    }

}
