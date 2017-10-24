package io.haskins.java.cloudtrailviewer.controller.components.elblogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.controller.components.ToolBarController;
import io.haskins.java.cloudtrailviewer.model.LoadLogsRequest;
import io.haskins.java.cloudtrailviewer.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElbLogToolBarController extends ToolBarController {

    @FXML private Button btnLocal;

    @FXML private Button btnAllEvents;

    private final ElbLogService elbLogService;
    private final EventTableService eventTableService;


    @Autowired
    public ElbLogToolBarController(ElbLogService elbLogService1, EventTableService eventTableService) {

        this.elbLogService = elbLogService1;
        this.eventTableService = eventTableService;
    }

    @FXML
    public void initialize() {

        btnLocal.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN));
        btnLocal.setTooltip(new Tooltip("Load Local Files"));


        btnAllEvents.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.ARCHIVE));
        btnAllEvents.setTooltip(new Tooltip("View all Events"));

    }

    @FXML private void doLocal() {
        LoadLogsRequest request = openDialog();

        if (request != null && !request.getFilenames().isEmpty()) {
            elbLogService .processRecords(request.getFilenames());
        }
    }

    @FXML private void allEvents() {
        this.eventTableService.setTableEvents(elbLogService.getAllLogs());
    }

}
