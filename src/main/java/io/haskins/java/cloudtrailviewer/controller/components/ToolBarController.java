package io.haskins.java.cloudtrailviewer.controller.components;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.dialog.filechooser.FileChooserController;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
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

    @FXML private Button btnLocal;
    @FXML private Button btnAllEvents;
    @FXML private Button btnChartPie;
    @FXML private Button btnChartBar;
//    @FXML private Button btnChartStacked;


    @FXML
    public void initialize() {

        btnLocal.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN));
        btnLocal.setTooltip(new Tooltip("Load Local Files"));

        btnAllEvents.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.ARCHIVE));
        btnAllEvents.setTooltip(new Tooltip("View all Events"));

        btnChartPie.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.PIE_CHART));
        btnChartPie.setTooltip(new Tooltip("Add Pie Chart"));

        btnChartBar.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART));
        btnChartBar.setTooltip(new Tooltip("Add Bar Chart"));

//        btnChartStacked.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.BAR_CHART));
//        btnChartStacked.setTooltip(new Tooltip("Add Stacked Bar Chart"));
    }

    protected LoadLogsRequest openDialog() {

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

            controller.init(dialogStage, null, null);

            dialogStage.showAndWait();

            return controller.getSelectedItems();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

    }
}
