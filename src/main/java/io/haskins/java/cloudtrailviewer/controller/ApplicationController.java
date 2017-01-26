package io.haskins.java.cloudtrailviewer.controller;

import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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

    public Scene getScene() {
        return this.root.getScene();
    }
}
