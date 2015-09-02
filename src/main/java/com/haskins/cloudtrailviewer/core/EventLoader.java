/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.core;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haskins.cloudtrailviewer.dao.AccountDao;
import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.model.event.Event;
import com.haskins.cloudtrailviewer.model.event.Records;
import com.haskins.cloudtrailviewer.model.filter.AllFilter;
import com.haskins.cloudtrailviewer.model.filter.Filter;
import com.haskins.cloudtrailviewer.model.load.LoadFileRequest;
import com.haskins.cloudtrailviewer.utils.EventUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import javax.swing.SwingWorker;


/**
 * Class responsible for Loading events into the database.
 * 
 * @author mark
 */
public class EventLoader {

    private final int BUFFER_SIZE = 32;
    
    private final FilteredEventDatabase eventDb;
    
    private final List<EventLoaderListener> listeners = new ArrayList<>();
    
    /**
     * Default Constructor
     * @param database Database that events should be added too
     */
    public EventLoader(FilteredEventDatabase database) {
        eventDb = database;
        listeners.add(eventDb);
    }
    
    /**
     * registers a listener to the Loaded
     * @param listener reference to a listener
     */
    public void addEventLoaderListener(EventLoaderListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Loads Events fro the local file system
     * @param request 
     */
    public void loadEventsFromLocalFiles(final LoadFileRequest request) {

        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            public Void doInBackground() {

                if (request.getFilter() == null) {
                    request.setFilter(new AllFilter());
                }
                
                int count = 0;
                
                List<String> filenames = request.getFilenames();
                int total = filenames.size();
                
                for (String filename : filenames) {
                    
                    count++;
                    
                    for (EventLoaderListener l : listeners) {
                        l.processingFile(count, total);
                    }
                    
                    try (InputStream stream = loadEventFromLocalFile(filename);) {
                        processStream(stream, request.getFilter());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                for (EventLoaderListener l : listeners) {
                    l.finishedLoading();
                }
                
                return null;
            };
        };

       worker.execute();
    }
    
    /**
     * Loads events from S3
     * @param request 
     */
    public void loadEventsFromS3(final LoadFileRequest request) {

        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            public Void doInBackground() {

                int count = 0;
                
                List<AwsAccount> accounts = AccountDao.getAllAccounts(true);
                if (accounts.isEmpty()) {
                    return null;
                }
                
                AwsAccount account = accounts.get(0);
                
                String key = account.getKey();
                String secret = account.getSecret();
                
                AWSCredentials credentials= new BasicAWSCredentials(key, secret);

                AmazonS3 s3Client = new AmazonS3Client(credentials);
                String bucketName = account.getBucket();
                
                if (request.getFilter() == null) {
                    request.setFilter(new AllFilter());
                }
                
                List<String> filenames = request.getFilenames();
                int total = filenames.size();
                
                for (String filename : filenames) {
                    
                    count++;
                    
                    for (EventLoaderListener l : listeners) {
                        l.processingFile(count, total);
                    }
                    
                    try (InputStream stream = loadEventFromS3(s3Client, bucketName, filename);) {
                        processStream(stream, request.getFilter());

                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (EventLoaderListener l : listeners) {
                    l.finishedLoading();
                }
                
                return null;
            };
        };

        worker.execute();
    }

    private InputStream loadEventFromLocalFile(final String file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file));
        return new ByteArrayInputStream(encoded);  
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private InputStream loadEventFromS3(AmazonS3 s3Client, String bucketName, final String key) throws IOException {
        
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        return s3Object.getObjectContent();
    }
    
    private String uncompress(InputStream stream) {
        
        String jsonString = "";
        
        try (GZIPInputStream gzis = new GZIPInputStream(stream, BUFFER_SIZE)) {
            
            try (BufferedReader bf = new BufferedReader(new InputStreamReader(gzis, "UTF-8"));) {
                
                String line;
                while ((line = bf.readLine()) != null) {
                    jsonString += line;
                }
            }
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
        
        Records records = null;
        ObjectMapper mapper = new ObjectMapper();
        
        if (json_string != null) {
            
            try {
                records = mapper.readValue(json_string, Records.class);
                mapper = null;
                json_string = null;
            }
            catch (IOException jpe) {
                Logger.getLogger(EventLoader.class.getName()).log(Level.SEVERE, null, jpe);
            }  
        }

        return records;
    }
    
    private void processStream(InputStream stream, Filter filter) {
        
        Records records = createRecords(uncompress(stream));
            
        if (records != null) {
            
            List<Event> events = records.getLogEvents();
            for (Event event : events) {

                if (filter.passesFilter(event)) {
                    
                    event.getResourceInfo();
                    EventUtils.addTimestamp(event);
                    eventDb.addEvent(event);
                }
            }
        }
    }
}
