package com.haskins.jcloudtrailerviewer.event;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.haskins.jcloudtrailerviewer.PropertiesSingleton;
import com.haskins.jcloudtrailerviewer.model.Event;
import com.haskins.jcloudtrailerviewer.model.Records;
import com.haskins.jcloudtrailerviewer.panel.StatusBarPanel;
import com.haskins.jcloudtrailerviewer.util.JvmUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author mark.haskins
 */
public class EventLoader {

    private final int BUFFER_SIZE = 32;
    
    private final List<EventLoaderListener> listeners = new ArrayList<>();

    public void addListener(EventLoaderListener l) {
        this.listeners.add(l);
    }

    public void loadFromLocalFiles(final File[] files) {

        if (files != null && files.length > 0) {
            
            int numFiles = files.length;
            int count = 0;
            
            for (File file : files) {
                
                count++;
                
                try {
                    
                    StatusBarPanel.getInstance().setMessage("Reading file " + count + " of " + numFiles);
                    
                    long start = System.currentTimeMillis();
                    readLogFile(file);
                    long end = System.currentTimeMillis();
                    
                    System.out.println("Loading File took : " + (end - start));
                    
                    StatusBarPanel.getInstance().setMemory(JvmUtils.getMemoryUsed());
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            StatusBarPanel.getInstance().setMessage("Finished loading files");
            StatusBarPanel.getInstance().setLoadedFiles(files.length);
        }
    }

    public void loadFromS3Files(final List<String> keys) {

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
                try {
                    
                    StatusBarPanel.getInstance().setMessage("Reading file " + count + " of " + numFiles);
                    
                    readS3File(s3Client, bucketName, key);
                    
                    StatusBarPanel.getInstance().setMemory(JvmUtils.getMemoryUsed());
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            StatusBarPanel.getInstance().setMessage("Finished loading files");
            StatusBarPanel.getInstance().setLoadedFiles(keys.size());
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

        StatusBarPanel.getInstance().incrementEventsLoaded(events.size());
        
        for (EventLoaderListener l : listeners) {

            l.newEvents(events);
        }
    }
}
