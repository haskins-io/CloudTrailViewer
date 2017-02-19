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

import io.haskins.java.cloudtrailviewer.controller.dialog.widget.NewWidgetDialogController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.utils.DialogUtils;
import io.haskins.java.cloudtrailviewer.utils.WidgetUtils;
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller that handles the Dashboard Menu
 *
 * Created by markhaskins on 06/01/2017.
 */
@Component
public class DashboardMenuController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardMenuController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @FXML
    private void newDashboard() {

        Optional<String> result = DialogUtils.showTextInputDialog("New Dashboard", "Please enter name for the dashboard");

        result.ifPresent(dashboardService::newDashboard);
    }

    @FXML
    private void saveDashboard() {

        dashboardService.saveDashboard();

        DialogUtils.showAlertDialog("CloudTrail Viewer", "Saving Dashboard","Dashboard has been saved.", Alert.AlertType.CONFIRMATION);
    }

    @FXML
    private void openDashboard() {

        File dir = new File(FileUtils.getApplicationDirectory());
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".ctd"));

        if (files != null && files.length > 0) {

            String currentDashboard = dashboardService.getCurrentDashboardName();

            List<String> dashboards = new ArrayList<>();

            for (File f : files) {

                String dashboardName = FileUtils.removeExtension(f.getName());
                if (!dashboardName.equalsIgnoreCase(currentDashboard)) {
                    dashboards.add(dashboardName);
                }
            }

            if (dashboards.isEmpty()) {

                DialogUtils.showAlertDialog("CloudTrail Viewer", "Opening Dashboard" ,"No other Dashboards are available", Alert.AlertType.WARNING);

            } else {

                Optional<String> result = DialogUtils.showChoiceDialog("Open Dashboard", "Select Dashboard to Open", dashboards);
                result.ifPresent(dashboardService::openDashboard);
            }
        }
    }

}
