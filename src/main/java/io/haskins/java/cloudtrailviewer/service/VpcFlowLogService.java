package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.filter.CompositeFilter;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.event.Event;
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
public class VpcFlowLogService extends LuceneDataService {

    public final static String LUCENE_DIR = System.getProperty("user.home", ".") + "/.cloudtrailviewer/lucene/vpclogs";

    @Autowired
    public VpcFlowLogService(StatusBarController statusBarController, GeoService geoService1) {
        this.statusBarController = statusBarController;
        this.geoService = geoService;

        LOGGER = Logger.getLogger("VpcFlowLogService");
    }

    public void processRecords(List<String> records, CompositeFilter filter, int requestType) {
        String regexPattern = "^([^ ]*) (\\d) (\\d+) (eni-\\w+) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (ACCEPT|REJECT) (OK|NODATA|SKIPDATA)$";
        process(records, regexPattern, filter, requestType);
    }


    public TermStats[] getTop(int top, String series) throws Exception {
        return LuceneUtils.getTopFromLucence(LUCENE_DIR, top, series);
    }

    String getLucenceDir() {
        return LUCENE_DIR;
    }

    void createDocument(Matcher matcher) {

        Document document = new Document();

        document.add(new StringField("version", matcher.group(2) , Field.Store.YES));
        document.add(new StringField("accountId", matcher.group(3) , Field.Store.YES));
        document.add(new StringField("interfaceId", matcher.group(4) , Field.Store.YES));
        document.add(new StringField("srcaddr", matcher.group(5) , Field.Store.YES));
        document.add(new StringField("dstaddr", matcher.group(6) , Field.Store.YES));
        document.add(new StringField("srcport", matcher.group(7) , Field.Store.YES));
        document.add(new StringField("dstport", matcher.group(8) , Field.Store.YES));
        document.add(new StringField("protocol", matcher.group(9) , Field.Store.YES));
        document.add(new StringField("packets", matcher.group(10) , Field.Store.YES));
        document.add(new StringField("bytes", matcher.group(11) , Field.Store.YES));
        document.add(new StringField("start", matcher.group(12) , Field.Store.YES));
        document.add(new StringField("end", matcher.group(13) , Field.Store.YES));
        document.add(new StringField("action", matcher.group(14) , Field.Store.YES));
        document.add(new StringField("logStatus", matcher.group(15) , Field.Store.YES));

        geoService.populateGeoData(document, "vpclogs");

        documents.add(document);
    }

    void createDocument(Event e) { /* Not needed */ }

    List<? extends AwsData> getDataDb() {
        return null;
    }

}
