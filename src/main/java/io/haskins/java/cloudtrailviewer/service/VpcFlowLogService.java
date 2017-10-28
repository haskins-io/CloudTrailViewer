package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;
import java.util.regex.Matcher;


@Service
public class VpcFlowLogService extends LuceneIndexer {

    private final static String LUCENE_DIR = System.getProperty("user.home", ".") + "/.cloudtrailviewer/lucene/vpclogs";

    private List<Document> documents = new ArrayList<>();

    @Autowired
    public VpcFlowLogService(StatusBarController statusBarController, GeoService geoService1) {
        this.statusBarController = statusBarController;

        LOGGER = Logger.getLogger("VpcFlowLogService");
    }

    public void processRecords(List<String> records) {
        String regexPattern = "^([^ ]*) (\\d) (\\d+) (eni-\\w+) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (ACCEPT|REJECT) (OK|NODATA|SKIPDATA)$";
        process(records, regexPattern);
    }

    @Override
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

        documents.add(document);
    }


    @Override
    void index() throws IOException {

        IndexWriter writer = createWriter(LUCENE_DIR);
        writer.deleteAll();
        writer.addDocuments(documents);
        writer.commit();
        writer.close();
    }

    @Override
    List<? extends AwsData> getDataDb() {
        return null;
    }
}
