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

package io.haskins.java.cloudtrailviewer.model.observable;

import javafx.beans.property.*;

/**
 * Model class handles file information for both local and s3 fils.
 *
 * Created by markhaskins on 27/01/2017.
 */
public class FileListModel {

    public static final int FILE_BACK = 0;
    public static final int FILE_DIR = 1;
    public static final int FILE_DOC = 2;

    private final StringProperty name;
    private final ObjectProperty path;
    private final IntegerProperty fileType;

    public FileListModel(String path, Object alias, int fileType) {

        this.name = new SimpleStringProperty(path);
        this.path = new SimpleObjectProperty(alias);
        this.fileType = new SimpleIntegerProperty(fileType);
    }

    public String getName() {
        return this.name.get();
    }

    public Object getPath() {
        return this.path.get();
    }

    public int getFileType() {
        return this.fileType.get();
    }
}
