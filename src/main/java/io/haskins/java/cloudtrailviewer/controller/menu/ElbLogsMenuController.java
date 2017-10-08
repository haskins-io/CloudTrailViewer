package io.haskins.java.cloudtrailviewer.controller.menu;

import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.ElbLogService;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElbLogsMenuController extends LogsController {

    private final ElbLogService elbLogService;

    @Autowired
    public ElbLogsMenuController(ElbLogService elbLogService) {
        this.elbLogService = elbLogService;
    }

    @FXML
    public void elbLogs() {

        LoadLogsRequest request = openDialog();

        if (request != null && !request.getFilenames().isEmpty()) {
            elbLogService .processRecords(request.getFilenames());
        }
    }

}