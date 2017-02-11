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

package io.haskins.java.cloudtrailviewer.controller.menu;

import io.haskins.java.cloudtrailviewer.CloudTrailViewer;
import io.haskins.java.cloudtrailviewer.controller.components.UserGuideController;
import io.haskins.java.cloudtrailviewer.controller.dialog.preferences.PreferencesDialogController;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.PropertiesService;
import io.haskins.java.cloudtrailviewer.utils.DialogUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Controller that handles the Help menu
 *
 * Created by markhaskins on 06/01/2017.
 */
@Component
public class HelpMenuController {

    private DatabaseService databaseService;
    private PropertiesService propertiesService;

    @Autowired
    public HelpMenuController(DatabaseService databaseService, PropertiesService propertiesService) {
        this.databaseService = databaseService;
        this.propertiesService = propertiesService;
    }

    @FXML
    private void showUserGuide() {

        String userGuide = getHTMLUserGuide();
        if (userGuide != null) {

            try {

                String fxmlFile = "/fxml/components/WebView.fxml";

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CloudTrailViewer.class.getResource(fxmlFile));
                WebView page = loader.load();

                Scene scene = new Scene(page);

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setScene(scene);

                UserGuideController controller = loader.getController();
                controller.setHTML(userGuide);

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void showAbout() {

        String app_version = propertiesService.getProperty("application.version");
        int db_version = databaseService.getCurrentDbVersion();

        StringBuilder message = new StringBuilder();
        message.append("CloudTrailViewer\n");
        message.append("Release : ");
        message.append(app_version);
        message.append(" [DB v").append(db_version).append("]");

        DialogUtils.showAlertDialog("CloudTrail Viewer", "About CloudTrail Viewer",  message.toString(), Alert.AlertType.INFORMATION);
    }

    private String getHTMLUserGuide() {

        StringBuilder result = new StringBuilder();

        try(InputStream stream = CloudTrailViewer.class.getResourceAsStream("/docs/UserGuide.md")) {

            if (stream != null) {

                InputStreamReader io = new InputStreamReader(stream);
                BufferedReader br = new BufferedReader(io);

                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line).append("\n");
                }

                Parser parser = Parser.builder().build();
                Node document = parser.parse(result.toString());
                HtmlRenderer renderer = HtmlRenderer.builder().build();

                return renderer.render(document);

            } else {
                DialogUtils.showAlertDialog("CloudTrail Viewer", "Application Error", "Unable to load User Guide.", Alert.AlertType.ERROR);
                return null;
            }

        } catch (IOException ioe) {

            DialogUtils.showAlertDialog("CloudTrail Viewer", "Application Error", "Unable to load User Guide.", Alert.AlertType.ERROR);
            return null;
        }

    }
}
