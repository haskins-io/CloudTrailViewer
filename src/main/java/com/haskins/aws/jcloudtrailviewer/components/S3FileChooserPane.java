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
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

/**
 *
 * @author mark
 */
public class S3FileChooserPane extends Pane {
    
    private ObservableList<String> items = FXCollections.observableArrayList ();
    
    private ListView<String> list = new ListView<>();
    private BreadCrumbBar breadCrumb = new BreadCrumbBar();       
    
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
        
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private void buildInterface() {
        
        this.setPrefHeight(480);
        this.setPrefWidth(640);
        
        BorderPane root = new BorderPane();
        
        // BreadCrumb
        breadCrumb.setPrefWidth(620);
        breadCrumb.setSelectedCrumb(new TreeItem("/"));
        breadCrumb.setOnCrumbAction(new EventHandler<BreadCrumbBar.BreadCrumbActionEvent>() {
            @Override
            public void handle(BreadCrumbActionEvent event) {                
                updateBreadCrumb(event.getSelectedCrumb());
            }
        });
        
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
               
        
        root.setTop(breadCrumb);
        root.setCenter(list);
        
        list.setPrefHeight( 460 );
        list.setPrefWidth( 620 );
        
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
    
    private void reloadContents() {
               
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
        
        updateBreadCrumb();
        
        this.items.setAll(tmp);
    }
    
    private void updateBreadCrumb(TreeItem selected) {
        
        String newPrefix = selected.getValue().toString() + "/";

        while (selected.getParent() != null) {

            selected = selected.getParent();
            newPrefix = selected.getValue().toString() + "/" + newPrefix;
        }

        System.out.println(newPrefix);

        this.prefix = newPrefix;
        
        reloadContents();
    }
    
    private void updateBreadCrumb() {
                
        breadCrumb.setSelectedCrumb( BreadCrumbBar.buildTreeModel( this.prefix.split("/")) );
    }
}
