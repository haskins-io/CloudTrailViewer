/*
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2017  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package io.haskins.java.cloudtrailviewer.controller.dialog.filechooser;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.observable.FileListModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

/**
 * Class that extends ListView to provide custom controll using ListViewCell
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FileListController extends ListView<FileListModel> {

    private FileHandler fileHandler;

    public FileListController() {

        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<FileListModel> oList = FXCollections.observableArrayList();
        this.setItems(oList);

        this.setCellFactory(new Callback<ListView<FileListModel>, ListCell<FileListModel>>() {
            @Override
            public ListCell<FileListModel> call(ListView<FileListModel> list) {
                return new ListViewCell();
            }
        });
    }

    /**
     * Initialises the Class, and attempts to load the first batch of files from S3
     */
    public void init(FileListControllerListener listener) {
        fileHandler = new LocalFileHandler(this, listener);
    }

    /**
     * Initialises the Class, and attempts to load the first batch of files from S3
     */
    public void init(AwsAccount currentAccount, FileListControllerListener listener) {
        fileHandler = new RemoteFileHandler(this, currentAccount, listener);
    }

    List<String> getSelectedItems() {
        return fileHandler.getSelectedItems();
    }
}

class ListViewCell extends ListCell<FileListModel> {

    private HBox layout = new HBox();
    private Label icon = new Label();
    private Label name = new Label();

    private FontAwesomeIconView folder = new FontAwesomeIconView(FontAwesomeIcon.FOLDER);
    private FontAwesomeIconView file = new FontAwesomeIconView(FontAwesomeIcon.FILE);
    private FontAwesomeIconView parent = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);

    ListViewCell() {

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


