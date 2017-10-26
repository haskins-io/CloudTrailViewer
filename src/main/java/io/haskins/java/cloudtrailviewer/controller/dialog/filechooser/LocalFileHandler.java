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
import io.haskins.java.cloudtrailviewer.utils.FileUtils;
import io.haskins.java.cloudtrailviewer.utils.OsUtils;
import javafx.scene.control.ListView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

/**
 * Handler for navigating files on the local file system.
 *
 * Created by markhaskins on 27/01/2017.
 */
class LocalFileHandler extends FileHandler {

    private static final String WINDOWS_ROOT = "WindowsRoot";

    private String path = FileUtils.getApplicationDirectory();

    LocalFileHandler(ListView<FileListModel> listView, FileListControllerListener listener) {

        this.listView = listView;
        listView.setItems(data);

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

        FileListModel selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {

            File file = (File)selected.getPath();

            if (selected.getName().equalsIgnoreCase(MOVE_BACK)) {

                if (file.getParent() != null) {
                    path = file.getParent();
                    reloadContents();
                } else if (file.getParent() == null && OsUtils.isWindows()) {
                    path = WINDOWS_ROOT;
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
    }

    private void addSelectedKeys() {

        selected_keys.clear();

        if (listView.getSelectionModel().getSelectedItems() != null &&
                !listView.getSelectionModel().getSelectedItems().isEmpty()) {

            File file = (File)listView.getSelectionModel().getSelectedItem().getPath();

            if (isScanning && file.isDirectory()) {

                File f = new File(file.getAbsolutePath());
                File[] files = f.listFiles();
                if (files != null) {
                    for (File object : files) {

                        if (object.isFile()) {
                            selected_keys.add(object.getAbsolutePath());
                        }
                    }
                }

            } else {
                List<FileListModel> selectedItems = listView.getSelectionModel().getSelectedItems();

                for (FileListModel key : selectedItems) {
                    selected_keys.add(((File)key.getPath()).getAbsolutePath());
                }
            }
        }
    }

    private void reloadContents() {

        data.clear();

        if (path.equalsIgnoreCase(WINDOWS_ROOT)) {

            File[] roots = File.listRoots();
            addDrives(roots);

        } else {

            File f = new File(path);
            File[] objects = f.listFiles();

            if (f.getParent() != null) {
                FileListModel model = new FileListModel(MOVE_BACK, f, FileListModel.FILE_BACK);
                data.add(model);
            }

            if (f.getParent() == null && OsUtils.isWindows()) {
                FileListModel model = new FileListModel(MOVE_BACK, f, FileListModel.FILE_BACK);
                data.add(model);
            }

            addDirectories(objects);
            addFileKeys(objects);
        }

    }

    private void addDrives(File[] objects) {

        FileSystemView fsv = FileSystemView.getFileSystemView();

        for (File object : objects) {
            FileListModel model = new FileListModel(fsv.getSystemDisplayName(object), object, FileListModel.FILE_DRIVE);
            data.add(model);
        }
    }

    private void addDirectories(File[] objects) {

        for (File object : objects) {

            if (object.isDirectory() && !object.isHidden()) {
                FileListModel model = new FileListModel(object.getName(), object, FileListModel.FILE_DIR);
                data.add(model);
            }
        }
    }

    private void addFileKeys(File[] objects) {

        for (File file : objects) {

            if (file.isFile()) {
                FileListModel model = new FileListModel(file.getName(), file, FileListModel.FILE_DOC);
                data.add(model);
            }
        }
    }
}
