package com.haskins.aws.jcloudtrailviewer.components;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.haskins.aws.jcloudtrailviewer.PropertiesSingleton;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author mark
 */
public class S3FileChooserPane extends Pane {
    
    private ListView<String> list = new ListView<>();
    private ObservableList<String> items = FXCollections.observableArrayList ();
    
    private String prefix = "";
    
    public S3FileChooserPane() {
        
        buildInterface();
        
        reloadContents();
    }
    
    public List<String> getSelectedFiles() {

        List<String> selectedFiles = new ArrayList<>();
        
        for (String item : list.getSelectionModel().getSelectedItems()) {
            
            String fqfn = prefix + item;
            
            selectedFiles.add(fqfn);
        }
        
        return selectedFiles;
    }
    
    public void reloadContents() {
               
        List<String> tmp = new ArrayList<>();
        this.items.setAll(tmp);
                
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
        List<S3ObjectSummary> files = objectListing.getObjectSummaries();
        
        for (final S3ObjectSummary objectSummary : files) {
        
            tmp.add( stripPrefix( objectSummary.getKey() ) );
        }
        
        this.items.setAll(tmp);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildInterface() {
        
        this.setPrefHeight(480);
        this.setPrefWidth(640);
        
        BorderPane root = new BorderPane();
        
        // Drop down
        ChoiceBox hierarchy = new ChoiceBox();        
        
        // files area
        list.setItems(items);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        
                        handleEvent();
                    }
                }
            }
        });
               
        
        root.setTop(hierarchy);
        root.setCenter(list);
        
        list.setPrefHeight( 460 );
        list.setPrefWidth( 610 );
        
        this.getChildren().add(root);
    }
    
    private void handleEvent() {
        
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
