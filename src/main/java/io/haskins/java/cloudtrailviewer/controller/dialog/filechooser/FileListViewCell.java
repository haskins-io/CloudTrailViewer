package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.observable.FileListModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * Custom List cell for showing Files with associated icon
 *
 * Created by markhaskins on 18/02/2017.
 */
class FileListViewCell extends ListCell<FileListModel> {

    private HBox layout = new HBox();
    private Label icon = new Label();
    private Label name = new Label();

    private FontAwesomeIconView drive = new FontAwesomeIconView(FontAwesomeIcon.SERVER);
    private FontAwesomeIconView folder = new FontAwesomeIconView(FontAwesomeIcon.FOLDER);
    private FontAwesomeIconView file = new FontAwesomeIconView(FontAwesomeIcon.FILE);
    private FontAwesomeIconView parent = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);

    FileListViewCell() {

        name.setPadding(new Insets(0, 0, 0, 10));

        layout.getChildren().add(icon);
        layout.getChildren().add(name);

        folder.getStyleClass().add("file_dir");
        file.getStyleClass().add("file_file");
    }

    @Override
    public void updateItem(FileListModel model, boolean empty) {

        super.updateItem(model, empty);

        if (model != null) {

            if (model.getFileType() == FileListModel.FILE_DIR) {
                icon.setGraphic(folder);
            } else if (model.getFileType() == FileListModel.FILE_DOC) {
                icon.setGraphic(file);
            } else if (model.getFileType() == FileListModel.FILE_DRIVE) {
                icon.setGraphic(drive);
            }  else {
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
