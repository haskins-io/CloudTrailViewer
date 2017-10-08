package io.haskins.java.cloudtrailviewer.controller.menu;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.VpcFlowLogService;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VpcLogsMenuController extends LogsController {

    private final DashboardService dashboardService;
    private final VpcFlowLogService vpcFlowLogService;

    @Autowired
    public VpcLogsMenuController(DashboardService dashboardService, VpcFlowLogService vpcFlowLogService) {

        this.dashboardService = dashboardService;
        this.vpcFlowLogService = vpcFlowLogService;
    }

    @FXML
    public void flowLogs() {

        LoadLogsRequest request = openDialog();

        if (request != null && !request.getFilenames().isEmpty()) {

            vpcFlowLogService.processRecords(request.getFilenames());

            DashboardWidget newWidget = new DashboardWidget("vpclogs","Table");

            dashboardService.addWidgetToDashboard(newWidget, vpcFlowLogService);
        }

    }

}