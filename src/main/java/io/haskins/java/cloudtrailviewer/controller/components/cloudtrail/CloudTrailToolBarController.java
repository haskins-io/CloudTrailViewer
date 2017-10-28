package io.haskins.java.cloudtrailviewer.controller.components.cloudtrail;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.controller.dialog.filechooser.FileChooserController;
import io.haskins.java.cloudtrailviewer.controller.widget.AbstractBaseController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Controller for ToolBar
 *
 * Created by markhaskins on 19/02/2017.
 */
@Component
public class CloudTrailToolBarController extends ToolBarController {

    @FXML private Button btnS3;

    @FXML private Button btnChartStacked;

    @FXML private Button btnError;
    @FXML private Button btnResource;
    @FXML private Button btnSecurity;


    private final EventService eventService;
    private final AccountService accountDao;
    private final DashboardService dashboardService;
    private final EventTableService eventTableService;
    private final AwsService awsService;


    @Autowired
    public CloudTrailToolBarController(
            DashboardService dashboardService, EventService eventService,
            AccountService accountDao, EventTableService eventTableService,
            AwsService awsService) {

        this.dashboardService = dashboardService;
        this.eventService = eventService;
        this.accountDao = accountDao;
        this.eventTableService = eventTableService;
        this.awsService = awsService;
    }

    @FXML
    @Override
    public void initialize() {

        super.initialize();

        btnS3.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.CLOUD_DOWNLOAD));
        btnS3.setTooltip(new Tooltip("Load Files from S3"));

        btnChartStacked.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART));
        btnChartStacked.setTooltip(new Tooltip("Add Stacked Bar Chart"));

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
        newWidget.setSeriesField("City");
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

    @FXML private void doChartStacked() {
        DashboardWidget newWidget = new DashboardWidget("cloudtrail","ChartBarStacked");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doError() {
        DashboardWidget newWidget = new DashboardWidget("cloudtrail","TableError");

        configureFixedWidgets(newWidget);

        newWidget.setTitle("Errors");
        newWidget.setSeriesField("ErrorCode");

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void doResource() {

        DashboardWidget newWidget = new DashboardWidget("cloudtrail","TableResources");

        configureFixedWidgets(newWidget);

        newWidget.setTitle("Resources");
        newWidget.setSeriesField("EventName");

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);

    }

    @FXML private void doSecurity() {

        DashboardWidget newWidget = new DashboardWidget("cloudtrail","TableSecurity");

        configureFixedWidgets(newWidget);

        newWidget.setTitle("Security");
        newWidget.setSeriesField("EventName");

        dashboardService.addWidgetToDashboard(newWidget, this.eventService);
    }

    @FXML private void allEvents() {
//        this.eventTableService.setTableEvents(eventService.getAllEvents());
    }

    private void handleRequest(LoadLogsRequest request, int requestType) {

        if (request != null && !request.getFilenames().isEmpty()) {
            eventService.processRecords(request.getFilenames(), request.getFilter(), requestType);
        }
    }

    private LoadLogsRequest showFileChooser(boolean localFiles) {

        try {

            String fxmlFile = "/fxml/dialog/filechooser/FileChooser.fxml";

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CloudTrailViewer.class.getResource(fxmlFile));
            Pane page = loader.load();

            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/style/fileChooser.css").toExternalForm());

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(scene);

            FileChooserController controller = loader.getController();

            if (localFiles) {
                controller.init(dialogStage, null, null);
            } else {
                controller.init(dialogStage, accountDao, awsService);
            }

            dialogStage.showAndWait();

            return controller.getSelectedItems();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private void configureFixedWidgets(DashboardWidget widget) {

        widget.setChartType(AbstractBaseController.WIDGET_TYPE_ALL);
        widget.setTop(-1);

        widget.setWidth(335);
        widget.setHeight(327);
    }
}
