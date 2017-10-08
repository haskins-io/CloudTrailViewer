package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.elblog.ElbLog;
import org.springframework.stereotype.Service;

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

    public void processRecords(List<String> records) {

        for (String record : records) {

            Matcher matcher = pattern.matcher(record);

            ElbLog log = new ElbLog();
//            log.populateFromRegex(matcher);

        }
    }

    public List<ElbLog> getAllLogs() {
        return logsDb;
    }

    List getDataDb() {
        return getAllLogs();
    }

}
