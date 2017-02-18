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

package io.haskins.java.cloudtrailviewer.controller;

import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFx controller that handles fxml/main.fxml.
 *
 * This is the entry point into the application and is responsible for perform actions that need to be performed at
 * startup.
 *
 * It also handles events on the Menu and Tool bars.
 *
 * Created by markhaskins on 03/01/2017.
 */
@Component
public class ApplicationController implements Initializable {

    @FXML private Pane widgets;
    @FXML private Node root;

    private final DatabaseService databaseService;
    private final DashboardService dashboardService;

    @Autowired
    public ApplicationController(DatabaseService databaseService, DashboardService dashboardService) {
        this.databaseService = databaseService;
        this.dashboardService = dashboardService;
    }

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        databaseService.sync();

        dashboardService.init(widgets);
        dashboardService.loadDashboard("default");
    }

}
