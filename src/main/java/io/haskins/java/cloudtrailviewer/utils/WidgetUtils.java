/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.utils;

import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.dialog.widget.AbstractDialogController;
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
public class WidgetUtils {

    private static final String EDIT_WIDGET_PACKAGE = "/fxml/dialog/widget/";

    private static String getEditWidgetFXML(DashboardWidget widget) {

        if (widget.getWidget() == null) {
            return EDIT_WIDGET_PACKAGE + "NewWidgetDialog.fxml";
        } else {
            return EDIT_WIDGET_PACKAGE + widget.getWidget() + "Dialog.fxml";
        }
    }

    public static DialogAction showWidgetDialog(DashboardWidget widget, boolean update) {

        DialogAction action;

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            String fxmlFile = WidgetUtils.getEditWidgetFXML(widget);

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
