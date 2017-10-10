package io.haskins.java.cloudtrailviewer.controller.menu;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.VpcFlowLogService;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
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
        }

    }

    @FXML
    public void showTable() {

        DashboardWidget newWidget = new DashboardWidget("vpclogs","Table");

//        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
//        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, vpcFlowLogService);
    }

}