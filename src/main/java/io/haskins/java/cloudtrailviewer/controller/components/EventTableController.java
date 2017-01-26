package io.haskins.java.cloudtrailviewer.controller.components;

import io.haskins.java.cloudtrailviewer.model.EventTableModel;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.EventTableService;
import io.haskins.java.cloudtrailviewer.service.listener.EventTableServiceListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhaskins on 26/01/2017.
 */
@Component
public class EventTableController implements EventTableServiceListener {

    @FXML private TableView tableView;

    @Autowired
    public EventTableController(EventTableService eventTableService) {
        eventTableService.addListener(this);
    }

    public void setEvents(List<Event> events) {

        tableView.getItems().clear();

        ObservableList<EventTableModel> data = tableView.getItems();

        List<EventTableModel> items = new ArrayList<>();

        for (Event event : events) {
            items.add(new EventTableModel(event));
        }

        data.addAll(items);
    }
}
