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

package io.haskins.java.cloudtrailviewer.controller.dialog.preferences;

import io.haskins.java.cloudtrailviewer.service.DatabaseService;

/**
 * Controller class that handles the Security Panel.
 *
 * Created by markhaskins on 26/01/2017.
 */
public class SecurityPanelController extends PreferencesPanel {

    public void init(DatabaseService databaseService) {

        this.databaseService = databaseService;

        configureListView();

        String query = "SELECT api_call FROM aws_security ORDER BY api_call ASC";
        populateListBySQL(query);
    }

    void removeItem(String itemToRemove) {

        String query = "DELETE FROM aws_security WHERE api_call = '" + itemToRemove + "'";
        databaseService.doInsertUpdate(query);

        listView.getItems().remove(itemToRemove);
    }

    void addItem(String itemToAdd) {

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO aws_security (api_call)");
        query.append(" VALUES (").append("'").append(itemToAdd).append("'").append(")");

        databaseService.doInsertUpdate(query.toString());
    }

}
