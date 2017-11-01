package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import io.haskins.java.cloudtrailviewer.utils.LuceneUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.misc.TermStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;

@Service
public class ElbLogService extends LuceneDataService {

    public final static String LUCENE_DIR = System.getProperty("user.home", ".") + "/.cloudtrailviewer/lucene/elblogs";

    @Autowired
    public ElbLogService(StatusBarController statusBarController, GeoService geoService1) {
        this.statusBarController = statusBarController;
        this.geoService = geoService;

        logger = Logger.getLogger("ElbLogService");
    }

    public void processRecords(List<String> records, CompositeFilter filter, int requestType) {
        String regexPattern = "([^ ]*) ([^ ]*) ([^ ]*):([0-9]*) ([^ ]*):([0-9]*) ([.0-9]*) ([.0-9]*) ([.0-9]*) (-|[0-9]*) (-|[0-9]*) ([-0-9]*) ([-0-9]*) \"([^ ]*) ([^ ]*) (- |[^ ]*)\".* \"(.*?)\".* ([^ ]*) ([^ ]*)";
        process(records, regexPattern,filter, requestType);
    }


    public TermStats[] getTop(int top, String series) throws Exception {
        return LuceneUtils.getTopFromLucence(LUCENE_DIR, top, series);
    }

    String getLucenceDir() {
        return LUCENE_DIR;
    }

    @Override
    void createDocument(Matcher matcher) {

        Document document = new Document();

        document.add(new StringField("timestamp", matcher.group(1) , Field.Store.YES));
        document.add(new StringField("dateTime", matcher.group(1) , Field.Store.YES));

        document.add(new StringField("eventTime", matcher.group(1), Field.Store.YES));
        document.add(new StringField("elb", matcher.group(2), Field.Store.YES));
        document.add(new StringField("clientAddress", matcher.group(3), Field.Store.YES));
        document.add(new StringField("clientPort", matcher.group(4), Field.Store.YES));
        document.add(new StringField("backendAddress", matcher.group(5), Field.Store.YES));
        document.add(new StringField("backendPort", matcher.group(6), Field.Store.YES));
        document.add(new StringField("requestProcessingTime", matcher.group(7), Field.Store.YES));
        document.add(new StringField("backendProcessingTime", matcher.group(8), Field.Store.YES));
        document.add(new StringField("responseProcessingTime", matcher.group(9), Field.Store.YES));
        document.add(new StringField("elbStatusCode", matcher.group(10), Field.Store.YES));
        document.add(new StringField("backendStatusCode", matcher.group(11), Field.Store.YES));
        document.add(new StringField("receivedBytes", matcher.group(12), Field.Store.YES));
        document.add(new StringField("sentByes", matcher.group(13), Field.Store.YES));
        document.add(new StringField("request", matcher.group(14), Field.Store.YES));
        document.add(new StringField("url", matcher.group(15), Field.Store.YES));
        document.add(new StringField("userAgent", matcher.group(17), Field.Store.YES));

        geoService.populateGeoData(document, "elblogs");

        for (DataServiceListener l : listeners) {
            l.newEvent(document);
        }

        documents.add(document);
    }

    void createDocument(Event e) { /* Not needed */ }

    List<? extends AwsData> getDataDb() {
        return null;
    }

}