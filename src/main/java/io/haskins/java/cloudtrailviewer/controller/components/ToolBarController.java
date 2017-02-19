package io.haskins.java.cloudtrailviewer.controller.components;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.dialog.filechooser.FileChooserController;
import io.haskins.java.cloudtrailviewer.controller.dialog.widget.NewWidgetDialogController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.EventService;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by markhaskins on 19/02/2017.
 */
@Component
public class ToolBarController {

    @FXML private Button btnLocal;
    @FXML private Button btnS3;

    @FXML private Button btnMap;
    @FXML private Button btnTable;

    @FXML private Button btnChartPie;
    @FXML private Button btnChartBar;

    @FXML private Button btnError;
    @FXML private Button btnResource;
    @FXML private Button btnSecurity;


    private final EventService eventService;
    private final AccountService accountDao;
    private final DashboardService dashboardService;


    @Autowired
    public ToolBarController(DashboardService dashboardService, EventService eventService, AccountService accountDao) {
        this.dashboardService = dashboardService;
        this.eventService = eventService;
        this.accountDao = accountDao;
    }

    @FXML
    public void initialize() {

        btnLocal.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN));
        btnS3.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.CLOUD_DOWNLOAD));

        btnMap.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.MAP_MARKER));
        btnTable.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TABLE));

        btnChartPie.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PIE_CHART));
        btnChartBar.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART));

        btnError.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE));
        btnResource.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SERVER));
        btnSecurity.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SHIELD));
    }


    @FXML private void doLocal() {
        handleRequest(showFileChooser(true), EventService.FILE_TYPE_LOCAL);
    }

    @FXML private void doS3() {
        handleRequest(showFileChooser(false), EventService.FILE_TYPE_S3);
    }

    @FXML private void doMap() {

        dashboardService.addWidgetToDashboard(new DashboardWidget("Map"));
    }

    @FXML private void doTable() {
        DashboardWidget newWidget = new DashboardWidget("Table");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget);
    }

    @FXML private void doChartPie() {
        DashboardWidget newWidget = new DashboardWidget("ChartPie");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget);
    }

    @FXML private void doChartBar() {
        DashboardWidget newWidget = new DashboardWidget("ChartBar");

        DialogAction configureWidgetAction = WidgetUtils.showWidgetDialog(newWidget, false);
        if (configureWidgetAction.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard(newWidget);
    }

    @FXML private void doError() {
        dashboardService.addWidgetToDashboard(new DashboardWidget("TableError"));
    }

    @FXML private void doResource() {
        dashboardService.addWidgetToDashboard(new DashboardWidget("TableResources"));
    }

    @FXML private void doSecurity() {
        dashboardService.addWidgetToDashboard(new DashboardWidget("TableSecurity"));
    }

    private void handleRequest(LoadLogsRequest request, int requestType) {

        if (request != null && !request.getFilenames().isEmpty()) {
            eventService.loadFiles(request.getFilenames(), request.getFilter(), requestType);
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
                controller.init(dialogStage, null);
            } else {
                controller.init(dialogStage, accountDao);
            }

            dialogStage.showAndWait();

            return controller.getSelectedItems();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
