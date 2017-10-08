package io.haskins.java.cloudtrailviewer.service;

import io.haskins.java.cloudtrailviewer.model.AwsData;
import io.haskins.java.cloudtrailviewer.model.vpclog.VpcFlowLog;
import io.haskins.java.cloudtrailviewer.service.listener.DataServiceListener;
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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VpcFlowLogService extends DataService {

    private final static Logger LOGGER = Logger.getLogger("CloudTrail");

    private static final String REGEX = "^([^ ]*) (\\d) (\\d+) (eni-\\w+) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (ACCEPT|REJECT) (OK|NODATA|SKIPDATA)$";
    private Pattern pattern = Pattern.compile(REGEX);

    private final List<VpcFlowLog> logsDb = new ArrayList<>();

    public void processRecords(List<String> records) {

        for (String record : records) {

            try (Scanner scanner = new Scanner(new File(record))) {

                while (scanner.hasNext()){

                    String line = scanner.nextLine().trim();
                    line = line.replace("\n", "").replace("\r", "");

                    Matcher matcher = pattern.matcher(line);

                    try {
                        VpcFlowLog log = new VpcFlowLog();
                        log.populateFromRegex(matcher);

                        logsDb.add(log);

                        for (DataServiceListener l : listeners) {
                            l.newEvent(log);
                        }
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (DataServiceListener l : listeners) {
            l.finishedLoading(true);
        }
    }

    private InputStream loadEventFromLocalFile(final String file) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(file));
        return new ByteArrayInputStream(encoded);
    }

    public void newEvent(AwsData data) {

        VpcFlowLog event = (VpcFlowLog)data;
        logsDb.add(event);
    }

    public List<VpcFlowLog> getAllLogs() {
        return logsDb;
    }

    List getDataDb() {
        return getAllLogs();
    }
}
