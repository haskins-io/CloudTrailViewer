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

package io.haskins.java.cloudtrailviewer.controller.dialog.widget;

import io.haskins.java.cloudtrailviewer.controller.dialog.widget.AbstractDialogController;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import javafx.fxml.FXML;

/**
 * Controller that handles a dialog when adding a new Table widget.
 *
 * Created by markhaskins on 06/01/2017.
 */
public class TableDialogController extends AbstractDialogController {

    @FXML
    protected void handleUpdate() {

        widget.setTop(top.getValue());

        super.handleUpdate();
    }

    public void setWidget(DashboardWidget widget) {

        super.setWidget(widget);

        if (widget.getTop() != 0) {
            top.setValue(widget.getTop());
        }

    }
}
