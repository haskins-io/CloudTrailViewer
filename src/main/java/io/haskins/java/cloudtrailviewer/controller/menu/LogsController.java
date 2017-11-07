package io.haskins.java.cloudtrailviewer.controller.menu;

import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.dialog.filechooser.FileChooserController;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class LogsController {

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

            controller.init(dialogStage, null, null, "");

            dialogStage.showAndWait();

            return controller.getSelectedItems();

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }

    }
}
