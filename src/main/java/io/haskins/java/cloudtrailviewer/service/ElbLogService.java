package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.controller.components.StatusBarController;
import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ElbLogService extends DataService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private static final String REGEX = "([^ ]*) ([^ ]*) ([^ ]*):([0-9]*) ([^ ]*):([0-9]*) ([.0-9]*) ([.0-9]*) ([.0-9]*) (-|[0-9]*) (-|[0-9]*) ([-0-9]*) ([-0-9]*) \"([^ ]*) ([^ ]*) (- |[^ ]*)\".* \"([^ ]*) ([^ ]*) (- |[^ ]*)\".* ([^ ]*) ([^ ]*)";
    private Pattern pattern = Pattern.compile(REGEX);

    private final List<ElbLog> logsDb = new ArrayList<>();


    private final StatusBarController statusBarController;

    @Autowired
    public ElbLogService(StatusBarController statusBarController) {
        this.statusBarController = statusBarController;
    }


    public void processRecords(List<String> records) {

        for (String record : records) {

            Matcher matcher = pattern.matcher(record);

            ElbLog log = new ElbLog();
//            log.populateFromRegex(matcher);

        }
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
