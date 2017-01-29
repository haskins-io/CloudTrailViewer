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
import io.haskins.java.cloudtrailviewer.controller.ApplicationController;
import io.haskins.java.cloudtrailviewer.controller.dialog.filechooser.FileChooserController;
import io.haskins.java.cloudtrailviewer.filter.AllFilter;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.service.AccountDao;
import io.haskins.java.cloudtrailviewer.service.EventService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles the Event menu
 *
 * Created by markhaskins on 06/01/2017.
 */
@Component
public class EventMenuController {

    private final FileChooser fileChooser = new FileChooser();

    private final ApplicationController applicationController;
    private final EventService eventService;
    private final AccountDao accountDao;

    @Autowired
    public EventMenuController(EventService eventService, ApplicationController appController, AccountDao accountDao) {
        this.eventService = eventService;
        this.applicationController = appController;
        this.accountDao = accountDao;
    }

    @FXML
    private void loadLocalEvents() {

        List<String> selectedItems = showFileChooser(true);
        if (selectedItems != null && !selectedItems.isEmpty()) {

            CompositeFilter filters = new CompositeFilter();
            filters.addFilter(new AllFilter());

            eventService.loadFiles(selectedItems, filters, EventService.FILE_TYPE_LOCAL);
        }
    }

    @FXML
    private void loadS3Events() {

        List<String> selectedItems = showFileChooser(false);
        if (selectedItems != null && !selectedItems.isEmpty()) {

            CompositeFilter filters = new CompositeFilter();
            filters.addFilter(new AllFilter());

            eventService.loadFiles(selectedItems, filters, EventService.FILE_TYPE_S3);
        }
    }

    @FXML
    private void clearEvents() {
        eventService.clearEvents();
    }


    private List<String> showFileChooser(boolean localFiles) {

        try {

            String fxmlFile = "/fxml/dialog/filechooser/FileChooser.fxml";

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CloudTrailViewer.class.getResource(fxmlFile));
            Pane page = loader.load();

            Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("/style/filechooser.css").toExternalForm());

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
