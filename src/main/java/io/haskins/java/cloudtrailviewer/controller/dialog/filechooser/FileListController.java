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

import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.observable.FileListModel;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.List;

/**
 * Class that extends ListView to provide custom controll using FileListViewCell
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FileListController extends ListView<FileListModel> {

    private FileHandler fileHandler;

    public FileListController() {

        this.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<FileListModel> oList = FXCollections.observableArrayList();
        this.setItems(oList);

        this.setCellFactory(list -> new FileListViewCell());
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
    public void init(AwsAccount currentAccount, FileListControllerListener listener, AwsService awsService, String bucket) {
        fileHandler = new S3FileHandler(this, currentAccount, listener, awsService, bucket);
    }

    public void setScanning(boolean isScanning) {
        fileHandler.setScanning(isScanning);
    }

    List<String> getSelectedItems() {
        return fileHandler.getSelectedItems();
    }
}



