/*    
CloudTrail Log Viewer, is a Java desktop application for reading AWS CloudTrail
logs files.

Copyright (C) 2015  Mark P. Haskins

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

package com.haskins.jcloudtrailerviewer.event;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.jCloudTrailViewer;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.Records;
import com.haskins.jcloudtrailerviewer.components.S3FileChooserDialog;
import com.haskins.jcloudtrailerviewer.util.EventUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * The EventLoad class is responsible for loading files from either the local
 * machine or from an S3 bucket.
 * 
 * @author mark.haskins
 */
public class EventLoader {

    private final int BUFFER_SIZE = 32;
    
    private final JFileChooser fileChooser = new JFileChooser();
    
    private final List<EventLoaderListener> listeners = new LinkedList<>();

    public EventLoader() {
        fileChooser.setMultiSelectionEnabled(true);
    }
    
    /**
     * registers the provided class as a listener to EventLoaderListener events.
     * @param l 
     */
    public void addListener(EventLoaderListener l) {
        this.listeners.add(l);
    }

    /**
     * Shows a JFileChooser dialog for loading files from the local machine.
     * 
     * If files are selected they will be read in and announced via the 
     * EventLoaderListener interface.
     */
    public void showFileBrowser() {
        
        int status = fileChooser.showOpenDialog(jCloudTrailViewer.DESKTOP);
        if (status == JFileChooser.APPROVE_OPTION) {

            postMessage("Loading Files from Disk");

            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                public Void doInBackground() {

                    File[] list;

                    if (fileChooser.getSelectedFiles().length != 0)  {

                        list = fileChooser.getSelectedFiles();

                    } else {

                        list = new File[1];
                        list[0] = fileChooser.getSelectedFile();
                    }

                    if (list != null) {
                        loadFromLocalFiles(list);
                    }

                    return null;
                };
            };
            worker.execute();
        }
    }
    
    /**
     * Shows the S3FileChooserDialog to load files from an S3 bucket.
     * 
     * If files are selected they will be read in and announced via the 
     * EventLoaderListener interface.
     */
    public void showS3Browser() {
        
        final List<String> files = S3FileChooserDialog.showDialog(jCloudTrailViewer.DESKTOP);

        if (!files.isEmpty()) {

            postMessage("Loading Files from S3");

            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                public Void doInBackground() {

                    loadFromS3Files(files);
                    return null;
                };
            };
            worker.execute();
        }
    }
    
    private void loadFromLocalFiles(final File[] files) {

        if (files != null && files.length > 0) {
            
            int numFiles = files.length;
            int count = 0;
            
            for (File file : files) {
                
                count++;
                
                try {
                    
                    postMessage("Processing file " + count + " of " + numFiles);
                    readLogFile(file);
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            postMessage("Finished loading files");
            
            for (EventLoaderListener l : listeners) {
                l.finishedLoading();
            }
        }
    }

    private void loadFromS3Files(final List<String> keys) {

        if (keys != null && keys.size() > 0) {
            
            AWSCredentials credentials
                = new BasicAWSCredentials(
                    PropertiesSingleton.getInstance().getProperty("Key"),
                    PropertiesSingleton.getInstance().getProperty("Secret")
                );

            AmazonS3 s3Client = new AmazonS3Client(credentials);
            String bucketName = PropertiesSingleton.getInstance().getProperty("Bucket");

            int numFiles = keys.size();
            int count = 0;
            
            for (String key : keys) {
                
                count++;
                
                try {
                    
                    postMessage("Processing file " + count + " of " + numFiles);
                    
                    readS3File(s3Client, bucketName, key);
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            for (EventLoaderListener l : listeners) {
                l.finishedLoading();
            }
            
            postMessage("Finished loading files");
        }
    }

    private void readLogFile(File file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        
        readLogEvents(encoded);
    }

    private void readS3File(AmazonS3 s3Client, String bucketName, String key) throws IOException {

        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        processStream(s3Object.getObjectContent());
    }

    private void readLogEvents(byte[] encoded) throws IOException {

        ByteArrayInputStream is = new ByteArrayInputStream(encoded);
        processStream(is);
    }
    
    private void processStream(InputStream stream) {
        
        String rawJson = uncompress(stream);
        
        if (rawJson.length() > 0) {
            Records records = createRecords(rawJson);
            if (records != null) {
                processRecords(records);  
            }
        }
    }
    
    private String uncompress(InputStream stream) {
        
        String jsonString = "";
        
        try {
            
            GZIPInputStream gzis = new GZIPInputStream(stream, BUFFER_SIZE);
            BufferedReader bf = new BufferedReader(new InputStreamReader(gzis, "UTF-8"));

            String line;
            while ((line = bf.readLine()) != null) {
                jsonString += line;
            }
            bf.close();
            gzis.close();

        }
        catch (ZipException | JsonParseException ex) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        return jsonString;
    }
    
    private Records createRecords(String json_string) {
        
        ObjectMapper mapper = new ObjectMapper();

        Records records = null;
        
        try {
            records = mapper.readValue(json_string, Records.class);
        }
        catch (IOException jpe) {
            Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, jpe);
        }
        
        return records;
    }
    
    private void processRecords(Records records) {
        
        List<Event> events = records.getLogEvents();

        for (Event event : events) {
            EventUtils.addTimestamp(event);
        }
        
        for (EventLoaderListener l : listeners) {

            l.newEvents(events);
        }
    }
    
    private void postMessage(String message) {
        
        for (EventLoaderListener l : listeners) {

            l.newMessage(message);
        }
    }
}
