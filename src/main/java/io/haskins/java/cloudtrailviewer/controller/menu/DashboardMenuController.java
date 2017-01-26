package io.haskins.java.cloudtrailviewer.controller.menu;

import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.DialogAction;
import io.haskins.java.cloudtrailviewer.service.DashboardService;
import io.haskins.java.cloudtrailviewer.utils.DialogUtils;
import io.haskins.java.cloudtrailviewer.utils.FXMLUtils;
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;
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

        if (result.isPresent()) {
            dashboardService.newDashboard(result.get());
        }
    }

    @FXML
    private void saveDashboard() {

        dashboardService.saveDashboard();

        DialogUtils.showAlertDialog("Save Dashboard", "Dashboard has been saved.");
    }

    @FXML
    private void openDashboard() {

        File dir = new File(FileUtils.getApplicationDirectory());
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ctd");
            }
        });

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

                DialogUtils.showAlertDialog("Open Dashboard", "No other Dashboards are available");

            } else {

                Optional<String> result = DialogUtils.showChoiceDialog("Open Dashboard", "Select Dashboard to Open", dashboards);
                if (result.isPresent()) {

                    dashboardService.openDashboard(result.get());
                }
            }
        }
    }

    @FXML
    private void addWidget() {

        DialogAction addWidget = FXMLUtils.showDialog(new DashboardWidget(), false);
        if (addWidget.getActionCode() == DialogAction.ACTION_CANCEL) return;

        DialogAction newWidget = FXMLUtils.showDialog((DashboardWidget)addWidget.getActionPayload(), false);
        if (newWidget.getActionCode() == DialogAction.ACTION_CANCEL) return;

        dashboardService.addWidgetToDashboard((DashboardWidget)newWidget.getActionPayload());
    }
}
