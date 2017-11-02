package io.haskins.java.cloudtrailviewer.controller.components.vpclogs;

import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import io.haskins.java.cloudtrailviewer.service.*;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            vpcFlowLogService.processRecords(request.getFilenames(),null, EventService.FILE_LOCATION_LOCAL);
        }
    }

    @FXML private void doMap() {

        DashboardWidget newWidget = new DashboardWidget("vpclogs","Map");
        newWidget.setSeriesField("City");
        newWidget.setWidth(700);
        newWidget.setHeight(327);

        dashboardService.addWidgetToDashboard(newWidget, this.vpcFlowLogService);
    }

    @FXML private void doTable() {
        DashboardWidget newWidget = new DashboardWidget("vpclogs","Table");
        newWidget.setWidth(335);
        newWidget.setHeight(327);

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.vpcFlowLogService);
    }

    @FXML private void doChartPie() {
        DashboardWidget newWidget = new DashboardWidget("vpclogs","ChartPie");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.vpcFlowLogService);
    }

    @FXML private void doChartBar() {
        DashboardWidget newWidget = new DashboardWidget("vpclogs","ChartBar");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.vpcFlowLogService);
    }

    @FXML private void allEvents() {
        this.eventTableService.setTableEvents(LuceneUtils.getAllDocuments(VpcFlowLog.TYPE), VpcFlowLog.TYPE);
    }
}
