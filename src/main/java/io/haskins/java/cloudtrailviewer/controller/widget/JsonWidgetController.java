package io.haskins.java.cloudtrailviewer.controller.widget;

import com.sun.javafx.event.EventUtil;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

/**
 * Created by markhaskins on 07/02/2017.
 */
public class JsonWidgetController extends AbstractBaseController {

    @FXML
    private TextArea textArea;

    public BorderPane loadFXML() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/widget/JsonWidget.fxml"));
        loader.setController(this);
        try {
            fxmlObject = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fxmlObject;
    }

    @Override
    public void configure(DashboardWidget widget, EventTableService eventTableService, DatabaseService databaseService) {

        super.configure(widget, eventTableService, databaseService);

        widgetControlsController.hideEditButton();
    }

    @Override
    public void finishedLoading(boolean reload) {

        Event event = (Event)widget.getPayload();

        if (event.getRawJSON() == null) {
            EventUtils.addRawJson(event);
        }

        textArea.setText(event.getRawJSON());
    }

    @Override
    public void newEvents(List<Event> events) {

    }

    @Override
    public void loadingFile(int fileNum, int totalFiles) {

    }
}
