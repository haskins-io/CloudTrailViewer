package io.haskins.java.cloudtrailviewer.controller.components;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.dialog.filechooser.FileChooserController;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.AccountService;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class ToolBarController {

    @FXML private Button btnS3;
    @FXML private Button btnLocal;

    @FXML private Button btnAllEvents;

    @FXML private Button btnMap;
    @FXML private Button btnTable;

    @FXML private Button btnChartPie;
    @FXML private Button btnChartBar;

    protected AccountService accountDao;
    protected AwsService awsService;

    protected abstract String getBucketName();

    @FXML
    public void initialize() {

        btnLocal.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN));
        btnLocal.setTooltip(new Tooltip("Load Local Files"));

        btnS3.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.CLOUD_DOWNLOAD));
        btnS3.setTooltip(new Tooltip("Load Files from S3"));

        btnAllEvents.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.ARCHIVE));
        btnAllEvents.setTooltip(new Tooltip("View all Events"));

        btnMap.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.MAP_MARKER));
        btnMap.setTooltip(new Tooltip("Add Map"));

        btnTable.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TABLE));
        btnTable.setTooltip(new Tooltip("Add Table"));

        btnChartPie.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PIE_CHART));
        btnChartPie.setTooltip(new Tooltip("Add Pie Chart"));

        btnChartBar.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART));
        btnChartBar.setTooltip(new Tooltip("Add Bar Chart"));

    }

    protected LoadLogsRequest showFileChooser(boolean localFiles) {
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
                controller.init(dialogStage, null, null, getBucketName());
            } else {
                controller.init(dialogStage, accountDao, awsService, getBucketName());
            }

            dialogStage.showAndWait();

            return controller.getSelectedItems();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
