package io.haskins.java.cloudtrailviewer.controller.components.vpclogs;

import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.*;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;;

@Component
public class VpcLogToolBarController extends ToolBarController {

    private final VpcFlowLogService vpcFlowLogService;
    private final EventTableService eventTableService;
    private final DashboardService dashboardService;


    @Autowired
    public VpcLogToolBarController(VpcFlowLogService vpcFlowLogService1, EventTableService eventTableService1, DashboardService dashboardService1) {

        this.vpcFlowLogService = vpcFlowLogService1;
        this.eventTableService = eventTableService1;
        this.dashboardService = dashboardService1;
    }


    @FXML private void doLocal() {
        LoadLogsRequest request = openDialog();

        if (request != null && !request.getFilenames().isEmpty()) {
            vpcFlowLogService.processRecords(request.getFilenames());
        }
    }

    @FXML private void doChartPie() {
        DashboardWidget newWidget = new DashboardWidget("vpclogs","ChartPie");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.vpcFlowLogService);
    }

    @FXML private void allEvents() {
        this.eventTableService.setTableEvents(vpcFlowLogService.getAllLogs());
    }
}
