package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ElbLogService extends DataService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private final List<ElbLog> logsDb = new ArrayList<>();

    private final StatusBarController statusBarController;

    @Autowired
    public ElbLogService(StatusBarController statusBarController) {
        this.statusBarController = statusBarController;
    }


    public void processRecords(List<String> records) {

        String regexPattern = "([^ ]*) ([^ ]*) ([^ ]*):([0-9]*) ([^ ]*):([0-9]*) ([.0-9]*) ([.0-9]*) ([.0-9]*) (-|[0-9]*) (-|[0-9]*) ([-0-9]*) ([-0-9]*) \"([^ ]*) ([^ ]*) (- |[^ ]*)\".* \"([^ ]*) ([^ ]*) (- |[^ ]*)\".* ([^ ]*) ([^ ]*)";
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

                            String line = scanner.nextLine().trim();
                            line = line.replace("\n", "").replace("\r", "");

                            Matcher m = pattern.matcher(line);

                            if (m.matches()) {

                                try {
                                    ElbLog log = new ElbLog();
                                    log.populateFromRegex(m);

                                    logsDb.add(log);

                                    for (DataServiceListener l : listeners) {
                                        l.newEvent(log);
                                    }

                                } catch (IllegalStateException e) {
                                    System.out.println(e.getMessage());
                                }

                            } else {
                                LOGGER.log(Level.INFO, line);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @Override
            protected void succeeded() {

                super.succeeded();

                updateMessage("");

                for (DataServiceListener l : listeners) {
                    l.finishedLoading(false);
                }
            }

        };

        statusBarController.message.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

    private InputStream loadEventFromLocalFile(final String file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file));
        return new ByteArrayInputStream(encoded);
    }

    public void newEvent(AwsData data) {

        ElbLog event = (ElbLog)data;
        logsDb.add(event);
    }

    public List<ElbLog> getAllLogs() {
        return logsDb;
    }

    List getDataDb() {
        return getAllLogs();
    }

}
