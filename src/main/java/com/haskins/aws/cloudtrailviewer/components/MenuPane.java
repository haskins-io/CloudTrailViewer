/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2014  Mark P. Haskins

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package com.haskins.aws.cloudtrailviewer.components;

import com.haskins.aws.cloudtrailviewer.events.EventLoader;
import com.haskins.aws.cloudtrailviewer.models.Event;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

/**
 *
 * @author mark
 */
public class MenuPane extends AbstractPane {
        
    private final MenuBar menu = new MenuBar();
    
    private final EventLoader eventLoader;
    
    public MenuPane(Scene scene, EventLoader eventLoader) {
        
        super(scene);
        
        this.eventLoader = eventLoader;
    }
    
    public MenuBar getPane(final Stage stage) {
        
        menu.setPrefWidth(stage.getWidth());
        menu.setPrefHeight(29);
        
        // --- Menu File
        Menu menuFile = new Menu("File");
        
        // --- Menu Logs
        Menu menuLogs = new Menu("Logs");
        
        MenuItem loadLocal = new MenuItem("Load Local Files");
        loadLocal.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Log File(s)");
                List<File> list = fileChooser.showOpenMultipleDialog(stage);
                if (list != null) {
                    eventLoader.loadFromLocalFiles(list);
                }
            }
        });
        
        MenuItem loadS3 = new MenuItem("Load S3 Files");
        loadS3.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent t) {
                
                List<String> keys = showS3FileChooser();
                if (keys != null && keys.size() > 0) {
                    
                    eventLoader.loadFromS3Files(keys);
                }
            }
        });
        
        menuLogs.getItems().addAll(loadLocal,loadS3);
        
        // Menu Help
        Menu menuHelp = new Menu("Help");
        
        menu.getMenus().addAll(menuFile, menuLogs, menuHelp);
        
        return menu;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// EventsDatabaseListener methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void onEventsUpdated(Map<String, Event> updatedEvents) {
        // Not Implemented
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// protected methods
    ////////////////////////////////////////////////////////////////////////////
    @Override
    protected void widthChanged(Number newWidth) {
         menu.setPrefWidth(newWidth.doubleValue());
    }
    
    @Override
    protected void heightChanged(Number newHeight) {
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private List<String> showS3FileChooser() {
     
        final List<String> selectedFiles = new ArrayList<>();
        
        Dialog dlg = new Dialog(null, "Open Log Files(s)");
                
        final S3FileChooserPane s3FileChooser = new S3FileChooserPane();
        
        dlg.setResizable(false);
        dlg.setIconifiable(false);
        dlg.setContent(s3FileChooser);
        
        final Action actionOpen = new AbstractAction("Open") {
            {  
                ButtonBar.setType(this, ButtonType.OK_DONE); 
            }

            // This method is called when the Open button is clicked...
            @Override
            public void handle(ActionEvent ae) {
                 
                Dialog dlg = (Dialog) ae.getSource();   
                selectedFiles.addAll( s3FileChooser.getSelectedFiles() );
                dlg.hide();
             }
        };
                
        dlg.getActions().addAll(actionOpen, Dialog.Actions.CANCEL);
        
        dlg.show();
        
        return selectedFiles;
    }
}
