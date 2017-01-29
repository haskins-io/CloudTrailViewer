package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.observable.S3ListModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.List;

/**
 * Created by markhaskins on 27/01/2017.
 */
public class FileListController extends ListView<S3ListModel> {

    private ObservableList<S3ListModel> oList = FXCollections.observableArrayList();

    private FileListControllerListener listener;

    private FileHandler fileHandler;

    public FileListController() {

        this.setItems(oList);

        this.setCellFactory(new Callback<ListView<S3ListModel>, ListCell<S3ListModel>>() {
            @Override
            public ListCell<S3ListModel> call(ListView<S3ListModel> list) {
                return new ListViewCell();
            }
        });
    }

    /**
     * Initialises the Class, and attempts to load the first batch of files from S3
     *
     * @return Return True if sucessful otherwise false
     */
    public void init(FileListControllerListener listener) {

        this.listener = listener;
        fileHandler = new LocalFileHandler(this, listener);
    }

    /**
     * Initialises the Class, and attempts to load the first batch of files from S3
     *
     * @return Return True if sucessful otherwise false
     */
    public void init(AwsAccount currentAccount, FileListControllerListener listener) {

        this.listener = listener;
        fileHandler = new RemoteFileHandler(this, currentAccount, listener);
    }

    public List<String> getSelectedItems() {
        return fileHandler.getSelectedItems();
    }
}

class ListViewCell extends ListCell<S3ListModel> {

    private BorderPane layout = new BorderPane();
    private Label icon = new Label();
    private Label name = new Label();

    private FontAwesomeIconView folder = new FontAwesomeIconView(FontAwesomeIcon.FOLDER);
    private FontAwesomeIconView file = new FontAwesomeIconView(FontAwesomeIcon.FILE);
    private FontAwesomeIconView parent = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);

    ListViewCell() {

        layout.setLeft(icon);
        layout.setCenter(name);

        folder.getStyleClass().add("file_dir");
        file.getStyleClass().add("file_file");
    }

    @Override
    public void updateItem(S3ListModel model, boolean empty) {

        super.updateItem(model, empty);

        if (model != null) {

            if (model.getFileType() == S3ListModel.FILE_DIR) {
                icon.setGraphic(folder);
            } else if (model.getFileType() == S3ListModel.FILE_DOC) {
                icon.setGraphic(file);
            } else {
                icon.setGraphic(parent);
            }

            name.setText(model.getName());

            setText(null);
            setGraphic(layout);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}


