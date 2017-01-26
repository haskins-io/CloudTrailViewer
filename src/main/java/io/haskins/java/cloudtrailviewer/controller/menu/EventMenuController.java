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

import io.haskins.java.cloudtrailviewer.controller.ApplicationController;
import io.haskins.java.cloudtrailviewer.service.EventService;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
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

    @Autowired
    public EventMenuController(EventService eventService, ApplicationController appController) {
        this.eventService = eventService;
        this.applicationController = appController;
    }

    @FXML
    private void loadLocalEvents() {

        List<File> files = fileChooser.showOpenMultipleDialog(applicationController.getScene().getWindow());
        if (files != null) {

            List<String> filenames = new ArrayList<>();

            for (File f: files) {
                filenames.add(f.getAbsolutePath());
            }

            eventService.loadFiles(filenames, null, EventService.FILE_TYPE_LOCAL);
        }
    }

    @FXML
    private void clearEvents() {
        eventService.clearEvents();
    }
}
