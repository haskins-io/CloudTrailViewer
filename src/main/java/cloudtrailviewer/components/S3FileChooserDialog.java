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

import cloudtrailviewer.PropertiesSingleton;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author mark
 */
public class S3FileChooserDialog {
    
    private final ObservableList<String> files = FXCollections.observableArrayList ();
    
    private final ListView<String> list = new ListView<>();  
    private final Button btnClose = new Button("Close");
    private final Button btnOpen = new Button("Open");
    
    private static final int ACTION_CLOSE = 1;
    private static final int ACTION_OPEN = 2;
    private int action = ACTION_CLOSE;
    
    private final Stage stage;
    
    private String prefix = "";
    
    public S3FileChooserDialog(Window window) {
        
        this.stage = new Stage();
        this.stage.initOwner(window);
        
        init();
        
        reloadContents();
    }
    
    public List<String> show() {
        
        this.stage.showAndWait();
        
        List<String> selectedFiles = new ArrayList<>();
        
        if (action == ACTION_OPEN) {
            
            for (String item : list.getSelectionModel().getSelectedItems()) {

                String fqfn = prefix + item;

                selectedFiles.add(fqfn);
            } 
        }

        return selectedFiles;
    }
    
    private void init() {
        
        stage.titleProperty().set("S3 File Browser");
        
        // layout components
        BorderPane borderPane = new BorderPane();
        buildInterface(borderPane);
        
        // add to stage
        Scene scene = new Scene(borderPane);
        this.stage.setScene(scene);
        
        this.stage.initModality(Modality.WINDOW_MODAL);
    }
    
    private void buildInterface(BorderPane borderPane) {
        
        // The Buttons
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                
                action = ACTION_CLOSE;
                stage.close();
                System.out.println("Close Clicked");
            }
        });
        btnClose.setDisable(false);
        
        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                
                action = ACTION_OPEN;
                stage.close();
                System.out.println("Open Clicked");
            }
        });
        btnOpen.setDisable(true);
        
        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnClose, btnOpen);
        
        
        // The file area
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.setItems(files);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    
                    if(mouseEvent.getClickCount() == 2){
                        
                        handleDoubleClickEvent();
                    } 
                    else if (mouseEvent.getClickCount() == 1) {
                        
                        // enable / disable the Open button
                        String selected = list.getSelectionModel().getSelectedItem();
                        if (selected.contains("/")) {
                            btnOpen.setDisable(true);
                        } 
                        else
                        {
                            btnOpen.setDisable(false);
                        }
                    }
                }
            }
        });
        
        borderPane.setCenter(list);
        borderPane.setBottom(buttons);
    }
    
    private void handleDoubleClickEvent() {
        
        String selected = list.getSelectionModel().getSelectedItem();
        
        // remove any slashes at the beginning
        int firstSlash = selected.indexOf("/");
        if (firstSlash == 0) {
            selected = selected.substring(1, selected.length());
        }
        
        // remove any trailing slashes
        int lastSlash = selected.lastIndexOf("/") + 1;
        if (lastSlash == selected.length()) {
            
            prefix = prefix + selected;
            reloadContents();
        }
        else {
            // it must be a file so we'll close the dialog
            action = ACTION_OPEN;
            stage.close();
        }
    }
    
    private void reloadContents() {
               
        List<String> tmp = new ArrayList<String>();
        this.files.setAll(tmp);
                
        String bucketName = PropertiesSingleton.getInstance().getProperty("Bucket");
        
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setDelimiter("/");
        
		AWSCredentials credentials = 
            new BasicAWSCredentials(
                PropertiesSingleton.getInstance().getProperty("Key"), 
                PropertiesSingleton.getInstance().getProperty("Secret")
            );
        
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        
        
        ObjectListing objectListing = s3Client.listObjects(listObjectsRequest);
        
        // these are directories
        List<String> directories = objectListing.getCommonPrefixes();
        for (String directory : directories) {
            
            tmp.add( stripPrefix( directory) );
        }
        
        // these are files
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for (final S3ObjectSummary objectSummary : objectSummaries) {
        
            tmp.add( stripPrefix( objectSummary.getKey() ) );
        }
        
        this.files.setAll(tmp);
    }
    
    private String stripPrefix(String key) {
        
        String stripped = key;
        
        if (this.prefix.trim().length() > 0) {
            
            int prefixLength = this.prefix.length() - 1;
            stripped = key.substring(prefixLength, key.length());
            
            int firstSlash = stripped.indexOf("/");
            if (firstSlash == 0) {
                stripped = stripped.substring(1, stripped.length());
            }
        } 
        
        return stripped;
    }
}
