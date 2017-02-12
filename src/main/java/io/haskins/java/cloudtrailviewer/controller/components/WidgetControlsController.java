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

package io.haskins.java.cloudtrailviewer.controller.components;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.haskins.java.cloudtrailviewer.model.DashboardWidget;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Created by markhaskins on 29/01/2017.
 */
public class WidgetControlsController extends BorderPane {

    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private Label titleLabel;
    @FXML private Label icon;

    private WidgetControlsControllerListener listener;

    private final FontAwesomeIconView ICON_CONFIG = new FontAwesomeIconView(FontAwesomeIcon.COG);
    private final FontAwesomeIconView ICON_REMOVE = new FontAwesomeIconView(FontAwesomeIcon.TIMES);

    public WidgetControlsController() {
        this.prefWidth(220);
        this.prefHeight(65);
    }

    public void init(WidgetControlsControllerListener l, DashboardWidget widget) {

        listener = l;
        titleLabel.setText(widget.getTitle());

        editButton.setGraphic(ICON_CONFIG);
        removeButton.setGraphic(ICON_REMOVE);

        icon.setGraphic(widget.getIcon());
    }

    @FXML void editWidget() {
        listener.editWidget();
    }
    @FXML void removeWidget() {
        listener.removeWidget();
    }

    public void hideEditButton() {
        editButton.setVisible(false);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

}
