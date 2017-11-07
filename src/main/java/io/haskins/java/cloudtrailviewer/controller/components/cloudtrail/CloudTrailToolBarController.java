package io.haskins.java.cloudtrailviewer.controller.components.cloudtrail;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controller for ToolBar
 *
 * Created by markhaskins on 19/02/2017.
 */
@Component
public class CloudTrailToolBarController extends ToolBarController {

    @FXML private Button btnError;
    @FXML private Button btnResource;
    @FXML private Button btnSecurity;

    private final EventService eventService;
    private final DashboardService dashboardService;
    private final EventTableService eventTableService;

    @Autowired
    public CloudTrailToolBarController(
            DashboardService dashboardService, EventService eventService,
            AccountService accountDao1, EventTableService eventTableService,
            AwsService awsService1) {

        this.dashboardService = dashboardService;
        this.eventService = eventService;
        this.accountDao = accountDao1;
        this.eventTableService = eventTableService;
        this.awsService = awsService1;
    }

    public String getBucketName() {

        String bucket = "";

        List<AwsAccount> accounts = accountDao.getAllAccountsWithBucket();
        for (AwsAccount account : accounts) {
            bucket = account.getCtBucket();
        }

        return bucket;
    }


    @FXML
    @Override
    public void initialize() {

        super.initialize();

        btnError.setTooltip(new Tooltip("Add Error Widget"));
        btnError.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE));

        btnResource.setTooltip(new Tooltip("Add Resource Widget"));
        btnResource.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SERVER));

        btnSecurity.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SHIELD));
        btnSecurity.setTooltip(new Tooltip("Add Security Widget"));
    }

    @FXML private void doLocal() {
        handleRequest(showFileChooser(true), EventService.FILE_LOCATION_LOCAL);
    }

    @FXML private void doS3() {
        handleRequest(showFileChooser(false), EventService.FILE_LOCATION_S3);
    }

    @FXML private void doMap() {

        DashboardWidget newWidget = new DashboardWidget("cloudtrail","Map");
        newWidget.setSeriesField("srcCity");
        newWidget.setWidth(700);
        newWidget.setHeight(327);

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doTable() {
        DashboardWidget newWidget = new DashboardWidget("cloudtrail","Table");
        newWidget.setWidth(335);
        newWidget.setHeight(327);

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doChartPie() {
        DashboardWidget newWidget = new DashboardWidget("cloudtrail","ChartPie");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doChartBar() {
        DashboardWidget newWidget = new DashboardWidget("cloudtrail","ChartBar");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doError() {
        DashboardWidget newWidget = new DashboardWidget("cloudtrail","TableError");

        configureFixedWidgets(newWidget);

        newWidget.setTitle("Errors");
        newWidget.setSeriesField("errorCode");

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doResource() {

        DashboardWidget newWidget = new DashboardWidget("cloudtrail","TableResources");

        configureFixedWidgets(newWidget);

        newWidget.setTitle("Resources");
        newWidget.setSeriesField("eventName");

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doSecurity() {

        DashboardWidget newWidget = new DashboardWidget("cloudtrail","TableSecurity");

        configureFixedWidgets(newWidget);

        newWidget.setTitle("Security");
        newWidget.setSeriesField("eventName");

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void allEvents() {
        this.eventTableService.setTableEvents(LuceneUtils.getAllDocuments(Event.TYPE), Event.TYPE);
    }

    private void handleRequest(LoadLogsRequest request, int requestType) {

        if (request != null && !request.getFilenames().isEmpty()) {
            eventService.processRecords(request.getFilenames(), request.getFilter(), requestType);
        }
    }

    private void configureFixedWidgets(DashboardWidget widget) {

        widget.setChartType(AbstractBaseController.WIDGET_TYPE_ALL);
        widget.setTop(-1);

        widget.setWidth(335);
        widget.setHeight(327);
    }
}
