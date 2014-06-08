package com.haskins.aws.jcloudtrailviewer.components;

import com.haskins.aws.jcloudtrailviewer.events.EventLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class MenuPane {
    
    private final Scene scene;
    private final EventLoader eventLoader;
    
    private final MenuBar menu = new MenuBar();
    
    public MenuPane(Scene scene, EventLoader eventLoader) {
                
        this.eventLoader = eventLoader;
        this.scene = scene;
        
        addListeners();
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
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void addListeners() {
                        
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                menu.setPrefWidth(newSceneWidth.doubleValue());
            }
        }); 
    }
    
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
