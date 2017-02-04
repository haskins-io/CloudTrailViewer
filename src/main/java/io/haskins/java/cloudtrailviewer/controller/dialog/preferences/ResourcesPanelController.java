package io.haskins.java.cloudtrailviewer.controller.dialog.preferences;

import io.haskins.java.cloudtrailviewer.model.observable.FileListModel;
import io.haskins.java.cloudtrailviewer.model.observable.StringObservable;
import io.haskins.java.cloudtrailviewer.service.DatabaseService;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Created by markhaskins on 04/02/2017.
 */
public class ResourcesPanelController extends PreferencesPanel {

    @FXML private ListView<StringObservable> listView;

    public void init(DatabaseService databaseService) {

        this.databaseService = databaseService;

        configureListView(listView);

        String query = "SELECT api_call FROM aws_resources";
        populateListBySQL(listView, query);
    }
}
