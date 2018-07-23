package io.haskins.java.cloudtrailviewer.service.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.GeoService;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;

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

import static io.haskins.java.cloudtrailviewer.service.EventService.FILE_TYPE_LOCAL;
import static io.haskins.java.cloudtrailviewer.service.EventService.FILE_TYPE_S3;

public class FileProcessingThread implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("FileProcessingThread");

    private static final int BUFFER_SIZE = 32;

    private final AmazonS3 s3Client;

    private final String filename;
    private final int fileType;
    private final CompositeFilter filters;
    private final List<AwsData> eventDb;
    private final AwsAccount activeAccount;
    private final GeoService geoService;
    private final  List<DataServiceListener> listeners;

    public FileProcessingThread(
            String f, int fileType, final CompositeFilter filters,
            AmazonS3 s3Client, AwsAccount activeAccount, GeoService geoService,
            List<AwsData> eventDb,  List<DataServiceListener> listeners) {

        this.filename = f;
        this.fileType = fileType;
        this.filters = filters;

        this.s3Client=s3Client;
        this.activeAccount = activeAccount;
        this.geoService = geoService;

        this.eventDb = eventDb;
        this.listeners = listeners;
    }

    @Override
    public void run() {

        if (fileType == FILE_TYPE_S3) {
            try (InputStream stream = loadEventFromS3(s3Client, activeAccount.getBucket(), filename)) {
                processStream(stream, filters);
            } catch (Exception ioe) {
                LOGGER.log(Level.WARNING, "Failed to load file : " + filename, ioe);
            }
        } else if (fileType == FILE_TYPE_LOCAL) {
            try (InputStream stream = loadEventFromLocalFile(filename)) {
                processStream(stream, filters);
            } catch (Exception ioe) {
                LOGGER.log(Level.WARNING, "Failed to load file : " + filename, ioe);
            }
        }
    }

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
        } catch (ZipException ex) {
            json.append(loadUncompressedFile(stream));
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, "File encoding not recognised : ", ex);
        } catch (IOException ex) {
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
        String firstChars = json.substring(0, 2);
        if (firstChars.equalsIgnoreCase("Re")) {
            json.insert(0, "{\"");
        } else if (firstChars.equalsIgnoreCase("\"R")) {
            json.insert(0, "{");
        }

        return json.toString();
    }

    private List<Event> createEvents(String jsonString) {

        Gson g = new Gson();

        List<Event> events = new ArrayList<>();

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        JsonArray records = (JsonArray) jsonObject.get("Records");

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

            for (DataServiceListener l : listeners) {
                l.scannedEvent();
            }

            EventUtils.addTimestamp(event);
            if (filter.passes(event)) {

                try {
                    geoService.populateGeoData(event);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed populate Location information");
                }

                eventDb.add(event);

                for (DataServiceListener l : listeners) {
                    l.newEvent(event);
                }
            }
        }
    }
}
