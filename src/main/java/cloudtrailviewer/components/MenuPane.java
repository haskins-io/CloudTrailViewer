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


package cloudtrailviewer.components;

import cloudtrailviewer.events.EventLoader;
import cloudtrailviewer.models.Event;
import java.io.File;
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

/**
 *
 * @author mark
 */
public class MenuPane extends AbstractPane {
        
    private final MenuBar menu = new MenuBar();
    
    private final EventLoader eventLoader;
    
    private final S3FileChooserDialog s3FileDialog;
    
    private final Stage stage;
    
    public MenuPane(Stage stage, Scene scene, EventLoader eventLoader) {
        
        super(scene);
        
        this.stage = stage;
        this.eventLoader = eventLoader;
        
        s3FileDialog = new S3FileChooserDialog(this.stage);
    }
    
    public MenuBar getPane() {
        
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
                
                List<String> keys = s3FileDialog.show();
                if (keys.size() > 0) {
                    
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
}
