package io.haskins.java.cloudtrailviewer.utils;

import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.widget.dialog.AbstractDialogController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by markhaskins on 06/01/2017.
 */
public class FXMLUtils {

    private static final String EDIT_WIDGET_PACKAGE = "/fxml/widget/dialog/";

    private static String getEditWidgetFXML(DashboardWidget widget) {

        if (widget.getWidget() == null) {
            return EDIT_WIDGET_PACKAGE + "NewWidgetDialog.fxml";
        } else {
            return EDIT_WIDGET_PACKAGE + widget.getWidget() + "Dialog.fxml";
        }
    }

    public static DialogAction showDialog(DashboardWidget widget, boolean update) {

        DialogAction action;

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            String fxmlFile = FXMLUtils.getEditWidgetFXML(widget);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CloudTrailViewer.class.getResource(fxmlFile));
            Pane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();

            if (update) {
                dialogStage.setTitle("Update " + widget.getWidget());
            } else {
                dialogStage.setTitle("Create " + widget.getWidget());
            }

            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AbstractDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setWidget(widget);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            action = controller.getAction();

        } catch (IOException e) {
            action = new DialogAction();
            e.printStackTrace();
        }

        return action;
    }
}
