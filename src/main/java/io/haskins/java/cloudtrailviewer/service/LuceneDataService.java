package io.haskins.java.cloudtrailviewer.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.aws.AwsAccount;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.utils.AwsService;
import io.haskins.java.cloudtrailviewer.utils.EventUtils;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import javafx.concurrent.Task;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public abstract class LuceneDataService extends DataService {

    static Logger logger;

    abstract void createDocument(Matcher m);
    abstract void createDocument(Event e);

    abstract String getLucenceDir();

    public static final int FILE_LOCATION_LOCAL = 1;
    public static final int FILE_LOCATION_S3 = 2;

    private static final int BUFFER_SIZE = 32;

    List<Document> documents = new ArrayList<>();

    private Pattern pattern;

    AccountService accountDao;
    AwsService awsService;
    StatusBarController statusBarController;
    GeoService geoService;

    void process(List<String> files, String regexPattern, final CompositeFilter filters, int file_location) {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                AwsAccount activeAccount = null;
                AmazonS3 s3Client = null;

                if (file_location == FILE_LOCATION_S3) {
                    activeAccount = awsService.getActiveAccount(accountDao);
                    s3Client = awsService.getS3Client(activeAccount);
                }

                boolean isJson = false;
                if (regexPattern == null) {
                    isJson = true;
                } else {
                    pattern = Pattern.compile(regexPattern);
                }

                int count = 0;
                for (String filename : files) {

                    count++;

                    String message = "Processing event " + count + " of " + files.size();
                    updateMessage(message);


                    if (file_location == FILE_LOCATION_S3) {
                        try (InputStream stream = loadEventFromS3(s3Client, activeAccount.getBucket(), filename)) {
                            processStream(stream, filters, isJson);
                        } catch (Exception ioe) {
                            logger.log(Level.WARNING, "Failed to load file : " + filename, ioe);
                        }
                    } else if (file_location == FILE_LOCATION_LOCAL) {
                        try (InputStream stream = loadEventFromLocalFile(filename)) {
                            processStream(stream, filters, isJson);
                        } catch (Exception ioe) {
                            logger.log(Level.WARNING, "Failed to load file : " + filename, ioe);
                        }
                    }
                }

                index();

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("");

                for (DataServiceListener l : listeners) {
                    l.finishedLoading(true);
                }

            }
        };

        statusBarController.message.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// file loading methods
    ////////////////////////////////////////////////////////////////////////////
    private static InputStream loadEventFromLocalFile(final String file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file));
        return new ByteArrayInputStream(encoded);
    }

    private InputStream loadEventFromS3(AmazonS3 s3Client, String bucketName, final String key) {

        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        return s3Object.getObjectContent();
    }

    private void processStream(InputStream stream, CompositeFilter filter, boolean isJson) {

        String data = uncompress(stream, isJson);
        if (data != null) {
            List<Event> events = createEvents(data);
            for (Event event : events) {

                EventUtils.addTimestamp(event);
                if (filter.passes(event)) {
                    createDocument(event);
                }
            }
        }
    }

    private List<Event> createEvents(String json_string) {

        Gson g = new Gson();

        List<Event> events = new ArrayList<>();

        JsonObject jsonObject = new JsonParser().parse(json_string).getAsJsonObject();
        JsonArray records = (JsonArray) jsonObject.get("Records");

        for (Object record : records) {

            try {
                JsonObject obj = (JsonObject) record;
                Event e = g.fromJson(obj, Event.class);
                EventUtils.addTimestamp(e);
                events.add(e);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Create Event from JSON : ", e);
            }
        }

        return events;
    }

    private String uncompress(InputStream stream, boolean isJson) {

        StringBuilder data = new StringBuilder();

        try (GZIPInputStream gzis = new GZIPInputStream(stream, BUFFER_SIZE)) {

            try (BufferedReader bf = new BufferedReader(new InputStreamReader(gzis, "UTF-8"))) {

                String line;
                while ((line = bf.readLine()) != null) {
                    data.append(line);
                }
            }

        } catch (ZipException ex) {
            return loadUncompressedFile(stream, isJson);
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.WARNING, "File encoding not recognised : ", ex);
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Problem uncompressing file data : ", ex);
        }

        return data.toString();
    }

    private String loadUncompressedFile(InputStream stream, boolean isJson) {

        if (isJson) {

            StringBuilder data = new StringBuilder();

            Scanner scanner = new Scanner(stream, "UTF-8");
            while (scanner.hasNext()) {
                String line = scanner.next();
                data.append(line.replaceAll("(\\r|\\n|\\t)", ""));
            }

            // check if the first character is a { otherwise add one
            String firstChars = data.substring(0, 2);
            if (firstChars.equalsIgnoreCase("Re")) {
                data.insert(0, "{\"");
            } else if (firstChars.equalsIgnoreCase("\"R")) {
                data.insert(0, "{");
            }

            return data.toString();

        } else {

            try (Scanner scanner = new Scanner(stream, "UTF-8")) {

                while (scanner.hasNext()) {

                    String line = scanner.nextLine().trim().replace("\n", "").replace("\r", "");

                    Matcher m = pattern.matcher(line);
                    if (m.matches()) {
                        createDocument(m);
                    } else {
                        logger.log(Level.INFO, line);
                    }
                }
            }

        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// Lucene utiltiy methods
    ////////////////////////////////////////////////////////////////////////////
    private void index() throws IOException {

        IndexWriter writer = LuceneUtils.createWriter(getLucenceDir());
        writer.deleteAll();
        writer.addDocuments(documents);
        writer.commit();
        writer.close();
    }

}
