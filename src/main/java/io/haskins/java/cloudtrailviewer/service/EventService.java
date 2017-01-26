package io.haskins.java.cloudtrailviewer.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.haskins.java.cloudtrailviewer.filter.AllFilter;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.listener.EventServiceListener;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * Service responsible for handling CloudTrail events.
 *
 * Created by markhaskins on 04/01/2017.
 */
@Service
public class EventService {

    public static final int FILE_TYPE_LOCAL = 1;
    private static final int FILE_TYPE_S3 = 2;

    private final static int BUFFER_SIZE = 32;

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private final AwsService awsService;
    private final GeoService geoService;

    private final List<EventServiceListener> listeners = new ArrayList<>();

    private final List<Event> eventDb = new ArrayList<>();

    @Autowired
    public EventService(AwsService awsService, GeoService geoService) {
        this.awsService = awsService;
        this.geoService = geoService;
    }

    public void registerAsListener(EventServiceListener l) {
        listeners.add(l);
    }

    public void loadFiles(List<String> filenames, CompositeFilter filters, int file_type) {

        if (filters == null) {
            filters = new CompositeFilter();
            filters.addFilter(new AllFilter());
        }

        AwsAccount activeAccount = null;
        AmazonS3 s3Client = null;

        if (file_type == FILE_TYPE_S3) {
            activeAccount = awsService.getActiveAccount();
            s3Client = awsService.getS3Client(activeAccount);
        }

        int count = 0;
        int total = filenames.size();

        for (String filename : filenames) {

            count++;

            for (EventServiceListener l : listeners) {
                l.loadingFile(count, total);
            }

            if (file_type == FILE_TYPE_S3) {
                try(InputStream stream = loadEventFromS3(s3Client, activeAccount.getBucket(), filename)) {
                    processStream(stream, filters);
                }
                catch (Exception ioe) {
                    LOGGER.log(Level.WARNING, "Failed to load file : " + filename, ioe);
                }
            } else {
                try(InputStream stream = loadEventFromLocalFile(filename)) {
                    processStream(stream, filters);
                }
                catch (Exception ioe) {
                    LOGGER.log(Level.WARNING, "Failed to load file : " + filename, ioe);
                }
            }
        }

        for (EventServiceListener l : listeners) {
            l.finishedLoading(false);
        }
    }

    public void injectEvents(EventServiceListener l) {
        l.newEvents(eventDb);
    }

    public void clearEvents() {

        eventDb.clear();

        for (EventServiceListener l : listeners) {
            l.clearEvents();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private static InputStream loadEventFromLocalFile(final String file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file));
        return new ByteArrayInputStream(encoded);
    }

    private InputStream loadEventFromS3(AmazonS3 s3Client, String bucketName, final String key) {

        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        return s3Object.getObjectContent();
    }

    private String uncompress(InputStream stream) {

        StringBuilder json = new StringBuilder();

        try (GZIPInputStream gzis = new GZIPInputStream(stream, BUFFER_SIZE)) {

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(gzis, "UTF-8"))) {

                String line;
                while ((line = bf.readLine()) != null) {
                    json.append(line);
                }
            }
        }
        catch (ZipException ex) {
            json.append(loadUncompressedFile(stream));
        }
        catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, "File encoding not recognised : ", ex);
        }
        catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Problem uncompressing file data : ", ex);
        }

        return json.toString();
    }

    private String loadUncompressedFile(InputStream stream) {

        StringBuilder json = new StringBuilder();

        Scanner scanner = new Scanner(stream, "UTF-8");
        while (scanner.hasNext()) {
            String line = scanner.next();
            json.append(line.replaceAll("(\\r|\\n|\\t)", ""));
        }

        // check if the first character is a { otherwise add one
        String firstChars = json.substring(0,2);
        if (firstChars.equalsIgnoreCase("Re")) {
            json.insert(0, "{\"");
        } else if (firstChars.equalsIgnoreCase("\"R")) {
            json.insert(0, "{");
        }

        return json.toString();
    }

    private List<Event> createEvents(String json_string) {

        Gson g = new Gson();

        List<Event> events = new ArrayList<>();

        JsonObject jsonObject = new JsonParser().parse(json_string).getAsJsonObject();
        JsonArray records = (JsonArray)jsonObject.get("Records");

        for (Object record : records) {

            try {
                JsonObject obj = (JsonObject) record;
                Event e = g.fromJson(obj, Event.class);
                events.add(e);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Create Event from JSON : ", e);
            }
        }

        return events;
    }

    private void processStream(InputStream stream, CompositeFilter filter) {

        List<Event> events = createEvents(uncompress(stream));
        for (Event event : events) {

            geoService.populateGeoData(event);

            EventUtils.addTimestamp(event);
            if (filter.passes(event)) {

                event.getResourceInfo();
                eventDb.add(event);

                for (EventServiceListener l : listeners) {
                    l.newEvent(event);
                }
            }
        }
    }
}
