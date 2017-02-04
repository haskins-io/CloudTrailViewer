package io.haskins.java.cloudtrailviewer.controller.dialog.preferences;

import io.haskins.java.cloudtrailviewer.model.dao.ResultSetRow;
import io.haskins.java.cloudtrailviewer.model.observable.StringObservable;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.List;

/**
 * Created by markhaskins on 04/02/2017.
 */
public abstract class PreferencesPanel {

    DatabaseService databaseService;

    void populateListBySQL(ListView<StringObservable> listView, String sqlQuery) {

        listView.getItems().clear();

        List<ResultSetRow> rows = databaseService.executeCursorStatement(sqlQuery);
        for (ResultSetRow row : rows) {

            String event_name = (String)row.get("api_call");
            listView.getItems().add(new StringObservable(event_name));
        }
    }

    void configureListView(ListView<StringObservable> listView) {

        listView.setCellFactory(new Callback<ListView<StringObservable>, ListCell<StringObservable>>() {
            @Override
            public ListCell<StringObservable> call(ListView<StringObservable> list) {
                return new ListViewCell();
            }
        });
    }
}

class ListViewCell extends ListCell<StringObservable> {

    @Override
    public void updateItem(StringObservable model, boolean empty) {

        super.updateItem(model, empty);

        if (model != null) {
            setText(model.getValue());
        } else {
            setText(null);
        }
    }
}
