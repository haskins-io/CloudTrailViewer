package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import io.haskins.java.cloudtrailviewer.model.observable.KeyIntegerValue;
import io.haskins.java.cloudtrailviewer.model.observable.S3ListModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhaskins on 27/01/2017.
 */
abstract class FileHandler {

    static final String MOVE_BACK = "..";
    static final String FILE_EXTENSION = "gz";

    ListView<S3ListModel> listView;
    final ObservableList<S3ListModel> data = FXCollections.observableArrayList();

    protected final List<String> selected_keys = new ArrayList<>();

    boolean scanning = false;

    FileListControllerListener fileListControllerListener;

    abstract void handleDoubleClickEvent();

    public abstract List<String> getSelectedItems();


    void setUpMouseListener() {

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    handleDoubleClickEvent();

                } else if (e.getClickCount() == 1) {

                    Object obj = listView.getSelectionModel().getSelectedItem().getPath();
                    if (obj instanceof  File) {

                        File selected = (File)obj;
                        if (selected.isDirectory()) {
                            fileListControllerListener.listItemSelected(false);
                        } else {
                            fileListControllerListener.listItemSelected(true);
                        }

                    } else {

                        String selected = (String)obj;
                        if (selected.contains("/")) {
                            fileListControllerListener.listItemSelected(false);
                        }
                        else {
                            fileListControllerListener.listItemSelected(true);
                        }
                    }

                }
            }
        });
    }
}
