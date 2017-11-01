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

package io.haskins.java.cloudtrailviewer.service.listener;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import org.apache.lucene.document.Document;

/**
 * Interface that should be implemented if a class need to know about EventService events
 *
 * Created by markhaskins on 04/01/2017.
 */
public interface DataServiceListener {

    /**
     * A new Event
     * @param document the new Document
     */
    void newEvent(Document document);

    /**
     * The Event service is currently processing this file
     * @param fileNum The number of the file being processed
     * @param totalFiles The total number of files to be processed
     */
    void loadingFile(int fileNum, int totalFiles);

    /**
     * All files have been loaded
     * @param reload A boolean flag to indicate if the recieving listener should reload their data.
     */
    void finishedLoading(boolean reload);

    /**
     * All local events should be cleared
     */
    void clearEvents();
}
