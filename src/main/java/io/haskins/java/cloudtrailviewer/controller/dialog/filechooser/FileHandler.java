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

import io.haskins.java.cloudtrailviewer.model.observable.FileListModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that handles common logic for File Handlers
 *
 * Created by markhaskins on 27/01/2017.
 */
abstract class FileHandler {

    static final String MOVE_BACK = "..";
    static final String FILE_EXTENSION = "gz";

    ListView<FileListModel> listView;
    final ObservableList<FileListModel> data = FXCollections.observableArrayList();

    final List<String> selected_keys = new ArrayList<>();

    protected boolean isScanning = false;

    FileListControllerListener fileListControllerListener;

    abstract void handleDoubleClickEvent();

    public abstract List<String> getSelectedItems();


    void setScanning(boolean isScanning) {
        this.isScanning = isScanning;
    }

    void setUpMouseListener() {

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

                if (e.getClickCount() == 2) {

                    handleDoubleClickEvent();

                } else if (e.getClickCount() == 1) {

                    if (listView.getSelectionModel().getSelectedItem() != null) {

                        Object obj = listView.getSelectionModel().getSelectedItem().getPath();
                        if (obj instanceof  File) {

                            File selected = (File)obj;
                            if (!isScanning && selected.isDirectory()) {
                                fileListControllerListener.listItemSelected(false);
                            } else {
                                fileListControllerListener.listItemSelected(true);
                            }

                        } else {

                            String selected = (String)obj;
                            if (!isScanning && selected.contains("/")) {
                                fileListControllerListener.listItemSelected(false);
                            }
                            else {
                                fileListControllerListener.listItemSelected(true);
                            }
                        }
                    }
                }
            }
        });
    }
}
