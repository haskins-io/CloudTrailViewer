package io.haskins.java.cloudtrailviewer.utils;

import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import io.haskins.java.cloudtrailviewer.model.event.Event;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneUtils {

    public final static String VPC_DIR = System.getProperty("user.home", ".") + "/.cloudtrailviewer/lucene/vpclogs";
    public final static String CLOUDTRAIL_DIR = System.getProperty("user.home", ".") + "/.cloudtrailviewer/lucene/cloudtrail";
    public final static String ELB_DIR = System.getProperty("user.home", ".") + "/.cloudtrailviewer/lucene/elblogs";

    public static TopDocs performQuery(String type, String field, String value) throws Exception {

        QueryParser qp = new QueryParser(field, new StandardAnalyzer());
        Query idQuery = qp.parse(value);

        IndexSearcher searcher = createSearcher(type);

        TotalHitCountCollector collector = new TotalHitCountCollector();
        searcher.search(idQuery, collector);
        return searcher.search(idQuery, Math.max(1, collector.getTotalHits()));
    }

    public static TermStats[] getTopFromLucence(String type, int top, String series) throws Exception {

        HighFreqTerms.DocFreqComparator cmp = new HighFreqTerms.DocFreqComparator();
        return HighFreqTerms.getHighFreqTerms(LuceneUtils.getReader(type), top, series, cmp);
    }

    public static IndexWriter createWriter(String type) throws IOException {

        FSDirectory dir = FSDirectory.open(Paths.get(getLucenePath(type)));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        return new IndexWriter(dir, config);
    }

    public static IndexSearcher createSearcher(String type) throws IOException {

        Directory dir = FSDirectory.open(Paths.get(getLucenePath(type)));
        IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
    }

    public static IndexReader getReader(String type) throws IOException {

        FSDirectory dir = FSDirectory.open(Paths.get(getLucenePath(type)));
        return DirectoryReader.open(dir);

    }

    public static String getLucenePath(String type) {

        switch(type) {
            case Event.TYPE:
                return CLOUDTRAIL_DIR;
            case VpcFlowLog.TYPE:
                return VPC_DIR;
            case ElbLog.TYPE:
                return ELB_DIR;
            default:
                return "";
        }
    }

}
