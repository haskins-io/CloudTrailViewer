package io.haskins.java.cloudtrailviewer.controller.components.elblogs;

import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import io.haskins.java.cloudtrailviewer.service.*;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElbLogToolBarController extends ToolBarController {

    private final ElbLogService elbLogService;
    private final EventTableService eventTableService;
    private final DashboardService dashboardService;

    @Autowired
    public ElbLogToolBarController(ElbLogService elbLogService1, EventTableService eventTableService,
                                   DashboardService dashboardService1, AccountService accountDao1,
                                   AwsService awsService1) {

        this.elbLogService = elbLogService1;
        this.eventTableService = eventTableService;
        this.dashboardService = dashboardService1;
        this.awsService = awsService1;
        this.accountDao = accountDao1;
    }

    public String getBucketName() {

        String bucket = "";

        List<AwsAccount> accounts = accountDao.getAllAccountsWithBucket();
        for (AwsAccount account : accounts) {
            bucket = account.getElbBucket();
        }

        return bucket;
    }

    @FXML private void doLocal() {
        LoadLogsRequest request = showFileChooser(true);

        if (request != null && !request.getFilenames().isEmpty()) {
            elbLogService.processRecords(request.getFilenames(), null, EventService.FILE_LOCATION_LOCAL);
        }
    }

    @FXML private void doS3() {
        handleRequest(showFileChooser(false), EventService.FILE_LOCATION_S3);
    }

    @FXML private void doMap() {

        DashboardWidget newWidget = new DashboardWidget("elblogs","Map");
        newWidget.setSeriesField("City");
        newWidget.setWidth(700);
        newWidget.setHeight(327);

        dashboardService.addWidgetToDashboard(newWidget, this.elbLogService);
    }

    @FXML private void doTable() {
        DashboardWidget newWidget = new DashboardWidget("elblogs","Table");
        newWidget.setWidth(335);
        newWidget.setHeight(327);

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.elbLogService);
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


    @FXML private void allEvents() {
        this.eventTableService.setTableEvents(LuceneUtils.getAllDocuments(ElbLog.TYPE), ElbLog.TYPE);
    }

    private void handleRequest(LoadLogsRequest request, int requestType) {

        if (request != null && !request.getFilenames().isEmpty()) {
            elbLogService.processRecords(request.getFilenames(), request.getFilter(), requestType);
        }
    }

}
