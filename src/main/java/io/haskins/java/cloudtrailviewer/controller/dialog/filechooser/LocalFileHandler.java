package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import io.haskins.java.cloudtrailviewer.model.observable.S3ListModel;
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.List;

/**
 * Created by markhaskins on 27/01/2017.
 */
class LocalFileHandler extends FileHandler {

    private String path = FileUtils.getApplicationDirectory();

    LocalFileHandler(ListView<S3ListModel> listView, FileListControllerListener listener) {

        this.listView = listView;
        this.fileListControllerListener = listener;

        setUpMouseListener();
        reloadContents();
    }

    public List<String> getSelectedItems() {

        addSelectedKeys();
        return this.selected_keys;
    }

    @Override
    void handleDoubleClickEvent() {

        S3ListModel selected = listView.getSelectionModel().getSelectedItem();
        File file = (File)selected.getPath();

        if (selected.getName().equalsIgnoreCase(MOVE_BACK)) {

            if (file.getParent() != null) {
                path = file.getParent();
                reloadContents();
            }
        } else {

            if (file.isDirectory()) {
                path = file.getAbsolutePath();
                reloadContents();
            } else {
                addSelectedKeys();
                fileListControllerListener.selectionComplete();
            }
        }
    }

    private void addSelectedKeys() {

        if (listView.getItems() != null && !listView.getItems().isEmpty()) {

            List<S3ListModel> selectedItems = listView.getSelectionModel().getSelectedItems();

            for (S3ListModel key : selectedItems) {
                selected_keys.add(((File)key.getPath()).getAbsolutePath());
            }
        }
    }

    private void reloadContents() {

        listView.getItems().clear();

        File f = new File(path);
        File[] objects = f.listFiles();

        if (f.getParent() != null) {
            S3ListModel model = new S3ListModel(MOVE_BACK, f, S3ListModel.FILE_BACK);
            listView.getItems().add(model);
        }

        addDirectories(objects);
        addFileKeys(objects);
    }

    private void addDirectories(File[] objects) {

        for (File object : objects) {

            if (object.isDirectory() && !object.isHidden()) {
                S3ListModel model = new S3ListModel(object.getName(), object, S3ListModel.FILE_DIR);
                listView.getItems().add(model);
            }
        }
    }

    private void addFileKeys(File[] objects) {

        for (File file : objects) {

            if (file.isFile() && FileUtils.getFileExtension(file).equalsIgnoreCase(FILE_EXTENSION)) {
                S3ListModel model = new S3ListModel(file.getName(), file, S3ListModel.FILE_DOC);
                listView.getItems().add(model);
            }
        }
    }
}
