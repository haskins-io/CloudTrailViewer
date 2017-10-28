package io.haskins.java.cloudtrailviewer.utils;

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

    public static TopDocs performQuery(String path, String field, String value) throws Exception {

        QueryParser qp = new QueryParser(field, new StandardAnalyzer());
        Query idQuery = qp.parse(value);

        IndexSearcher searcher = createSearcher(path);

        TotalHitCountCollector collector = new TotalHitCountCollector();
        searcher.search(idQuery, collector);
        return searcher.search(idQuery, Math.max(1, collector.getTotalHits()));
    }

    public static TermStats[] getTopFromLucence(String path, int top, String series) throws Exception {

        HighFreqTerms.DocFreqComparator cmp = new HighFreqTerms.DocFreqComparator();
        return HighFreqTerms.getHighFreqTerms(LuceneUtils.getReader(path), top, series, cmp);
    }

    public static IndexWriter createWriter(String path) throws IOException {

        FSDirectory dir = FSDirectory.open(Paths.get(path));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        return new IndexWriter(dir, config);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// private  methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static IndexSearcher createSearcher(String path) throws IOException {

        Directory dir = FSDirectory.open(Paths.get(path));
        IndexReader reader = DirectoryReader.open(dir);
        return new IndexSearcher(reader);
    }

    private static IndexReader getReader(String path) throws IOException {

        FSDirectory dir = FSDirectory.open(Paths.get(path));
        return DirectoryReader.open(dir);

    }
}
