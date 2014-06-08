package com.haskins.aws.jcloudtrailviewer.events;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.haskins.aws.jcloudtrailviewer.PropertiesSingleton;
import com.haskins.aws.jcloudtrailviewer.models.Event;
import com.haskins.aws.jcloudtrailviewer.models.Records;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark
 */
public class EventLoader {

    private final List<EventLoaderListener> listeners = new ArrayList<>();

    private Stage dialog;

    private final Label loadingFiles = new Label();
    private final Label loadingEvents = new Label("Loading Events");
    private final ProgressBar eventsProgress = new ProgressBar();

    public EventLoader(Stage stage) {

        buildDialog(stage);
    }

    public void addListener(EventLoaderListener l) {
        this.listeners.add(l);
    }

    public void loadFromLocalFiles(final List<File> files) {

        if (files != null && files.size() > 0) {

            for (File file : files) {
                try {
                    readLogFile(file);
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void loadFromS3Files(final List<String> keys) {
        
        if (keys != null && keys.size() > 0) {
            
            for (String key : keys) {
                try {
                    readS3File(key);
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void readLogFile(File file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String strFile = new String(encoded, StandardCharsets.UTF_8);

        readLogEvents(strFile);
    }
    
    private void readS3File(String key) throws IOException  {
        
		AWSCredentials credentials = 
            new BasicAWSCredentials(
                PropertiesSingleton.getInstance().getProperty("Key"), 
                PropertiesSingleton.getInstance().getProperty("Secret")
            );
        
        AmazonS3 s3Client = new AmazonS3Client(credentials);
        String bucketName = PropertiesSingleton.getInstance().getProperty("Bucket");
        
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        
        GZIPInputStream gzis = new GZIPInputStream(s3Object.getObjectContent());
        BufferedReader bf = new BufferedReader(new InputStreamReader(gzis, "UTF-8"));        
        
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }
        bf.close();
        gzis.close();
        
        readLogEvents(outStr);
    }

    private void readLogEvents(String jsonString) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        Records records = mapper.readValue(jsonString, Records.class);

        List<Event> events = records.getLogEvents();
        int count = 1;

        for (Event event : events) {

            String rawJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(event);
            event.setRawJSON(rawJson);

            eventsProgress.setProgress(count);

            count++;
        }

        for (EventLoaderListener l : listeners) {

            l.newEvents(events);
        }
    }

    private void buildDialog(Stage stage) {

        dialog = new Stage();
        dialog.setWidth(360);
        dialog.setHeight(240);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);

        Scene scene = new Scene(new Group());

        loadingFiles.setLayoutX(115);
        loadingFiles.setLayoutY(15);

        loadingEvents.setLayoutX(20);
        loadingEvents.setLayoutY(45);

        eventsProgress.setLayoutX(130);
        eventsProgress.setLayoutY(40);
        eventsProgress.setPrefWidth(200);

        Pane pane = new Pane();
        pane.setPrefWidth(360);
        pane.setPrefHeight(240);

        pane.getChildren().addAll(loadingEvents, loadingFiles, eventsProgress);

        ((Group) scene.getRoot()).getChildren().addAll(loadingEvents, loadingFiles, eventsProgress);

        dialog.setScene(scene);
    }
}
