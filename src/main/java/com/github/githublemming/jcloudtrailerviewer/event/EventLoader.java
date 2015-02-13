package com.github.githublemming.jcloudtrailerviewer.event;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.github.githublemming.jcloudtrailerviewer.PropertiesSingleton;
import com.github.githublemming.jcloudtrailerviewer.model.Event;
import com.github.githublemming.jcloudtrailerviewer.model.Records;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private final List<EventLoaderListener> listeners = new ArrayList<>();

    public void addListener(EventLoaderListener l) {
        this.listeners.add(l);
    }

    public void loadFromLocalFiles(final File[] files) {

        if (files != null && files.length > 0) {

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

            AWSCredentials credentials
                = new BasicAWSCredentials(
                    PropertiesSingleton.getInstance().getProperty("Key"),
                    PropertiesSingleton.getInstance().getProperty("Secret")
                );

            AmazonS3 s3Client = new AmazonS3Client(credentials);
            String bucketName = PropertiesSingleton.getInstance().getProperty("Bucket");

            for (String key : keys) {
                try {
                    readS3File(s3Client, bucketName, key);
                }
                catch (IOException ex) {
                    Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void readLogFile(File file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));

        readLogEvents(encoded);
    }

    private void readS3File(AmazonS3 s3Client, String bucketName, String key) throws IOException {

        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));

        uncompress(s3Object.getObjectContent());
    }

    private void readLogEvents(byte[] encoded) throws IOException {

        try {
            
            ByteArrayInputStream is = new ByteArrayInputStream(encoded);

            uncompress(is);
        }
        catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }
    
    private void uncompress(InputStream stream) throws IOException {
        
        try {
            
            final int BUFFER_SIZE = 32;
            
            GZIPInputStream gzis = new GZIPInputStream(stream, BUFFER_SIZE);

            BufferedReader bf = new BufferedReader(new InputStreamReader(gzis, "UTF-8"));

            String outStr = "";
            String line;
            while ((line = bf.readLine()) != null) {
                outStr += line;
            }
            bf.close();
            gzis.close();
            
            convertStringToJson(outStr);

        }
        catch (ZipException | JsonParseException ex) {

            System.out.println(ex.getMessage());
        }  
    }
    
    private void convertStringToJson(String json_string) {
        
        ObjectMapper mapper = new ObjectMapper();

        Records records = null;
        
        try {
            records = mapper.readValue(json_string, Records.class);
        }
        catch (IOException jpe) {

            System.out.println(jpe.getMessage());
        }
        
        if (records != null) {
            
            processRecords(records);
        }
    }
    
    private void processRecords(Records records) {
        
        ObjectMapper mapper = new ObjectMapper();
        
        List<Event> events = records.getLogEvents();

        for (Event event : events) {

            String rawJson;
            try {
                rawJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(event);
                event.setRawJSON(rawJson);

            } catch (IOException ex) {
                Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (EventLoaderListener l : listeners) {

            l.newEvents(events);
        }
    }
}
