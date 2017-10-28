package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import javafx.concurrent.Task;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LuceneIndexer extends DataService {

    static Logger LOGGER;

    abstract void createDocument(Matcher m);
    abstract void index() throws IOException;

    StatusBarController statusBarController;

    void process(List<String> records, String regexPattern) {

        Pattern pattern = Pattern.compile(regexPattern);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                int count = 0;
                for (String record : records) {

                    count++;

                    String message = "Processing event " + count + " of " + records.size();
                    updateMessage(message);

                    try (Scanner scanner = new Scanner(new File(record))) {

                        while (scanner.hasNext()) {

                            String line = scanner.nextLine().trim().replace("\n", "").replace("\r", "");

                            Matcher m = pattern.matcher(line);
                            if (m.matches()) {
                                createDocument(m);
                            } else {
                                LOGGER.log(Level.INFO, line);
                            }
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
            }
        };

        statusBarController.message.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    IndexWriter createWriter(String path) throws IOException {

        FSDirectory dir = FSDirectory.open(Paths.get(path));
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        return new IndexWriter(dir, config);
    }
}
