/*
 * Copyright (C) 2016 Mark P. Haskins
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.haskins.cloudtrailviewer.dialog.s3filechooser;

/**
 *
 * @author markhaskins
 */
public class S3ListModel  {

    public static final int FILE_BACK = 0;
    public static final int FILE_DIR = 1;
    public static final int FILE_DOC = 2;

    private final String path;
    private final String alias;
    private final int fileType;

    S3ListModel(String path, String alias, int fileType) {
        this.path = path;
        this.alias = alias;
        this.fileType = fileType;
    }

    public String getPath() {
        return this.path;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getFileType() {
        return this.fileType;
    }
}
